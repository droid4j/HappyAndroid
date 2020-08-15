package cn.dapan.basic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.dapan.router.annotation.Route;
import cn.dapan.router.api.Router;

@Route("/basic/main")
public class MainBasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_basic);
    }

    public void gotoApp(View view) {
        Router.getInstance().start(this, "/app/main");
    }
}
