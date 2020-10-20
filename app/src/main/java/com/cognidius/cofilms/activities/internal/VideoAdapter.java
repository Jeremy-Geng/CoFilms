package com.cognidius.cofilms.activities.internal;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.player.MediaPlayerActivity;
import com.cognidius.cofilms.database.room.Video;

import java.io.File;
import java.io.PipedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> implements Filterable {
    private List<Video> videoList;
    private List<Video> videoListAll;
    private Context context;
    private ContextWrapper cw;
    private static List<Integer> countAnswers = new ArrayList<>();


    public VideoAdapter(Context context) {
        this.context = context;
        cw = new ContextWrapper(context);
    }

    public void setVideoList(List<Video> videos){
        videoList = videos;
        videoListAll = new ArrayList<>(videoList);
        if(videos.size() > countAnswers.size()){
            countAnswers.add(0);
        }
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
                //MediaPlayerActivity.setPreProcess(true);
                Intent intent = new Intent(context, MediaPlayerActivity.class);
                context.startActivity(intent);
            }
        });

        File parentPath = new File(cw.getExternalCacheDir(),"/" + videoList.get(position).getVideoId());
        File[] answers = parentPath.listFiles();
        if(answers.length - 1 > countAnswers.get(position)) {
            holder.newAnswerNotify.setVisibility(View.VISIBLE);
            countAnswers.set(position, answers.length - 1);
        }else if(answers.length - 1 == countAnswers.get(position)){
            holder.newAnswerNotify.setVisibility(View.INVISIBLE);
        }


        if(answers.length > 1){
            holder.isAnswered.setText("Answered");
            holder.isAnswered.setTextColor(cw.getResources().getColor(R.color.red) );
        }



        Glide.with(context)
                .asBitmap()
                .load(videoList.get(position).getThumbnailUrl())
                .into(holder.videoThumbnail);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Video> filterList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filterList.addAll(videoListAll);
            }else{
                for(Video video : videoListAll ){
                    if(video.getVideoTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filterList.add(video);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            videoList.clear();
            videoList.addAll((Collection<? extends Video>) results.values);
            notifyDataSetChanged();
        }
    };


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private TextView videoTitle, isAnswered;
        private ImageView videoThumbnail, newAnswerNotify;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.user_parent);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            isAnswered = itemView.findViewById(R.id.isAnswered);
            videoThumbnail = itemView.findViewById(R.id.thumbNailImage);
            newAnswerNotify = itemView.findViewById(R.id.newAnswerNotification);

        }
    }

    public static void setCountAnswers(List<Integer> countAnswers) {
        VideoAdapter.countAnswers = countAnswers;
    }
}
