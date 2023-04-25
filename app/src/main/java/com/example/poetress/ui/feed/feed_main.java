package com.example.poetress.ui.feed;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.poetress.R;
import com.example.poetress.databinding.FragmentFeedMainBinding;
import com.example.poetress.ui.create.CategoryDialog.recycler_view_category_adapter;
import com.example.poetress.view_model.feed.FeedMainViewModel;
import com.example.poetress.view_model.feed.recycler_view_category_widgets_adapter;

import java.util.ArrayList;

public class feed_main extends Fragment implements recycler_view_category_widgets_adapter.ItemClickListener{

    private FeedMainViewModel mViewModel;
    FragmentFeedMainBinding binding;
    AppCompatImageView search;
    RecyclerView mCategoryView;
    recycler_view_category_widgets_adapter adapter;

    public static feed_main newInstance() {
        return new feed_main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFeedMainBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FeedMainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        mCategoryView = binding.feedCategoryRecycler;
        mCategoryView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        adapter = new recycler_view_category_widgets_adapter(getActivity(), mViewModel.getCategoryData());
        adapter.setClickListener(this);
        Log.d("my", "onCreate: Adapter Set");
        mCategoryView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = binding.searchButton;
        search.setOnClickListener(v -> {
            Fragment navHostFragment  = getParentFragment().getParentFragment();
            NavHostFragment.findNavController(navHostFragment).navigate(R.id.action_mainFragment_to_SearchFragment);
        });
    }
}
