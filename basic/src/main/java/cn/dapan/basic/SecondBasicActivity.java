package cn.dapan.basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.dapan.download.MainDownloadActivity;
import cn.dapan.router.annotation.Route;

@Route("/basic/second")
public class SecondBasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);
    }

    public void gotoDownload(View view) {
        Intent intent = new Intent(this, MainDownloadActivity.class);
        startActivity(intent);
    }
}
