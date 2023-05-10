package com.example.poetress.ui.sign_up_sign_in;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.lang.Character;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.RegisterFormBinding;
import com.example.poetress.view_model.RegisterViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private RegisterFormBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    String regex;
    private EditText email,password, password_repeat;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RegisterFormBinding.inflate(inflater);
        progressBar = binding.progressBar;
        constraintLayout = binding.constraintLayoutSubMain;
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        email = binding.etEmail;
        password = binding.etPass;
        password_repeat = binding.passRepeat;
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){
            NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_new_graph);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding.btnRegistration.setOnClickListener(v-> {
            if (passwordCheck(password.getText().toString().trim(), password_repeat.getText().toString().trim())) {
                constraintLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Register", "Create: successful");
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Письмо с подтверждением отправленно", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment);
                                constraintLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Log.d("Register", "Verification send");
                            } else {
                                constraintLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity().getApplicationContext(), "Письмо не отправленно", Toast.LENGTH_SHORT).show();
                                Log.d("Register", task1.getException().toString());
                            }

                        });

                    } else {
                        constraintLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getApplicationContext(), "Письмо не отправленно", Toast.LENGTH_SHORT).show();
                        Log.d("Register", task.getException().toString());
                    }
                });
            }
        });


        binding.btnLogInrform.setOnClickListener(v->{
            NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }
    public boolean passwordCheck(String password, String repeat_password) {

        if (password.length() < 8) {
            binding.etPass.setError("Минимальная длина пароля: 8");
            return false;
        }
        boolean flag = false;
        for (int i = 0; i < password.length(); i++) {
            int code = password.charAt(i);
            if (!((code >= 'A' && code <= 'Z') || (code >= 'a' && code <= 'z') || Character.isDigit(code))) {
                binding.etPass.setError("Пароль может содержать только латинские цифры и буквы");
                return false;
            } else {
                if (Character.isDigit(code)) {
                    flag = true;
                }
            }
        }
        if (!password.equals(repeat_password)){
            binding.etPass.setError("Пароли не совпадают");
            return false;
        }
        if (!flag){
            binding.etPass.setError("Пароль должен содержать минимум одну цифру");
        }
        return flag;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
    }
}
