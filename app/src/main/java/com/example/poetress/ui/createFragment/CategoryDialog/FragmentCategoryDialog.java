package com.example.poetress.ui.createFragment.CategoryDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.databinding.DialogCreateCategoryBinding;

import java.util.ArrayList;

public class FragmentCategoryDialog extends androidx.fragment.app.DialogFragment implements recycler_view_category_adapter.ItemClickListener{

    DialogCreateCategoryBinding binding;
    recycler_view_category_adapter adapter;
    RecyclerView mCategoryView;
    Button categoryButton;
    ArrayList<String> Ganers;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCreateCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Ganers = new ArrayList<>();
        Ganers.add("Лирика");
        Ganers.add("Гражданская лирикаaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Военная поэзия");
        Ganers.add("Проза");
        Ganers.add("Хоку");
        mCategoryView = binding.categoryRecycler;
        mCategoryView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new recycler_view_category_adapter(getActivity(), Ganers);
        adapter.setClickListener(this);
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
