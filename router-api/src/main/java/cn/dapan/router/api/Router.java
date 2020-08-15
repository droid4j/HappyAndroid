package cn.dapan.router.api;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private static Router instance = new Router();

    public static Router getInstance() {
        return instance;
    }

    private Router() {

    }

    private static Map<String, Class<? extends Activity>> routers = new HashMap<>();

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
