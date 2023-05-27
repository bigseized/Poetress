package com.example.poetress.view_model.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.data.types.Message;
import com.example.poetress.databinding.ItemRecyclerDialogMessageReceivedBinding;
import com.example.poetress.databinding.ItemRecyclerDialogMessageSentBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatMainDialogRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVED = 0;

    private List<Message> messages;
    String Image;
    String senderID;

    public void setImage(String Uri){
        this.Image = Uri;
    }

    public ChatMainDialogRecyclerAdapter(List<Message> messages, String senderID, String Image) {
        this.messages = messages;
        this.senderID = senderID;
        this.Image = Image;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND){
            return new DialogSendViewHolder(
                    ItemRecyclerDialogMessageSentBinding.inflate(
                            LayoutInflater.from(parent.getContext()),parent,false
                    )
            );
        }
        else{
            return new DialogReceivedViewHolder(
                    ItemRecyclerDialogMessageReceivedBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEND){
            ((DialogSendViewHolder) holder).setData(messages.get(position));
        }
        else{
            ((DialogReceivedViewHolder) holder).setData(messages.get(position), Image);
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    public int getItemViewType(int position){
        if (messages.get(position).getSenderID().equals(senderID)){
            return TYPE_SEND;
        }else {
            return TYPE_RECEIVED;
        }
    }

    static class DialogSendViewHolder extends RecyclerView.ViewHolder{

        private final ItemRecyclerDialogMessageSentBinding binding;

        public DialogSendViewHolder(@NonNull ItemRecyclerDialogMessageSentBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setData(Message message){
            binding.sentTextMessage.setText(message.getMessageText());
            try {
                Date date = new Date(message.getDate().getSeconds() * 1000 + message.getDate().getNanoseconds() / 1000000);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                String formattedDate = formatter.format(date);
                binding.textDateTime.setText(formattedDate);
            } catch (Exception e){}
        }
    }

    static class DialogReceivedViewHolder extends RecyclerView.ViewHolder{

        private final ItemRecyclerDialogMessageReceivedBinding binding;

        public DialogReceivedViewHolder(@NonNull ItemRecyclerDialogMessageReceivedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setData(Message message, String image){
            binding.receivedTextMessage.setText(message.getMessageText());
            try {
                Date date = new Date(message.getDate().getSeconds() * 1000 + message.getDate().getNanoseconds() / 1000000);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                String formattedDate = formatter.format(date);
                binding.textDateTime.setText(formattedDate);
            } catch (Exception e){}
            try {
                Picasso.get().load(Uri.parse(image)).into(binding.imageDialog);
            }catch (Exception e){

            }

        }
    }
}
