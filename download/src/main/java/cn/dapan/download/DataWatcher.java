package cn.dapan.download;

import java.util.Observable;
import java.util.Observer;
/**
 * 观查者模式，监听下载状态变化
 */
public abstract class DataWatcher implements Observer {
    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownloadEntity) {
            notifyUpdate((DownloadEntity)data);
        }
    }

    public abstract void notifyUpdate(DownloadEntity data);
}
