package com.appturma.ui.principal;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appturma.R;
import com.appturma.databinding.PrincipalFragmentBinding;

public class PrincipalFragment extends Fragment {

    private PrincipalViewModel mViewModel;
    private PrincipalFragmentBinding binding;

    public static PrincipalFragment newInstance() {
        return new PrincipalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.principal_fragment, container, false);

        binding = PrincipalFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.btnPetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_petar);
            }
        });
        binding.btnReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_receitas);
            }

         });
    }

    @Override
        public void onDestroyView(){
        super.onDestroyView();
        binding = null;
        }
}

