package com.ranairu.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inflater_menu);

        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.inflater_menu, null);

        Button btn1 = customView.findViewById(R.id.btn_akun);
        btn1.setText("uwwoogg");
    }
}