package com.example.poetress.ui.feed;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poetress.MainFragment;
import com.example.poetress.R;
import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.data.types.RawVerse;
import com.example.poetress.databinding.FragmentFeedMainBinding;
import com.example.poetress.ui.create.CategoryDialog.recycler_view_category_adapter;
import com.example.poetress.view_model.feed.FeedMainViewModel;
import com.example.poetress.view_model.feed.UserVersesAdapter;
import com.example.poetress.view_model.feed.recycler_view_category_widgets_adapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class feed_main extends Fragment implements recycler_view_category_widgets_adapter.ItemClickListener{

    private FeedMainViewModel mViewModel;
    FragmentFeedMainBinding binding;
    AppCompatImageView search;
    ProgressBar progressBar;
    RecyclerView mCategoryView, recyclerView;
    recycler_view_category_widgets_adapter adapter;
    UserVersesAdapter adapterPost;
    List<ProfileVerse> profileVerseList;
    List<AdditionVerseInfo> additionVerseInfoList;

    public static feed_main newInstance() {
        return new feed_main();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFeedMainBinding.inflate(inflater,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerView = binding.feedPostsRecycler;
        progressBar = binding.progressBar;
        additionVerseInfoList = new ArrayList<>();



        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);


        mViewModel = new ViewModelProvider(this).get(FeedMainViewModel.class);

        adapterPost = new UserVersesAdapter(mViewModel);

        recyclerView.setAdapter(adapterPost);
        recyclerView.getRecycledViewPool().clear();
        adapterPost.clear();
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mViewModel.getUserVersesLiveData().observe(getViewLifecycleOwner(), new Observer<List<ProfileVerse>>() {

            @Override
            public void onChanged(List<ProfileVerse> profileVerses) {
                adapterPost.addItems(profileVerses);
                profileVerseList = profileVerses;
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.getAdditionVersesInfoData().observe(getViewLifecycleOwner(), new Observer<List<AdditionVerseInfo>>() {
            @Override
            public void onChanged(List<AdditionVerseInfo> additionVerseInfo) {
                adapterPost.addAdd(additionVerseInfo);
                additionVerseInfoList.addAll(additionVerseInfo);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!mViewModel.isLoading() && !mViewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 20) {
                        mViewModel.loadUserVerses();
                    }
                }
            }
        });

        adapterPost.setOnItemClickListener(new UserVersesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int clickedViewId, RawVerse info, String UserId) {
                if (clickedViewId == R.id.text3){
                    try{
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
                        bottomSheetDialog.setContentView(R.layout.bottom_dialog_profile_verse);
                        TextView bd_title = bottomSheetDialog.findViewById(R.id.bd_Title);
                        TextView bd_text = bottomSheetDialog.findViewById(R.id.bd_Text);
                        bd_title.setText(info.getName_Verse());
                        bd_text.setText(info.getText_Verse());
                        bottomSheetDialog.show();
                    }
                    catch (Exception e){
                        Log.wtf("GLOBAL ERRORS", "Failed to set BottomSheetDialog");
                    }
                }
                else if(clickedViewId == R.id.author) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", mViewModel.getUserIds().get(position));
                    NavHostFragment.findNavController(getParentFragment().getParentFragment()).navigate(R.id.action_mainFragment_to_SomeOneProfile, bundle);
                }
                else if(clickedViewId == R.id.like){
                    LiveData<Boolean> resultLiveData = mViewModel.updateLike(mViewModel.getVerseIds().get(position),UserId);
                    resultLiveData.observe(getViewLifecycleOwner(), containsCurrentUser -> {
                        MutableLiveData<List<AdditionVerseInfo>> likeInfoLiveData = mViewModel.getAdditionVersesInfoData();
                        AdditionVerseInfo newAddInfo =  additionVerseInfoList.get(position);
                        if (containsCurrentUser != null && containsCurrentUser) {
                            // Документ существует и содержит идентификатор текущего пользователя
                            newAddInfo.setLiked(true);
                            newAddInfo.setNumOfLikes(newAddInfo.getNumOfLikes() + 1);
                        } else {
                            // Документ не существует или не содержит идентификатор текущего пользователя
                            newAddInfo.setLiked(false);
                            newAddInfo.setNumOfLikes(newAddInfo.getNumOfLikes() - 1);
                        }
                        adapterPost.notifyItemChanged(position, newAddInfo);
                    });
                }
                else if(clickedViewId == R.id.comment){
                    Bundle bundle = new Bundle();
                    bundle.putString("User_Id",UserId);
                    bundle.putString("Verse_Id", mViewModel.getVerseIds().get(position));
                    NavHostFragment.findNavController(getParentFragment().getParentFragment())
                            .navigate(R.id.action_mainFragment_to_CommentsFargment, bundle);
                }
            }
        });

        mViewModel.refreshUserVerses();

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

    @Override
    public void onStop() {
        recyclerView.getRecycledViewPool().clear();
        mViewModel.clearData();
        super.onStop();
        adapterPost.clear();
    }

}
