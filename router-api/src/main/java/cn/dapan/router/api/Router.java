package cn.dapan.router.api;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Router {

    private static Router instance = new Router();
    private static boolean isRegisterFromPlugin;

    public static Router getInstance() {
        return instance;
    }

    private Router() {

    }

    private static Map<String, Class<? extends Activity>> routers = new HashMap<>();

    private static void loadRouterMap() {
        isRegisterFromPlugin = false;
//        new basicRouter().load(routers);
//        new appRouter().load(routers);
//        new Router().toString();
//        isRegisterFromPlugin = true;
    }

    public static void init(Application context) {

        loadRouterMap();
        if (isRegisterFromPlugin) {
            return;
        }

        try {
            Set<String> classNames = ClassUtils.getFileNameByPackageName(context, "cn.dapan.routers");
            for (String className : classNames) {
                Class<?> aClass = Class.forName(className);
                if (IRouteDelegate.class.isAssignableFrom(aClass)) {
                    IRouteDelegate iRouteDelegate = (IRouteDelegate) aClass.newInstance();
                    iRouteDelegate.load(routers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(String path, Class<? extends Activity> clazz) {
        routers.put(path, clazz);
    }

    public void start(Activity activity, String path) {
        Class<? extends Activity> aClass = routers.get(path);
        if (aClass != null) {
            Intent intent = new Intent(activity, aClass);
            activity.startActivity(intent);
        }
    }


}
