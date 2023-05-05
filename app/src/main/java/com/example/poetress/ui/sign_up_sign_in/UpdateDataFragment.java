package com.example.poetress.ui.sign_up_sign_in;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.UpdateUserDataBinding;
import com.example.poetress.view_model.UpdateDataViewModel;

import org.checkerframework.common.returnsreceiver.qual.This;


public class UpdateDataFragment extends Fragment {
    private UpdateDataViewModel mViewModel;
    private UpdateUserDataBinding binding;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    private MutableLiveData<Boolean> asyncTasksCompleted = new MutableLiveData<>(false);
    EditText name,surname, interests;
    ImageView Image;
    Uri ImageUri;
    final static int PICK_PHOTO_FOR_AVATAR = 1;

    @Override
    public void onStart() {
        super.onStart();

    }

    public static UpdateDataFragment newInstance() {
        return new UpdateDataFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = UpdateUserDataBinding.inflate(inflater);
        progressBar = binding.progressBar;
        progressBar.setIndeterminate(true);
        linearLayout = binding.linearLayout;
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateDataViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        name = binding.setNameUser;
        surname = binding.setSurnameUser;
        Image = binding.setImage;
        interests = binding.setInterestUser;

        binding.setImage.setOnClickListener(v ->{
            try {
                pickImage();
            }catch (Exception e){
                Log.d("my", "pickImage: failed");
            }
        });
        binding.postForm.setOnClickListener(v -> {
            if (checkEdit()){
                progressBar.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                mViewModel.sendData(ImageUri,name.getText().toString(),surname.getText().toString(), interests.getText().toString());
                NavHostFragment.findNavController(this).navigate(R.id.action_updateInfoFragment_to_new_graph);
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }
    private boolean checkEdit(){
        boolean flag = true;
        if (name.getText().toString().isEmpty()) {
            name.setError("Обязательное поле");
            flag = false;
        }
        if (surname.getText().toString().isEmpty()){
            surname.setError("Обязательное поле");
            flag = false;
        }
        if (ImageUri == null){
            Toast.makeText(getActivity(),"Выберите фото профиля", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
           binding.setImage.setImageURI(data.getData());
           ImageUri = data.getData();
        }
    }
}
