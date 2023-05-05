package com.example.poetress.ui.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.databinding.FragmentProfileMainBinding;
import com.example.poetress.ui.profile.RecyclerView.ProfileViewHolder;
import com.example.poetress.view_model.ProfileMainViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class profile_main extends Fragment {

    private ProfileMainViewModel mViewModel;
    FirestoreRecyclerAdapter adapter;
    TextView bd_title,bd_text;
    FirebaseAuth firebaseAuth;
    String User_UID;
    View view;
    ImageView settings;
    FragmentProfileMainBinding binding;
    ProgressBar progressBarAva, progressBarVerse;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestorelist;
    Bitmap bitmap;
    Integer imHeight, inWidth;

    public static profile_main newInstance() {
        return new profile_main();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileMainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false);
        settings = binding.settings;

        mViewModel.loadData();
        mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (!data.getImage_Profile().isEmpty()){
                Picasso.get().load(Uri.parse(data.getImage_Profile())).into(binding.imageView);
            }
            binding.text.setText(data.getName() + " " + data.getSurname());
            binding.text3.setText("Интересы: " + data.getInterests());
        });

        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mFirestorelist = binding.profileRecycler;
        mFirestorelist.setItemAnimator(null);
        mFirestorelist.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mFirestorelist.setNestedScrollingEnabled(false);

        User_UID = firebaseAuth.getCurrentUser().getUid();
        Query query = firebaseFirestore.collection("User_Data").document(User_UID).collection("User_Verses").orderBy("Date_Verse", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ProfileVerse> options =
                new FirestoreRecyclerOptions.Builder<ProfileVerse>()
                        .setQuery(query, ProfileVerse.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<ProfileVerse, ProfileViewHolder>(options) {
            @NonNull
            @Override
            public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_profile_verse, parent, false);
                return new ProfileViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull ProfileVerse verse) {
                //holder.ganre.setText(verse.getGenre_Verse());
                holder.name.setText(verse.getAuthor());
                holder.title.setText(verse.getName_Verse());
                holder.text.setText(verse.getText_Verse().replaceAll("\\\\n", "\n"));
                mViewModel.loadData();
                mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
                    if (!data.getImage_Profile().isEmpty()){
                        Picasso.get().load(Uri.parse(data.getImage_Profile())).into(holder.image);
                    }
                });
                holder.setOnClickListener(new ProfileViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                        bottomSheetDialog.setContentView(R.layout.bottom_dialog_profile_verse);
                        bd_title = bottomSheetDialog.findViewById(R.id.bd_Title);
                        bd_text = bottomSheetDialog.findViewById(R.id.bd_Text);
                        bd_title.setText(verse.getName_Verse());
                        bd_text.setText(verse.getText_Verse());
                        bottomSheetDialog.show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                Log.d("my", "onBindViewHolder: successful added ");
            }
        };

        mFirestorelist.setHasFixedSize(false);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFirestorelist.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test_ava);
//        bm = Bitmap.createScaledBitmap(bm, bm.getWidth() / 4, bm.getHeight() / 4, true);
//        binding.imageView.setImageBitmap(bm);

        settings.setOnClickListener(v -> {

            Fragment navHostFragment = getParentFragment().getParentFragment();
            NavHostFragment.findNavController(navHostFragment).navigate(R.id.action_mainFragment_to_SettingsFragment);
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileMainViewModel.class);
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
        mFirestorelist.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
        adapter.stopListening();
    }

}
