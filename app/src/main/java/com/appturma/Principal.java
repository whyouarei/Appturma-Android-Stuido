package com.appturma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appturma.ui.principal.PrincipalFragment;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PrincipalFragment.newInstance())
                    .commitNow();
        }
    }
}