package com.example.poetress.view_model.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.view_model.profile.ProfileMainViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileVersesAdapter extends FirestoreRecyclerAdapter<ProfileVerse, ProfileVersesAdapter.ProfileViewHolder> {
    List<String> documentId;
    List<ProfileVerse> Verses;
    ProfileMainViewModel mViewModel;
    OnItemClickListener listener;
    List<AdditionVerseInfo> list;
    Uri uri;

    public ProfileVersesAdapter(@NonNull FirestoreRecyclerOptions<ProfileVerse> options, ProfileMainViewModel mViewModel) {
        super(options);
        documentId = new ArrayList<>();
        Verses = new ArrayList<>();
        this.mViewModel = mViewModel;
    }


    public ProfileVerse getVerses(int position) {
        return Verses.get(position);
    }

    public String getDocumentId(int position) {
        return documentId.get(position);
    }

    public void setListAdd(List<AdditionVerseInfo> addList){
        list = addList;
        notifyItemRangeChanged(0,list.size(), list);
        notifyDataSetChanged();
    }

    public void deleteDocument(int position){
        documentId.remove(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int clickedViewId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_profile_verse, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull ProfileVerse verse) {
        if (list != null) {
            Verses.add(verse);
            documentId.add(getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition()).getId());

//            holder.likes_counter.setText(list.get(position).getNumOfLikes().toString());
//            holder.comments_counter.setText(list.get(position).getNumOfComment().toString());
//            holder.like.setChecked(list.get(position).getLiked());

            holder.name.setText(verse.getAuthor());
            holder.title.setText(verse.getName_Verse());
            holder.text.setText(verse.getText_Verse().replaceAll("\\\\n", "\n"));
            Picasso.get().load(uri).into(holder.image);
        }

    }
    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView name, title, text, ganre, likes_counter,comments_counter;
        public CheckBox like;
        public ImageView image,delete;
        public ConstraintLayout constraintItem;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            ganre = itemView.findViewById(R.id.text);
            name = itemView.findViewById(R.id.text1);
            title = itemView.findViewById(R.id.text2);
            text = itemView.findViewById(R.id.text3);
            image = itemView.findViewById(R.id.imageView1);
            constraintItem = itemView.findViewById(R.id.constraintItem);
//            like = itemView.findViewById(R.id.like);
//            likes_counter = itemView.findViewById(R.id.likes_counter);
//            comments_counter = itemView.findViewById(R.id.comments_counter);


            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onItemClick(getAbsoluteAdapterPosition(), R.id.text3);
                    }catch (Exception e){
                        Log.d("my", "onClick: "+ e);
                    }
                }
            });
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onItemClick(getAbsoluteAdapterPosition(), R.id.delete);
                    }catch (Exception e){
                        Log.d("my", "onClick: "+ e);
                    }
                }
            });

//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(getAbsoluteAdapterPosition(), R.id.like);
//                }
//            });

        }
    }
}
