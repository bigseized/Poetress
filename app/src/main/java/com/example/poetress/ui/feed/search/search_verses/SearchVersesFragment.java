package com.example.poetress.ui.feed.search.search_verses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.FeedSearchFragmentVersesBinding;
import com.example.poetress.ui.profile.Settings.SettingsFragment;
import com.example.poetress.view_model.feed.SearchVersesViewModel;

public class SearchVersesFragment extends Fragment {
    FeedSearchFragmentVersesBinding binding;
    SearchVersesViewModel mViewModel;
    Button authors;
    ImageView back_btn;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FeedSearchFragmentVersesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        authors = binding.authors;
        back_btn = binding.backButton;
        authors.setOnClickListener(v->{
            NavHostFragment.findNavController(this).navigate(R.id.action_searchVerses_to_searchAuthors);
        });
        back_btn.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view1 = requireActivity().getCurrentFocus();
            if (view1 != null) {
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
            NavHostFragment.findNavController(this).navigate(R.id.action_searchVerses_to_main);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchVersesViewModel.class);
        // TODO: Use the ViewModel
    }

}
