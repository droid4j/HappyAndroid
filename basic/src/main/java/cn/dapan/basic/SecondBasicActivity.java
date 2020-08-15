package cn.dapan.basic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.dapan.router.annotation.Route;

@Route("/basic/second")
public class SecondBasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_basic);
    }
}
