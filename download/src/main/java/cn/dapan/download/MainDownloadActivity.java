package cn.dapan.download;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.dapan.download.test.DownloadListActivity;

public class MainDownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private DownloadManager mDownloadManager;
    private DownloadEntity entity;
    private DataWatcher watcher = new DataWatcher() {
        @Override
        public void notifyUpdate(DownloadEntity data) {
            DLog.e(data.toString());
            entity = data;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_main);
        Button mDownloadBtn = findViewById(R.id.downloadBtn);
        mDownloadBtn.setOnClickListener(this);
        Button pauseBtn = findViewById(R.id.pauseBtn);
        pauseBtn.setOnClickListener(this);
        Button listBtn = findViewById(R.id.listBtn);
        listBtn.setOnClickListener(this);
        mDownloadManager = DownloadManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadManager.addObserver(watcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDownloadManager.removeObserver(watcher);
    }


    @Override
    public void onClick(View v) {
        if (entity == null) {
            entity = new DownloadEntity();
            entity.name = "test.jpg";
            entity.url = "https://www.baidu.com";
            entity.id = "1";
        }
        if (v.getId() == R.id.downloadBtn) {
            mDownloadManager.add(entity);
        } else if (v.getId() == R.id.pauseBtn) {
            if (entity.status == DownloadEntity.DownloadStatus.downloading) {
                mDownloadManager.pause(entity);
            } else if (entity.status == DownloadEntity.DownloadStatus.pause) {
                mDownloadManager.resume(entity);
            }
        } else if (v.getId() == R.id.listBtn) {
            Intent intent = new Intent(this, DownloadListActivity.class);
            startActivity(intent);
        }
    }
}