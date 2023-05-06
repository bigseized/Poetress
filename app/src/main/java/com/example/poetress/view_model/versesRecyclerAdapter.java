package com.example.poetress.view_model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.poetress.R;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.ui.profile.RecyclerView.ProfileViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

public class versesRecyclerAdapter extends FirestoreRecyclerAdapter<ProfileVerse, versesRecyclerItemViewHolder>{
    Bitmap image;
    //adapter = new FirestoreRecyclerAdapter<ProfileVerse, ProfileViewHolder>(options) {
    public versesRecyclerAdapter(FirestoreRecyclerOptions<ProfileVerse> options){
        super(options);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @NonNull
        @Override
        public versesRecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_another_profile_verse, parent, false);
            return new versesRecyclerItemViewHolder(view);
        }


        @Override
        protected void onBindViewHolder(@NonNull versesRecyclerItemViewHolder holder, int position, @NonNull ProfileVerse verse) {
            //holder.ganre.setText(verse.getGenre_Verse());
            holder.name.setText(verse.getAuthor());
            holder.title.setText(verse.getName_Verse());
            holder.text.setText(verse.getText_Verse().replaceAll("\\\\n", "\n"));
            holder.image.setImageBitmap(this.image);
            holder.setOnClickListener(new versesRecyclerItemViewHolder.ClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext(),R.style.AppBottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(R.layout.bottom_dialog_profile_verse);
                    TextView bd_title = bottomSheetDialog.findViewById(R.id.bd_Title);
                    TextView bd_text = bottomSheetDialog.findViewById(R.id.bd_Text);
                    bd_title.setText(verse.getName_Verse());
                    bd_text.setText(verse.getText_Verse());
                    bottomSheetDialog.show();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            Log.d("my", "onBindViewHolder: successful added ");
        }
    };
