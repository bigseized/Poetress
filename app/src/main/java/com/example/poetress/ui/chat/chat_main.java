package com.example.poetress.ui.chat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.poetress.R;
import com.example.poetress.data.types.SimpleUserData;
import com.example.poetress.databinding.FragmentChatMainBinding;
import com.example.poetress.view_model.adapters.ChatMainRecyclerAdapter;
import com.example.poetress.view_model.chat.ChatMainViewModel;

import java.util.List;

public class chat_main extends Fragment {

    private ChatMainViewModel mViewModel;
    FragmentChatMainBinding binding;
    RecyclerView recyclerView;
    ChatMainRecyclerAdapter adapter;

    public static chat_main newInstance() {
        return new chat_main();
    }
    private final NavController.OnDestinationChangedListener onDestinationChangedListener =
            (controller, destination, arguments) -> {
                if(destination.getId() == R.id.chat_main) {
                    mViewModel.getChats().observe(getViewLifecycleOwner(), new Observer<List<SimpleUserData>>() {
                        @Override
                        public void onChanged(List<SimpleUserData> userData) {
                            //refresh
                            adapter.setData(userData);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatMainBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatMainViewModel.class);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        adapter = new ChatMainRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChatMainRecyclerAdapter.ChatMainRecyclerViewHolder.ClickListener()
        {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                SimpleUserData data = mViewModel.getData(position);
                bundle.putString("ID",data.getID());
                bundle.putString("Name",data.getName());
                bundle.putString("Surname",data.getSurname());
                bundle.putString("Image",data.getImage_Profile());
                NavHostFragment.findNavController(getParentFragment().getParentFragment()).navigate(R.id.action_mainFragment_to_ChatMainDialog, bundle);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Bundle bundleID = new Bundle();
                SimpleUserData data = mViewModel.getData(position);
                bundleID.putString("userId",data.getID());
                NavHostFragment.findNavController(getParentFragment().getParentFragment()).navigate(R.id.action_mainFragment_to_SomeOneProfile, bundleID);

            }
        });



        binding.button.setOnClickListener(v -> {
            NavHostFragment.findNavController(getParentFragment().getParentFragment())
                    .navigate(R.id.action_mainFragment_to_ChatSelectNew);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatMainViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.refreshData();
        NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(onDestinationChangedListener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NavController navController = NavHostFragment.findNavController(this);
        navController.removeOnDestinationChangedListener(onDestinationChangedListener);
        binding = null;
    }
}
