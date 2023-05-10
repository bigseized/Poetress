package com.example.poetress.view_model;


import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.repositories.CommentsDataInteraction;
import com.example.poetress.data.repositories.FeedVersesGetData;
import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.Comment;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.data.types.RawVerse;
import com.example.poetress.view_model.feed.FeedMainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private List<Comment> comments = new ArrayList<>();
    private List<String> userId = new ArrayList<>();
    private com.example.poetress.view_model.feed.UserVersesAdapter.OnItemClickListener listener;
    CommentsViewModel viewModel;

    public interface OnItemClickListener {
        void onItemClick(int position, int clickedViewId, RawVerse info, String userId);
    }

    public CommentsAdapter(CommentsViewModel viewModel) {
        this.viewModel = viewModel;
    }


    public void addItems(List<Comment> newVerses) {

        comments = newVerses;
    }


    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_recycler_item, parent, false);
        return new CommentsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setOnItemClickListener(com.example.poetress.view_model.feed.UserVersesAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    public class CommentsViewHolder extends RecyclerView.ViewHolder{

        private TextView commentText;
        private ImageView userIm;
        private ConstraintLayout constraintLayout;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.commentText);
            userIm = itemView.findViewById(R.id.imageComment);
            constraintLayout = itemView.findViewById(R.id.constraintComments);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onItemClick(getAdapterPosition(), R.id.constraintLayout,null, userId.get(getAdapterPosition()));
//                }
//            });

        }

        public void bind(Comment comment) {

            commentText.setText(comment.getText());

            try {
                Picasso.get().load(Uri.parse(comment.getUri())).into(userIm);
            }
            catch (NullPointerException e){
                userIm.setImageResource(R.drawable.profile_avatar_view);
            }
        }

    }
}
