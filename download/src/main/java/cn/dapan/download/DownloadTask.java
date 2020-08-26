package cn.dapan.download;

public class DownloadTask implements Runnable {

    private final DownloadEntity entity;
    private boolean isPaused;
    private boolean isCancelled;

    public DownloadTask(DownloadEntity entity) {
        this.entity = entity;
    }

    public void pause() {
        isPaused = true;
    }

    public void cancel() {
        isCancelled = true;
    }

    public void start() {
        entity.status = DownloadEntity.DownloadStatus.waiting;
        DataChanger.getInstance().postStatus(entity);

        entity.totalLength = 1024 * 100;

        for (int i = entity.currentLength; i < entity.totalLength; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isPaused || isCancelled) {
                entity.status = isPaused ? DownloadEntity.DownloadStatus.pause : DownloadEntity.DownloadStatus.cancel;
                DataChanger.getInstance().postStatus(entity);
                return;
                // todo if cancelled, delete related file
            }
            i += 1024;
            entity.currentLength += 1024;
            entity.status = DownloadEntity.DownloadStatus.downloading;
            DataChanger.getInstance().postStatus(entity);
        }

        entity.status = DownloadEntity.DownloadStatus.completed;
        DataChanger.getInstance().postStatus(entity);
    }

    @Override
    public void run() {
        start();
    }
}
