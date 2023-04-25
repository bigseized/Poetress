package com.example.poetress.ui.profile.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public TextView name, title, text, ganre;
    public ImageView image;
    public ConstraintLayout constraintItem;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mClickListener.onItemClick(v, getAbsoluteAdapterPosition());
                }catch (Exception e){
                    Log.d("my", "onClick: "+ e);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
        ganre = itemView.findViewById(R.id.text);
        name = itemView.findViewById(R.id.text1);
        title = itemView.findViewById(R.id.text2);
        text = itemView.findViewById(R.id.text3);
        image = itemView.findViewById(R.id.imageView1);
        constraintItem = itemView.findViewById(R.id.constraintItem);
    }
    private ClickListener mClickListener;
    public interface ClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ClickListener clickListener){
        Log.d("my", "setOnClickListener: Try init");
        mClickListener = clickListener;
    }
}
