package com.example.poetress.ui.some1_profile;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.databinding.ProfileAnotherPersonBinding;
import com.example.poetress.ui.ImageFragment;
import com.example.poetress.view_model.SomeOneProfileViewModel;
import com.example.poetress.view_model.versesRecyclerAdapter;
import com.squareup.picasso.Picasso;

public class SomeOneProfile extends Fragment {

    private SomeOneProfileViewModel mViewModel;
    ProfileAnotherPersonBinding binding;
    String Name, Surname;
    Bitmap image;
    versesRecyclerAdapter adapter;
    RecyclerView versesRecycler;
    String UserID;
    Uri uri;


    public static com.example.poetress.ui.some1_profile.SomeOneProfile newInstance() {return new com.example.poetress.ui.some1_profile.SomeOneProfile();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserID = getArguments().getString("userId");
        mViewModel = new ViewModelProvider(this).get(SomeOneProfileViewModel.class);
        mViewModel.setUserIdRepository(UserID);
        mViewModel.setParams(UserID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProfileAnotherPersonBinding.inflate(inflater, container, false);
        mViewModel.loadData();
        mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (!data.getImage_Profile().isEmpty()){
                Picasso.get().load(Uri.parse(data.getImage_Profile())).into(binding.imageView);
                class ImageSet extends Thread {
                    private final Object lock;

                    public ImageSet(Object lock) {
                        this.lock = lock;
                    }

                    @Override
                    public void run() {
                        try {
                            image = Picasso.get().load(Uri.parse(data.getImage_Profile())).get();
                            uri = Uri.parse(data.getImage_Profile());
                            adapter.setImage(image);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            // Обработка ошибки
                        } finally {
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    }
                }
                Object lock = new Object();
                ImageSet imageSet = new ImageSet(lock);
                imageSet.start();

                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Name = data.getName();
            Surname = data.getSurname();
            binding.name.setText(data.getName() + " " + data.getSurname());
            binding.text3.setText("Интересы: " + data.getInterests());
        });

        binding.backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.imageView.setOnClickListener(v -> {
            ImageFragment dialogFragment = new ImageFragment(uri);
            dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
            dialogFragment.show(getActivity().getSupportFragmentManager(), "image");
        });

        binding.add.setOnClickListener(v -> {
            try {
                mViewModel.addFriend(UserID, Name,Surname);
            }catch (Exception e){}

            Toast.makeText(getActivity(),"Предложение отправленно", Toast.LENGTH_SHORT).show();
        });

        versesRecycler = binding.anotherProfileRecycler;
        versesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        versesRecycler.setItemAnimator(null);
        versesRecycler.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        versesRecycler.setNestedScrollingEnabled(false);
        versesRecycler.setHasFixedSize(false);
        adapter = mViewModel.getAdapter();
        versesRecycler.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SomeOneProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();

    }

    @Override
    public void onPause() {
        super.onPause();
        versesRecycler.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
        adapter.stopListening();
    }

}
