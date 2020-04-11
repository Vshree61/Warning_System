package com.example.warningsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewActivity extends AppCompatActivity {
    private Button mReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        mReturn = findViewById(R.id.btn_grant);
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewActivity.this,permissionActivity.class));
                finish();
            }
        });

    }
}
