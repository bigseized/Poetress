package com.example.poetress.ui.feed.search.search_authors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.FeedSearchFragmentAuthorsBinding;
import com.example.poetress.ui.profile.Settings.SettingsFragment;
import com.example.poetress.view_model.SearchAuthorsViewModel;

public class SearchAuthorsFragment extends Fragment {
    FeedSearchFragmentAuthorsBinding binding;
    SearchAuthorsViewModel mViewModel;
    Button verses;
    ImageView back_btn;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FeedSearchFragmentAuthorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        verses = binding.verses;
        back_btn = binding.backButton;
        verses.setOnClickListener(v->{
            NavHostFragment.findNavController(this).navigate(R.id.action_searchAuthor_to_searchVerses);
        });
        back_btn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_searchAutors_to_main);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         mViewModel = new ViewModelProvider(this).get(SearchAuthorsViewModel.class);
        // TODO: Use the ViewModel
    }

}
