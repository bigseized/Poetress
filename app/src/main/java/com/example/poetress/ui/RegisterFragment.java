package com.example.poetress.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.Character;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.RegisterFormBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private RegisterFormBinding binding;
    FirebaseAuth firebaseAuth;
    String regex;
    private EditText email,password, password_repeat;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = RegisterFormBinding.inflate(inflater);
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
        binding.btnRegistration.setOnClickListener(v-> {
            if (passwordCheck(password.getText().toString().trim(), password_repeat.getText().toString().trim())) {

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Register", "Create: successful");
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Письмо с подтверждением отправленно", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment);
                                Log.d("Register", "Verification send");
                            } else {
                                Log.d("Register", task1.getException().toString());
                            }

                        });

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity().getApplicationContext(), "Минимальная длина пароля: 8", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean flag = false;
        for (int i = 0; i < password.length(); i++) {
            int code = password.charAt(i);
            if (!((code >= 'A' && code <= 'Z') || (code >= 'a' && code <= 'z') || Character.isDigit(code))) {
                Toast.makeText(getActivity().getApplicationContext(), "Пароль может содержать только латинские цифры и буквы", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (Character.isDigit(code)) {
                    flag = true;
                }
            }
        }
        if (!flag){
            Toast.makeText(getActivity().getApplicationContext(), "Пароль должен содержать минимум одну цифру", Toast.LENGTH_SHORT).show();

        }
        return flag;
    }

}
