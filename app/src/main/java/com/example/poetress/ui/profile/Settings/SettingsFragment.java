package com.example.poetress.ui.profile.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.example.poetress.databinding.FragmentProfileSettingsBinding;
import com.example.poetress.view_model.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    ImageView backButton;
    Button exit;
    private SettingsViewModel mViewModel;
    FirebaseAuth firebaseAuth;
    FragmentProfileSettingsBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        backButton = binding.backButton;
        exit = binding.logOut;
        backButton.setOnClickListener(v->{
            NavHostFragment.findNavController(this).popBackStack();
        });
        exit.setOnClickListener(v -> {
            firebaseAuth.signOut();
            NavHostFragment.findNavController(this).navigate(R.id.action_Settings_to_Login);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}
