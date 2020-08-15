package cn.dapan.happy;

import android.app.Application;

import cn.dapan.router.api.Router;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRoutes();
    }

    private void initRoutes() {
        Router.init(this);
        // Router.getInstance().register("app/main", MainActivity.class);
    }
}
