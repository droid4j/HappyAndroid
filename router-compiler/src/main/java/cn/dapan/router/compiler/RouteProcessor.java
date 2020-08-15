package cn.dapan.router.compiler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import cn.dapan.router.annotation.Route;

// @SupportedAnnotationTypes("cn.dapan.router.annotation.Route")
@SupportedOptions("moduleName")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RouteProcessor extends AbstractProcessor {

    private String mModuleName;
    private boolean hasCreated = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Map<String, String> options = processingEnv.getOptions();
        mModuleName = options.get("moduleName");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Route.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (hasCreated) {
            return false;
        }

        System.out.println("===================================");
        System.out.println("=                                 =");
        System.out.println("=          Router Processor       =");
        System.out.println("=                                 =");
        System.out.println("===================================");

        String top = "package cn.dapan.routers;\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "\n" +
                "import cn.dapan.basic.MainBasicActivity;\n" +
                "import cn.dapan.router.api.IRouteDelegate;\n" +
                "\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class " + mModuleName + "Router implements IRouteDelegate {\n" +
                "\t@Override\n" +
                "\tpublic void load(Map<String, Class<? extends Activity>> routers) {\n";

        String bottom = "\t}\n" +
                "}\n";

        StringBuilder routers = new StringBuilder();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Route.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;

            String name = typeElement.getQualifiedName().toString();

            Route route = typeElement.getAnnotation(Route.class);
            String path = route.value();

            System.out.println(name);
            routers.append("\t\trouters.put(\"").append(path).append("\", ").append(name).append(".class);\n");
        }

        String code = top + routers.toString() + bottom;




        Filer filer = processingEnv.getFiler();

        try {
            JavaFileObject sourceFile = filer.createSourceFile("cn.dapan.routers." + mModuleName + "Router");
            OutputStream os = sourceFile.openOutputStream();
            os.write(code.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hasCreated = true;

        return false;
    }
}