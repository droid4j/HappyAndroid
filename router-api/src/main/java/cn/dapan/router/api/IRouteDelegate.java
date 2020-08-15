package cn.dapan.router.api;

import android.app.Activity;

import java.util.Map;

public interface IRouteDelegate {
    void load(Map<String, Class<? extends Activity>> routers);
}
