package cn.dapan.buildsrc

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class MyTransform extends Transform {

    private Project project

    MyTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "MyTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context,
                   Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider,
                   boolean isIncremental)
            throws IOException, TransformException, InterruptedException {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)

        if (!isIncremental) {
            outputProvider.deleteAll()
        }

        // println "transform----> $inputs"

        inputs.each {TransformInput input ->

            if (input.directoryInputs != null && input.directoryInputs.size() > 0) {
                // println "input directory : $input.directoryInputs"
                eachDirectory(context, outputProvider, input.directoryInputs)
            }

            if (input.jarInputs != null && input.jarInputs.size() > 0) {
                // println "input jar : $input.jarInputs"
                eachJar(context, outputProvider, input.jarInputs)
            }

        }
    }

    private static void eachDirectory(Context context, TransformOutputProvider outputProvider, Collection<DirectoryInput> directoryInputs) {
        directoryInputs.each { DirectoryInput directoryInput ->
            File dest = outputProvider.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

            // println "name -> $dest"

            File dir = directoryInput.file

            if (dir) {
                HashMap<String, File> modifyMap = new HashMap<>()

                dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                    File classFile ->
                        if (ClassModifier.isShouldModify(classFile.name)) {
                            File modified = ClassModifier.modifyClassFile(dir, classFile, context.getTemporaryDir())

                            if (modified != null) {
                                String key = classFile.absolutePath.replace(dir.absolutePath, '')
                                // println "dir: $key"
                                modifyMap.put(key, modified)
                            }
                        }
                }
                FileUtils.copyDirectory(directoryInput.file, dest)
                modifyMap.entrySet().each {
                    Map.Entry<String, File> en ->
                        File target = new File(dest.absolutePath + en.getKey())
                        if (target.exists()) {
                            target.delete()
                        }
                        FileUtils.copyFile(en.getValue(), target)
                        en.getValue().delete()
                }
            }
        }
    }

    private static void eachJar(Context context, TransformOutputProvider outputProvider, Collection<JarInput> input ) {
        input.each { JarInput jarInput ->

            String destName = jarInput.file.name

            // println "dest name: $destName"

            def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8)

            if (destName.endsWith(".jar")) {
                destName = destName.substring(0, destName.length() - 4)
            }

            def dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)

            // println "start modify: $dest"

            def modifiedJar = ClassModifier.modifyJar(jarInput.file, context.getTemporaryDir(), true)

            if (modifiedJar == null) {
                modifiedJar = jarInput.file
            }

            // println "jar name - > $dest"
            FileUtils.copyFile(modifiedJar, dest)
        }
    }
}