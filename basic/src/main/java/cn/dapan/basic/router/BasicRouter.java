package cn.dapan.basic.router;

import android.app.Activity;

import java.util.Map;

import cn.dapan.basic.MainBasicActivity;
import cn.dapan.router.api.IRouteDelegate;

public class BasicRouter implements IRouteDelegate {

    @Override
    public void load(Map<String, Class<? extends Activity>> routers) {
        routers.put("basic/main", MainBasicActivity.class);
    }
}
