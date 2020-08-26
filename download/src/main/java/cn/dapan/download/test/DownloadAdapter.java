package cn.dapan.download.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.dapan.download.DownloadEntity;
import cn.dapan.download.DownloadManager;
import cn.dapan.download.R;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.Holder> {

    private List<DownloadEntity> list;
    private DownloadManager mDownloadManager;

    public DownloadAdapter(List<DownloadEntity> list, DownloadManager downloadManager) {
        this.list = list;
        this.mDownloadManager = downloadManager;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final DownloadEntity entity = list.get(position);
        holder.textView.setText(entity.toString());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.status == DownloadEntity.DownloadStatus.idle) {
                    mDownloadManager.add(entity);
                } else if (entity.status == DownloadEntity.DownloadStatus.downloading) {
                    mDownloadManager.pause(entity);
                } else if (entity.status == DownloadEntity.DownloadStatus.pause) {
                    mDownloadManager.resume(entity);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView textView;
        public Button button;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
