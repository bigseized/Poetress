package com.example.poetress.ui;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.LoginFormBinding;
import com.example.poetress.databinding.RegisterFormBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private LoginViewModel mViewModel;
    private LoginFormBinding binding;
    EditText email, password;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        email = binding.etEmail;
        password = binding.etPass;
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_new_graph);
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
            binding.btnIn.setOnClickListener(v-> {
                try {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            try {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                    NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_new_graph);
                                    Log.d("Login", "Login: succesfull");
                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Электронная почта не подтверждена",Toast.LENGTH_SHORT).show();
                                }
                            }catch (NullPointerException e){
                                Log.wtf("Login", "Login: nullpointer");
                            }

                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(),"Пользователь с такими данными не найден",Toast.LENGTH_SHORT).show();
                            Log.d("Login", task.getException().toString());
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),"Поля не должны быть пустыми",Toast.LENGTH_SHORT).show();
                    Log.d("Login", e.getMessage());
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
}
