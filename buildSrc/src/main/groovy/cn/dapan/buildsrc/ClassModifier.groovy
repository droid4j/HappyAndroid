package cn.dapan.buildsrc

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class ClassModifier {

    private static Set<String> exclude = new HashSet<>()
    static {
        exclude = new HashSet<>()
        exclude.add('android.support')
        // exclude.add('cn.dapan.router.api')
    }

    protected static boolean isShouldModify(String className) {
        Iterator<String> iterator = exclude.iterator()
        while (iterator.hasNext()) {
            String packageName = iterator.next()

            if (className.startsWith(packageName)) {
                return false
            }
        }

        if (className.contains('R$') ||
        className.contains('R2$') ||
        className.contains('R.class') ||
        className.contains('R2.class') ||
        className.contains('BuildConfig.class')) {
            return false
        }

//        if (className == 'cn.dapan.router.api.Router') {
//            return true
//        }
//        return false
        return true
    }

    static File modifyClassFile(File dir, File classFile, File tempDir) {
        File modified = null
        try {
            String className = path2ClassName(classFile.absolutePath.replace(dir.absolutePath + File.separator, ""))
            byte[] sourceClassBytes = IOUtils.toByteArray(new FileInputStream(classFile))
            byte[] modifiedClassBytes = modifyClass(sourceClassBytes)
            if (modifiedClassBytes) {
                modified = new File(tempDir, className.replace('.', '') + '.class')
                if (modified.exists()) {
                    modified.delete()
                }
                modified.createNewFile()
                new FileOutputStream(modified).write(modifiedClassBytes)
            }
        } catch (IOException e) {
            e.printStackTrace()
            modified = classFile
        }
        return modified
    }

    static String path2ClassName(String pathName) {
        String name = pathName.replace(File.separator, ".").replace(".class", "")
        return name
    }

    private static List<String> nameList = new ArrayList<>()
    private static byte[] modifyClass(byte[] srcClass) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        ClassVisitor visitor = new RouterClassVisitor(writer, nameList)
        ClassReader reader = new ClassReader(srcClass)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    static File modifyJar(File jarFile, File tempDir, boolean  nameHex) {
        def file = new JarFile(jarFile)
        def hexName = ""
        if (nameHex) {
            hexName = DigestUtils.md5Hex(jarFile.absolutePath).substring(0, 8)
        }
        def outputJar = new File(tempDir, hexName + jarFile.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
        Enumeration enumeration = file.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            // println "jar entry: $jarEntry"
            InputStream inputStream = file.getInputStream(jarEntry)

            String entryName = jarEntry.getName()

            ZipEntry zipEntry = new ZipEntry(entryName)

            jarOutputStream.putNextEntry(zipEntry)

            byte[] modifiedClassBytes = null
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream)
            if (entryName.endsWith(".class")) {
                String className = entryName.replace("/", ".").replace(".class", "")
                // println "modify class: $className"
                if (isShouldModify(className)) {
                    // println "should modify class: $className"
                    modifiedClassBytes = modifyClass(sourceClassBytes)
                }
            }

            if (modifiedClassBytes == null) {
                modifiedClassBytes = sourceClassBytes
            }
            jarOutputStream.write(modifiedClassBytes)
            jarOutputStream.closeEntry()
            inputStream.close()
        }
        jarOutputStream.close()
        file.close()
        return outputJar
    }
}