package com.example.poetress.ui.chat;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.data.repositories.ChatDialogDataInteraction;
import com.example.poetress.data.types.Message;
import com.example.poetress.data.types.SimpleUserData;
import com.example.poetress.databinding.ChatDialogBinding;
import com.example.poetress.view_model.adapters.ChatMainDialogRecyclerAdapter;
import com.example.poetress.view_model.chat.ChatMainDialogViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatMainDialog extends Fragment {

    ChatDialogBinding binding;
    ChatMainDialogViewModel mViewModel;
    SimpleUserData simpleUserData;
    List<Message> messages;
    String ImageSender;
    RecyclerView recyclerView;
    ChatMainDialogRecyclerAdapter adapter;
    Message message;

    public static chat_main newInstance() {
        return new chat_main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ChatMainDialogViewModel.class);
        binding = ChatDialogBinding.inflate(inflater);
        recyclerView = binding.chatRecycler;
        simpleUserData = new SimpleUserData();

        simpleUserData.setName(getArguments().getString("Name"));
        simpleUserData.setSurname(getArguments().getString("Surname"));
        simpleUserData.setID(getArguments().getString("ID"));
        simpleUserData.setImage_Profile(getArguments().getString("Image"));

        binding.dialogWithName.setText(simpleUserData.getName() + " " + simpleUserData.getSurname());

        binding.send.setOnClickListener(v -> {
            if (!binding.sendTextMessage.getText().toString().equals("")) {
                message = new Message();
                message.setMessageText(binding.sendTextMessage.getText().toString());
                message.setSenderID(mViewModel.getID());
                message.setReceiverId(simpleUserData.getID());
                message.setDate(Timestamp.now()); // will add in repository
                mViewModel.sendMessage(message);
            }
            binding.sendTextMessage.setText("");
        });
        mViewModel.getUri(simpleUserData.getID(), new ChatDialogDataInteraction.OnUriReadyCallback() {
            @Override
            public void onUriReady(Uri uri) {
                ImageSender = uri.toString();
                adapter.setImage(ImageSender);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.e("", "onError: Image didn't uploaded");
            }
        });
        messages = new ArrayList<>();
        adapter = new ChatMainDialogRecyclerAdapter(messages,mViewModel.getID(),ImageSender);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        mViewModel.setListener(eventListener, mViewModel.getID(), simpleUserData.getID());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null)
            return;
        if (value != null){
            int count = messages.size();
            for (DocumentChange documentChange: value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Message message = new Message();
                    message.setSenderID(documentChange.getDocument().getString("SenderId"));
                    message.setReceiverId(documentChange.getDocument().getString("ReceiverId"));
                    message.setMessageText(documentChange.getDocument().getString("MessageText"));
                    message.setDate(documentChange.getDocument().getTimestamp("Date"));
                    if (message.getDate() == null){
                        message.setDate(Timestamp.now());
                    }
                    messages.add(message);
                }
            }
            if (messages != null && messages.size() != 0){
                messages.sort(Comparator.comparing(Message::getDate, Comparator.nullsLast(Comparator.naturalOrder())));
            }
            if (count == 0){
                adapter.notifyDataSetChanged();
            }
            else{
                adapter.notifyItemRangeInserted(messages.size(),messages.size());
                binding.chatRecycler.smoothScrollToPosition(messages.size());
            }
        }
    });


}
