package com.example.poetress.view_model.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.types.SimpleUserData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatSelectNewRecyclerAdapter extends RecyclerView.Adapter<ChatSelectNewRecyclerAdapter.ChatSelectNewRecyclerViewHolder> {
    List<SimpleUserData> model;
    private static ChatSelectNewRecyclerViewHolder.ClickListener clickListener;
    public ChatSelectNewRecyclerAdapter(){}

    public void setData(List<SimpleUserData> userData){
        this.model = userData;
    }



    @NonNull
    @Override
    public ChatSelectNewRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_new_chat_recycler_item, parent, false);
        return new ChatSelectNewRecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChatSelectNewRecyclerViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(model.get(position).getImage_Profile())).into(holder.image);
        holder.nameSurname.setText(model.get(position).getName() + model.get(position).getSurname());
        holder.setOnClickListener(new ChatSelectNewRecyclerViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("my", "onItemClick: clicked");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // Handle item long click
            }
        });
    }

    @Override
    public int getItemCount() {
        return model == null ? 0 : model.size();
    }

    public void setOnItemClickListener(ChatSelectNewRecyclerViewHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public static class ChatSelectNewRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSurname;
        public ImageView image;

        public ChatSelectNewRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        clickListener.onItemClick(v, getAdapterPosition());
                    } catch (Exception e) {
                        Log.d("my", "onClick: " + e);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        clickListener.onItemLongClick(v, getAdapterPosition());
                        return true;
                    } catch (Exception e) {
                        Log.d("my", "onLongClick: " + e);
                    }
                    return false;
                }
            });
            nameSurname = itemView.findViewById(R.id.text1);
            image = itemView.findViewById(R.id.imageView1);
        }

        private ClickListener mClickListener;
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(ClickListener clickListener){
            Log.d("my", "setOnClickListener: Try init");
            this.mClickListener = clickListener;
        }
    }

}
