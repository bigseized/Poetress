package com.example.poetress.ui.feed.search.search_authors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.poetress.databinding.FeedSearchFragmentAuthorsBinding;
import com.example.poetress.ui.profile.Settings.SettingsFragment;
import com.example.poetress.view_model.SearchAuthorsViewModel;

public class SearchAuthorsFragment extends Fragment {
    FeedSearchFragmentAuthorsBinding binding;
    SearchAuthorsViewModel mViewModel;
    Button verses;
    ImageView back_btn;
    AlertDialog.Builder builder;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FeedSearchFragmentAuthorsBinding.inflate(inflater, container, false);
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Подтверждение действия");
        builder.setMessage("Раздел в разработке!");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
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
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view1 = requireActivity().getCurrentFocus();
            if (view1 != null) {
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
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
