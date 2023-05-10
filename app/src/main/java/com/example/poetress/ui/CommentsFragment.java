package com.example.poetress.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.Comment;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.databinding.CommentsFragmentBinding;
import com.example.poetress.databinding.SimpleImageFragmentBinding;
import com.example.poetress.view_model.ChatMainViewModel;
import com.example.poetress.view_model.CommentsAdapter;
import com.example.poetress.view_model.CommentsViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentsFragment extends Fragment {
    CommentsFragmentBinding binding;
    Uri uri;
    String User_Id, Verse_Id, commentText;
    ImageView photoImageView;
    EditText text;
    CommentsAdapter adapter;
    RecyclerView commentsRecycler;
    List<Comment> comments = new ArrayList<>();
    CommentsViewModel mViewModel;
    AppCompatImageView send;
    ImageView backButton;

    @Override
    public void onStart() {
        super.onStart();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CommentsFragmentBinding.inflate(inflater, container, false);
        send = binding.sendCommentButton;
        text = binding.sendComment;
        backButton = binding.backButton;
        User_Id = getArguments().getString("User_Id");
        Verse_Id = getArguments().getString("Verse_Id");
        mViewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
        mViewModel.loadComments(User_Id,Verse_Id);

        adapter = new CommentsAdapter(mViewModel);

        commentsRecycler = binding.commentsRecycler;
        commentsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRecycler.setAdapter(adapter);

        mViewModel.getCommentsLD().observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> additionVerseInfo) {
                adapter.addItems(additionVerseInfo);
                adapter.notifyDataSetChanged();
                comments.addAll(additionVerseInfo);
            }
        });

        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        mViewModel.getIsSend().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    mViewModel.loadComments(User_Id, Verse_Id);
                    adapter.notifyItemRangeChanged(0, comments.size());
                    text.setText("");
                }else{
                    Toast.makeText(getActivity(),"Ошибка",Toast.LENGTH_SHORT).show();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentText = text.getText().toString();
                if (!commentText.equals("")){
                    mViewModel.newComment(User_Id, Verse_Id, commentText);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
