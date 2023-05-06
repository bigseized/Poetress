package com.example.poetress.ui.create.CategoryDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.data_sources.CategoryFeedMain;
import com.example.poetress.databinding.DialogCreateCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategoryDialog extends androidx.fragment.app.DialogFragment implements recycler_view_category_adapter.ItemClickListener{

    DialogCreateCategoryBinding binding;
    recycler_view_category_adapter adapter;
    RecyclerView mCategoryView;
    Button categoryButton;
    ArrayList<String> Ganers;
    CategoryFeedMain categoryFeedMain;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCreateCategoryBinding.inflate(inflater, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryFeedMain = new CategoryFeedMain();
        Ganers = new ArrayList<>();
        Ganers = categoryFeedMain.getCategory();
        mCategoryView = binding.categoryRecycler;
        mCategoryView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCategoryView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new recycler_view_category_adapter(getActivity(), Ganers);
        adapter.setClickListener(this);
        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });
        Log.d("my", "onCreate: Adapter Set");
        mCategoryView.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        categoryButton = getActivity().findViewById(R.id.categoryButton);
        categoryButton.setText(Ganers.get(position));
        dismiss();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCategoryView.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
    }
}
