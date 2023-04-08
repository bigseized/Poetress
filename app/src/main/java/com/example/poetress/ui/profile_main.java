package com.example.poetress.ui;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.poetress.R;
import com.example.poetress.databinding.FragmentProfileMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile_main extends Fragment {

    private ProfileMainViewModel mViewModel;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    String User_UID;
    FragmentProfileMainBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestorelist;
    Bitmap bitmap;
    Integer imHeight;
    Integer inWidth;
    public static profile_main newInstance() {
        return new profile_main();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileMainBinding.inflate(inflater,container,false);

        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mFirestorelist = binding.profileRecycler;
        mFirestorelist.setItemAnimator(null);
        mFirestorelist.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mFirestorelist.setNestedScrollingEnabled(false);
        User_UID = firebaseAuth.getCurrentUser().getUid();
        Query query = firebaseFirestore.collection("User_Data").document(User_UID).collection("User_Verses");


        FirestoreRecyclerOptions<ProfileVerse> options =
                new FirestoreRecyclerOptions.Builder<ProfileVerse>()
                        .setQuery(query, ProfileVerse.class)
                        .build();//нихуя не работает

        adapter = new FirestoreRecyclerAdapter<ProfileVerse, ProfileViewHolder>(options) {
            @NonNull
            @Override
            public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_profile_verse, parent,false);
                return new ProfileViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull ProfileVerse verse) {
                //holder.ganre.setText(verse.getGenre_Verse());
                holder.name.setText(verse.getAuthor());
                holder.title.setText(verse.getName_Verse());
                holder.text.setText(verse.getText_Verse().replaceAll("\\\\n","\n"));
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
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test_ava);
        bm = Bitmap.createScaledBitmap(bm,bm.getWidth()/4,bm.getHeight()/4,true);
        binding.imageView.setImageBitmap(bm);
    }
    class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, text, ganre;
        ConstraintLayout constraintItem;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            ganre = itemView.findViewById(R.id.text);
            name = itemView.findViewById(R.id.text1);
            title = itemView.findViewById(R.id.text2);
            text = itemView.findViewById(R.id.text3);
            constraintItem = itemView.findViewById(R.id.constraintItem);
        }
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
