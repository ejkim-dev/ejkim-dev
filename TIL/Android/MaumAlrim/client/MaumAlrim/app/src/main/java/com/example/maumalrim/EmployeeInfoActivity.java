package com.example.maumalrim;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maumalrim.databinding.ActivityEmployeeInfoBinding;

public class EmployeeInfoActivity extends AppCompatActivity {

    ActivityEmployeeInfoBinding employeeInfoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeInfoBinding = ActivityEmployeeInfoBinding.inflate(getLayoutInflater());
        View view = employeeInfoBinding.getRoot();
        setContentView(view);
        
    }
}
