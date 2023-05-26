package com.example.poetress.ui.sign_up_sign_in;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.data.types.UserMainData;
import com.example.poetress.databinding.LoginFormBinding;
import com.example.poetress.view_model.LoginViewModel;
import com.example.poetress.view_model.UpdateDataViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private LoginViewModel mViewModel;
    private LoginFormBinding binding;
    EditText email, password;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    ConstraintLayout allItems;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        email = binding.etEmail;
        progressBar = binding.progressBar;
        allItems = binding.allItems;
        password = binding.etPass;
        List<Task<?>> tasks = new ArrayList<>();

        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){
            progressBar.setVisibility(View.VISIBLE);
            allItems.setVisibility(View.INVISIBLE);
            mViewModel.loadData();
            mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
                if ((data.getSurname().isEmpty() || data.getName().isEmpty())){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("update", false);
                    NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_info, bundle);
                }
                else{
                    NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_new_graph);
                }
            });
            mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
                if (error != null && error.equals("Document does not exist")) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("update", false);
                    NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_info,bundle);
                }
                else{
                    NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_new_graph);
                }
            });
        }
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LoginFormBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            binding.btnIn.setOnClickListener(v-> {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                progressBar.setVisibility(View.VISIBLE);
                allItems.setVisibility(View.INVISIBLE);
                try {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            try {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                    mViewModel.loadData();
                                    mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
                                        if ((data.getSurname().isEmpty() || data.getName().isEmpty())){
                                            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_info);
                                        }
                                        else{
                                            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_new_graph);
                                        }
                                    });
                                    mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
                                        if (error != null && error.equals("Document does not exist")) {
                                            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_info);
                                        }
                                        else{
                                            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_new_graph);
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Электронная почта не подтверждена",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    allItems.setVisibility(View.VISIBLE);
                                }
                            }catch (NullPointerException e){
                                Log.wtf("Login", "Login: nullpointer");
                            }

                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(),"Пользователь с такими данными не найден",Toast.LENGTH_SHORT).show();
                            Log.d("Login", task.getException().toString());
                            progressBar.setVisibility(View.INVISIBLE);
                            allItems.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),"Поля не должны быть пустыми",Toast.LENGTH_SHORT).show();
                    Log.d("Login", e.getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    allItems.setVisibility(View.VISIBLE);
                }

            });
        binding.btnSignUplform.setOnClickListener(v->{
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment);
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
    }
}
