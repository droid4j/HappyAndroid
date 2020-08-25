package cn.dapan.buildsrc

import jdk.internal.org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class RouterClassVisitor extends ClassVisitor implements Opcodes {

    private ClassVisitor classVisitor
    private List<String> nameList

    RouterClassVisitor(ClassWriter classWriter, List<String> nameList) {
        super(Opcodes.ASM5, classWriter)
        this.classVisitor = classWriter
        this.nameList = nameList
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        // println "visit version: $version, access: $access, name: $name, signature: $signature, superName: $superName"
        if (interfaces != null && interfaces.length > 0) {
            for (String item : interfaces) {
                if (item == 'cn/dapan/router/api/IRouteDelegate') {
                    nameList.add(name)
                    println "visit: $name"
                }
            }
        }
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        println "visitMethod: $name, $descriptor"
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
        methodVisitor = new RouterMethodVisitor(methodVisitor, access, name, descriptor, nameList)
        // println "visit method desc: $nameDesc"
        return methodVisitor
    }
}
