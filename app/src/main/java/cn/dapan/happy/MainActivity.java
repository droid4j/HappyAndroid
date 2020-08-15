package cn.dapan.happy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.dapan.router.annotation.Route;
import cn.dapan.router.api.Router;

@Route("/app/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoBasic(View view) {
        Router.getInstance().start(this, "/basic/main");
    }
}