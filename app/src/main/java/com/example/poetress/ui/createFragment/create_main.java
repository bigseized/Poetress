package com.example.poetress.ui.createFragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poetress.databinding.FragmentCreateMainBinding;
import com.example.poetress.ui.createFragment.CategoryDialog.FragmentCategoryDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class create_main extends Fragment {

    private CreateMainViewModel mViewModel;
    FragmentCreateMainBinding binding;
    CheckBox checkBox;
    ImageView create;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView title, verse, category;
    TextView text1;

    //public static create_main newInstance() {
        //return new create_main();
    //}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateMainBinding.inflate(inflater);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkBox = binding.checkbox;
        text1 = binding.text;
        create = binding.post;
        title = binding.title;
        verse = binding.verse;
        category = binding.categoryButton;

        SharedPreferences prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        if (prefs.contains("text")){
            text1.setText(prefs.getString("text", ""));
        }
        checkBox.setOnClickListener(v -> {

            if ( ((CheckBox)v).isChecked() ) {
                text1.setText("Публично");
            }
            else{
                text1.setText("Анонимно");
            }

        });
        category.setOnClickListener(v -> {
            FragmentCategoryDialog dialogFragment = new FragmentCategoryDialog();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "category");
        });
        create.setOnClickListener(v -> {
            if (!title.getText().toString().equals("") && !text1.getText().toString().equals("") ) {
                createVerse();
                Toast.makeText(getActivity().getApplicationContext(), "Успешно", Toast.LENGTH_SHORT).show();
                title.setText("");
                verse.setText("");
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreateMainViewModel.class);
        // TODO: Use the ViewModel
    }
    public void createVerse() throws NullPointerException{
        Map<String, Object> data = new HashMap<>();
        data.put("Author", "Миша Беляев");
        data.put("Date_Verse", com.google.firebase.firestore.FieldValue.serverTimestamp());
        data.put("Ganre_Verse", category.getText().toString());
        data.put("Name_Verse",title.getText().toString());
        data.put("Text_Verse", verse.getText().toString());
        String User_UID = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("User_Data").document(User_UID).collection("User_Verses").add(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor prefEditor = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
        prefEditor.putString("text", text1.getText().toString());
        prefEditor.commit();
    }
}