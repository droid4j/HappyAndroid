package cn.dapan.download;

import java.io.Serializable;

public class DownloadEntity implements Serializable {

    public String id;
    public String name;
    public String url;

    public DownloadEntity(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public DownloadStatus status;

    public int currentLength;
    public int totalLength;

    public DownloadEntity() {

    }

    public enum  DownloadStatus {
        idle, waiting, downloading, pause, resume, cancel, completed
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
//                ", url='" + url + '\'' +
                ", status=" + status +
                ", " + currentLength +
                "/" + totalLength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadEntity entity = (DownloadEntity) o;
        return id.equals(entity.id);
    }
}
