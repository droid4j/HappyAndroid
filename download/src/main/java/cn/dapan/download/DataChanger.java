package cn.dapan.download;

import java.util.Observable;

public class DataChanger extends Observable {

    private static DataChanger sInstance = new DataChanger();
    private DataChanger() {}

    public static DataChanger getInstance() {
        return sInstance;
    }

    public void postStatus(DownloadEntity entity) {
        setChanged();
        notifyObservers(entity);
    }
}
