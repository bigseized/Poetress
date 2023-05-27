package com.example.poetress.ui;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.SimpleImageFragmentBinding;
import com.squareup.picasso.Picasso;

public class ImageFragment extends DialogFragment {

    SimpleImageFragmentBinding binding;
    Uri uri;
    ImageView photoImageView;

    public ImageFragment(Uri uri){
        this.uri = uri;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SimpleImageFragmentBinding.inflate(inflater, container, false);
        Picasso.get().load(uri).into(binding.imageView);
        photoImageView = binding.imageView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().setOnClickListener(v -> {
            dismiss();
        });
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
