package com.example.poetress.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.types.SimpleUserData;
import com.example.poetress.databinding.ChatSelectNewBinding;
import com.example.poetress.view_model.ChatMainRecyclerAdapter;
import com.example.poetress.view_model.ChatSelectNewRecyclerAdapter;
import com.example.poetress.view_model.ChatSelectNewViewModel;

import java.util.List;

public class FragmentChatSelectNew extends Fragment {
    private ChatSelectNewViewModel mViewModel;
    ChatSelectNewBinding binding;
    RecyclerView recyclerView;
    ChatSelectNewRecyclerAdapter adapter;

    public static chat_main newInstance() {
        return new chat_main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChatSelectNewBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatSelectNewViewModel.class);
        binding.backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        adapter = new ChatSelectNewRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChatSelectNewRecyclerAdapter.ChatSelectNewRecyclerViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        createNewChat(mViewModel.getData(position));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NavHostFragment.findNavController(getParentFragment()).popBackStack();
                            }
                        });
                    }
                };
                new Thread(runnable).start();
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });

        mViewModel.getFriends().observe(getViewLifecycleOwner(), new Observer<List<SimpleUserData>>() {
            @Override
            public void onChanged(List<SimpleUserData> userData) {
                adapter.setData(userData);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public  void createNewChat(SimpleUserData simpleUserData){
        mViewModel.createNewChat(simpleUserData);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
