package cn.dapan.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {

    private ExecutorService mExecutor;
    private Map<String, DownloadTask> mTasks = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = Executors.newCachedThreadPool();
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

            case Constants.KEY_DOWNLOAD_RESUME:
                resumeDownload(entity);
                break;

            case Constants.KEY_DOWNLOAD_CANCEL:
                cancelDownload(entity);
                break;

            case Constants.KEY_DOWNLOAD_PAUSE:
                pauseDownload(entity);
                break;
        }
    }

    private void pauseDownload(DownloadEntity entity) {
        DownloadTask task = mTasks.remove(entity.id);
        if (task != null) {
            task.pause();
        }
    }

    private void cancelDownload(DownloadEntity entity) {
        DownloadTask task = mTasks.remove(entity.id);
        if (task != null) {
            task.cancel();
        }
    }

    private void resumeDownload(DownloadEntity entity) {
        startDownload(entity);
    }

    private void startDownload(DownloadEntity entity) {
        DownloadTask task = new DownloadTask(entity);
        mTasks.put(entity.id, task);
        mExecutor.execute(task);
    }

}
