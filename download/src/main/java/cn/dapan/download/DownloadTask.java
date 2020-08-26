package cn.dapan.download;

import android.os.Handler;
import android.os.Message;

public class DownloadTask implements Runnable {

    private final DownloadEntity entity;
    private boolean isPaused;
    private boolean isCancelled;
    private Handler mHandler;

    public DownloadTask(DownloadEntity entity, Handler handler) {
        this.entity = entity;
        this.mHandler = handler;
    }

    public void pause() {
        isPaused = true;
    }

    public void cancel() {
        isCancelled = true;
    }

    public void start() {
        entity.status = DownloadEntity.DownloadStatus.downloading;
        // DataChanger.getInstance().postStatus(entity);
        Message message = mHandler.obtainMessage();
        message.obj = entity;
        message.sendToTarget();

        entity.totalLength = 1024 * 100;

        for (int i = entity.currentLength; i < entity.totalLength; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isPaused || isCancelled) {
                entity.status = isPaused ? DownloadEntity.DownloadStatus.pause : DownloadEntity.DownloadStatus.cancel;
                // DataChanger.getInstance().postStatus(entity);
                message = mHandler.obtainMessage();
                message.obj = entity;
                message.sendToTarget();
                return;
                // todo if cancelled, delete related file
            }
            i += 1024;
            entity.currentLength += 1024;
            entity.status = DownloadEntity.DownloadStatus.downloading;
            // DataChanger.getInstance().postStatus(entity);
            message = mHandler.obtainMessage();
            message.obj = entity;
            message.sendToTarget();
        }

        entity.status = DownloadEntity.DownloadStatus.completed;
        // DataChanger.getInstance().postStatus(entity);
        message = mHandler.obtainMessage();
        message.obj = entity;
        message.sendToTarget();
    }

    @Override
    public void run() {
        start();
    }
}
