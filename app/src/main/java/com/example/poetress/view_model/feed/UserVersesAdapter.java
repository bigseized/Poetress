package com.example.poetress.view_model.feed;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.repositories.FeedVersesGetData;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.data.types.RawVerse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserVersesAdapter extends RecyclerView.Adapter<UserVersesAdapter.UserVerseViewHolder> {

    private List<ProfileVerse> userVerses = new ArrayList<>();
    private List<String> userId = new ArrayList<>();
    private OnItemClickListener listener;
    FeedVersesGetData repository;
    FeedMainViewModel viewModel;

    public interface OnItemClickListener {
        void onItemClick(int position, int clickedViewId, RawVerse info, String userId);
    }

    public UserVersesAdapter(FeedMainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setUserVerses(List<ProfileVerse> userVerses) {
        this.userVerses = userVerses;
        notifyDataSetChanged();
    }

    public void clear() {
        userVerses.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<ProfileVerse> newVerses) {
        int startPos = userVerses.size();
        userVerses.addAll(newVerses);
        notifyItemRangeInserted(startPos, newVerses.size());
    }

    @NonNull
    @Override
    public UserVerseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_recycler_post_item, parent, false);
        return new UserVerseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserVerseViewHolder holder, int position) {
        ProfileVerse userVerse = userVerses.get(position);
        holder.bind(userVerse);
    }

    @Override
    public int getItemCount() {
        return userVerses.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class UserVerseViewHolder extends RecyclerView.ViewHolder{

        private TextView author, date, title, text;
        private ImageView userIm;
        private ConstraintLayout constraintLayout;
        MutableLiveData<Integer> clickData = new MutableLiveData<>();

        public UserVerseViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.text1);
            date = itemView.findViewById(R.id.dateText);
            title = itemView.findViewById(R.id.text2);
            text = itemView.findViewById(R.id.text3);
            userIm = itemView.findViewById(R.id.imageView1);
            constraintLayout = itemView.findViewById(R.id.author);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        userId = viewModel.getUserIds();
                        try {
                            listener.onItemClick(getAdapterPosition(), R.id.author,null, userId.get(getAdapterPosition()));
                        }
                        catch (Exception e){

                        }
                    }
                }
            });
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition(), R.id.text3,new RawVerse(userVerses.get(getAdapterPosition()).getName_Verse(), userVerses.get(getAdapterPosition()).getText_Verse()), null);
                    }
                }
            });
        }

        public LiveData<Integer> getClickLiveData() {
            return clickData;
        }

        public void bind(ProfileVerse userVerse) {
            author.setText(userVerse.getAuthor());
            date.setText(userVerse.getDate_Verse().toString());
            title.setText(userVerse.getName_Verse());
            text.setText(userVerse.getText_Verse());
            try {
                Picasso.get().load(Uri.parse(userVerse.getUri())).into(userIm);
            }
            catch (NullPointerException e){
                userIm.setImageResource(R.drawable.profile_avatar_view);
                }
            }
        }
    }