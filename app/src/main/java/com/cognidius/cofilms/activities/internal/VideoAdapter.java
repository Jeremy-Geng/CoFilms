package com.cognidius.cofilms.activities.internal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.player.MediaPlayerActivity;
import com.cognidius.cofilms.database.room.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videoList;
    private Context context;

    public VideoAdapter(Context context) {
        this.context = context;
    }

    public void setVideoList(List<Video> videos){
        videoList = videos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_video_list_for_user, parent,false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, final int position) {
        holder.videoTitle.setText(videoList.get(position).getVideoTitle());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerActivity.setCurrentVideo(videoList.get(position));
                Intent intent = new Intent(context, MediaPlayerActivity.class);
                context.startActivity(intent);
            }
        });

//        Glide.with(context)
//                .asBitmap()
//                .load(videoList.get(position).getThumbnailurl())
//                .into(holder.videoThumbnail);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private TextView videoTitle;
        private ImageView videoThumbnail;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.user_parent);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoThumbnail = itemView.findViewById(R.id.thumbNailImage);

        }
    }


}
