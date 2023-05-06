package com.example.poetress.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poetress.R;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.databinding.FragmentProfileMainBinding;
import com.example.poetress.ui.ImageFragment;
import com.example.poetress.ui.create.CategoryDialog.FragmentCategoryDialog;
import com.example.poetress.ui.profile.RecyclerView.ProfileViewHolder;
import com.example.poetress.view_model.ProfileMainViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class profile_main extends Fragment {

    private ProfileMainViewModel mViewModel;
    FirestoreRecyclerAdapter adapter;
    TextView bd_title,bd_text;
    FirebaseAuth firebaseAuth;
    String User_UID;
    List<String> documentId;
    AlertDialog.Builder builder;
    View view;
    Uri uri;
    ImageView settings;
    int adapterPosition;
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

        documentId = new ArrayList<>();
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Подтверждение действия");
        builder.setMessage("Вы уверены, что хотите удалить произведение?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity(), documentId + "  " + adapterPosition, Toast.LENGTH_SHORT).show();
                deleteVerse(documentId.get(adapterPosition));
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false);
        settings = binding.settings;
        progressBarVerse = binding.progressBarVerses;
        mFirestorelist = binding.profileRecycler;
        progressBarVerse.setVisibility(View.VISIBLE);
        mFirestorelist.setVisibility(View.INVISIBLE);


        mViewModel.loadData();
        mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (!data.getImage_Profile().isEmpty()){
                Picasso.get().load(Uri.parse(data.getImage_Profile())).into(binding.imageView);
                uri = Uri.parse(data.getImage_Profile());
            }
            binding.text.setText(data.getName() + " " + data.getSurname());
            binding.text3.setText("Интересы: " + data.getInterests());
        });

        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mFirestorelist.setItemAnimator(null);
        mFirestorelist.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mFirestorelist.setNestedScrollingEnabled(false);

        User_UID = firebaseAuth.getCurrentUser().getUid();
        Query query = firebaseFirestore.collection("User_Data").document(User_UID).collection("User_Verses").orderBy("Date_Verse", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()){
                progressBarVerse.setVisibility(View.INVISIBLE);
                mFirestorelist.setVisibility(View.VISIBLE);
            }
        });

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
                documentId.add(getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition()).getId());
                holder.name.setText(verse.getAuthor());
                holder.title.setText(verse.getName_Verse());
                holder.text.setText(verse.getText_Verse().replaceAll("\\\\n", "\n"));
                mViewModel.loadData();
                mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
                    if (!data.getImage_Profile().isEmpty()){
                        Picasso.get().load(Uri.parse(data.getImage_Profile())).into(holder.image);
                    }
                    progressBarVerse.setVisibility(View.INVISIBLE);
                    mFirestorelist.setVisibility(View.VISIBLE);
                });
                holder.setOnClickListener(new ProfileViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
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
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = builder.create();
                        adapterPosition = holder.getAbsoluteAdapterPosition();
                        dialog.show();
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
        binding.imageView.setOnClickListener(v -> {
            ImageFragment dialogFragment = new ImageFragment(uri);
            dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
            dialogFragment.show(getActivity().getSupportFragmentManager(), "image");
        });
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

    private void deleteVerse(String ID_Verse){
        mViewModel.deleteVerse(ID_Verse, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                adapter.notifyDataSetChanged();
                documentId.remove(adapterPosition);
                //adapter.notifyItemRemoved(adapterPosition);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
