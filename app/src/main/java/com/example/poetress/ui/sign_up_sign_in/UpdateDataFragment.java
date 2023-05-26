package com.example.poetress.ui.sign_up_sign_in;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    Boolean flag = false, update;
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
        try {
            update = getArguments().getBoolean("update");

        } catch (NullPointerException e){

        }

        if (update){
            binding.setSurnameUser.setVisibility(View.GONE);
            binding.setNameUser.setVisibility(View.GONE);
        }else{
            binding.setSurnameUser.setVisibility(View.VISIBLE);
            binding.setNameUser.setVisibility(View.VISIBLE);
        }
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

        mViewModel.getIsSended().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_updateInfoFragment_to_new_graph);
                    progressBar.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getActivity(),"Ошибка публикации", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);

                }
            }
        });


        binding.setImage.setOnClickListener(v ->{
            try {
                pickImage();
            }catch (Exception e){
                Log.d("my", "pickImage: failed");
            }
        });
        binding.postForm.setOnClickListener(v -> {
            if (checkEdit()){
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                progressBar.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                mViewModel.sendData(ImageUri,name.getText().toString(),surname.getText().toString(), interests.getText().toString());
            }
        });
    }
    private boolean checkEdit(){
        boolean flag = true;
        if (name.getText().toString().isEmpty() && !update) {
            name.setError("Обязательное поле");
            flag = false;
        }
        if (surname.getText().toString().isEmpty() && !update){
            surname.setError("Обязательное поле");
            flag = false;
        }
        if (ImageUri == null || !flag){
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
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK && data != null) {
           binding.setImage.setImageURI(data.getData());
           ImageUri = data.getData();
           flag = true;
        }
    }
}
