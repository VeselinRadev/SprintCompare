package com.veselin.sprintcompare.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.acitvities.CountDown;

public class StartRunFragment extends Fragment {

    public StartRunFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_start_run, container, false);
        Button btn = v.findViewById(R.id.start_btn);
        EditText distanceET = v.findViewById(R.id.distanceET);
        btn.setOnClickListener(view -> {
            if(!distanceET.getText().toString().equals("")) {
                startActivity(new Intent(getActivity(), CountDown.class).putExtra("distance", distanceET.getText().toString()));
            }else{
                Toast.makeText(getActivity(), "Please enter running distance!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}