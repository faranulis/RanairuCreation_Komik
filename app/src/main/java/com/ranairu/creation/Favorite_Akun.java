package com.ranairu.creation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Favorite_Akun extends AppCompatActivity {

    private EditText etKategori, etTanggal, etJudul, etIsi;
    private Button btSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_akun);
        etKategori=findViewById(R.id.etKategori);
        etTanggal=findViewById(R.id.etTanggal);
        etJudul=findViewById(R.id.etJudul);
        etIsi=findViewById(R.id.etIsi);
        btSimpan=findViewById(R.id.btSimpan);
        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
    }
    private void simpan(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("berita");

        myRef.child(etKategori.getText().toString()).child(etTanggal.getText().toString()).child(etJudul.getText().toString()).setValue(etIsi.getText().
                toString());
//        Intent form3 = new
//                Intent(Favorite_Akun.this,MainActivity3.class);
//        form3.putExtra("Kategori",etKategori.getText().toString());
//        form3.putExtra("Tanggal",etTanggal.getText().toString());
//        form3.putExtra("Judul",etJudul.getText().toString());
//        startActivity(form3);
    }
}
