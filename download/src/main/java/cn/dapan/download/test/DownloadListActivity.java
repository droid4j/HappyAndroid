package cn.dapan.download.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.dapan.download.DLog;
import cn.dapan.download.DataWatcher;
import cn.dapan.download.DownloadEntity;
import cn.dapan.download.DownloadManager;
import cn.dapan.download.R;

public class DownloadListActivity extends AppCompatActivity {

    private RecyclerView downloadList;
    private DownloadManager mDownloadManager;
    private List<DownloadEntity> list = new ArrayList<>();

    private DataWatcher watcher = new DataWatcher() {
        @Override
        public void notifyUpdate(DownloadEntity data) {
            int index = list.indexOf(data);
            if (index != -1) {
                list.remove(index);
                list.add(index, data);
                mAdapter.notifyDataSetChanged();
            }
            DLog.e(data.toString());
        }
    };
    private DownloadAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        mDownloadManager = DownloadManager.getInstance(this);
        downloadList = findViewById(R.id.downloadList);
        config();
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

    private void config() {
        downloadList.setLayoutManager(new LinearLayoutManager(this));
        downloadList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        data();
        mAdapter = new DownloadAdapter(list, mDownloadManager);
        downloadList.setAdapter(mAdapter);
    }

    private void data() {
        for (int i = 0; i < 30; i++) {
            DownloadEntity entity = new DownloadEntity();
            entity.name = "test_" + i + ".jpg";
            entity.url = "https://www.baidu.com/" + i;
            entity.id = "1" + i;
            entity.status = DownloadEntity.DownloadStatus.idle;
            list.add(entity);
        }
    }
}