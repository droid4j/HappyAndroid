package cn.dapan.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DownloadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownloadEntity entity = (DownloadEntity) intent.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTITY);
        int action = intent.getIntExtra(Constants.KEY_DOWNLOAD_ACTION, -1);
        doAction(action, entity);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 任务分发
     * @param action
     * @param entity
     */
    private void doAction(int action, DownloadEntity entity) {
        switch (action) {
            case Constants.KEY_DOWNLOAD_ADD:
                startDownload(entity);
                break;
        }
    }

    private void startDownload(DownloadEntity entity) {
        entity.status = DownloadEntity.DownloadStatus.waiting;
        DataChanger.getInstance().postStatus(entity);

        entity.totalLength = 1024 * 100;

        for (int i = entity.currentLength; i < entity.totalLength; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i += 1024;
            entity.currentLength += 1024;
            entity.status = DownloadEntity.DownloadStatus.downloading;
            DataChanger.getInstance().postStatus(entity);
        }

        entity.status = DownloadEntity.DownloadStatus.completed;
        DataChanger.getInstance().postStatus(entity);
    }

}
