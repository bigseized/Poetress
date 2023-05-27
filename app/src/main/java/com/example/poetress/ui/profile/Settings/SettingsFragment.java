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
import com.example.poetress.view_model.profile.SettingsViewModel;

public class SettingsFragment extends Fragment {
    ImageView backButton;
    Button exit, updataData;
    private SettingsViewModel mViewModel;
    FragmentProfileSettingsBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        backButton = binding.backButton;
        exit = binding.logOut;
        updataData = binding.updateData;

        backButton.setOnClickListener(v->{
            NavHostFragment.findNavController(this).popBackStack();
        });
        exit.setOnClickListener(v -> {
            mViewModel.SignOut();
            NavHostFragment.findNavController(this).navigate(R.id.action_Settings_to_Login);
        });
        updataData.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("update", true);
            NavHostFragment.findNavController(this).navigate(R.id.action_Settings_to_UpdateData, bundle);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}
