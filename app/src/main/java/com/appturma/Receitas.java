package com.appturma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Receitas extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle
                                savedInstanceState){
        View root = inflater.inflate(R.layout.activity_receitas,container, false);
        return root;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}