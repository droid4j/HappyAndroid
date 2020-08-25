package cn.dapan.download;

import android.content.Context;
import android.content.Intent;

/**
 * 下载操作入口类
 * DownloadManager -> DownloadService -> DataWatcher
 *
 * Manager 提交下载任务给 Service，然后由 DataChanger 分发状态
 */
public class DownloadManager {

    private final Context context;
    private static DownloadManager sInstance;

    private DownloadManager(Context context) {
        this.context = context;
    }

    public static synchronized DownloadManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DownloadManager(context);
        }
        return sInstance;
    }

    public void add(DownloadEntity entity) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, entity);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ADD);
        context.startService(intent);
    }

    public void pause(DownloadEntity entity){
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, entity);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_PAUSE);
        context.startService(intent);
    }

    public void resume(DownloadEntity entity){
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, entity);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_RESUME);
        context.startService(intent);
    }

    public void cancel(DownloadEntity entity){
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTITY, entity);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_CANCEL);
        context.startService(intent);
    }

    public void addObserver(DataWatcher watcher) {
        DataChanger.getInstance().addObserver(watcher);
    }

    public void removeObserver(DataWatcher watcher) {
        DataChanger.getInstance().deleteObserver(watcher);
    }
}
