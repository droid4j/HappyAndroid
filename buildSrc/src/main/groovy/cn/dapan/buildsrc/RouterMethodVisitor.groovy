package cn.dapan.buildsrc

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class RouterMethodVisitor extends AdviceAdapter {

    private String desc
    private List<String> nameList

    RouterMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, List<String> nameList) {
        super(Opcodes.ASM5, methodVisitor, access, name, descriptor)
        desc = descriptor
        this.nameList = nameList
    }

    @Override
    void visitCode() {
        super.visitCode()
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        def nameDesc = name + desc
        if (nameDesc == 'loadRouterMap()V') {

            for (String name : nameList) {
                mv.visitTypeInsn(NEW, name)
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false)
                mv.visitFieldInsn(GETSTATIC, "cn/dapan/router/api/Router", "routers", "Ljava/util/Map;")
                mv.visitMethodInsn(INVOKEVIRTUAL, name, "load", "(Ljava/util/Map;)V", false)
            }

//            mv.visitTypeInsn(NEW, "cn/dapan/routers/basicRouter")
//            mv.visitInsn(DUP)
//            mv.visitMethodInsn(INVOKESPECIAL, "cn/dapan/routers/basicRouter", "<init>", "()V", false)
//            mv.visitFieldInsn(GETSTATIC, "cn/dapan/router/api/Router", "routers", "Ljava/util/Map;")
//            mv.visitMethodInsn(INVOKEVIRTUAL, "cn/dapan/routers/basicRouter", "load", "(Ljava/util/Map;)V", false)


            mv.visitInsn(ICONST_1)
            mv.visitFieldInsn(PUTSTATIC, "cn/dapan/router/api/Router", "isRegisterFromPlugin", "Z")

            println "method enter..."

        }
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()


    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible)
    }
}
