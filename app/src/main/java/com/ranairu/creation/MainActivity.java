package com.ranairu.creation;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ranairu.creation.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Helper {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // creating variables for
    // our ui components.
    private RecyclerView courseRV;

    // variable for our adapter
    // class and array list
    private CourseAdapter adapter;
    private ArrayList<CourseModal> courseModalArrayList;

    // below line is the variable for url from
    // where we will be querying our data.
    String url = "https://ranairucreation.000webhostapp.com/data.json";
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    EditText enterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

requestAppPermissions();

// initializing our variables.
        courseRV = findViewById(R.id.idRVCourses);
        progressBar = findViewById(R.id.idPB);

        // below line we are creating a new array list
        courseModalArrayList = new ArrayList<>();
        getData();

        // calling method to
        // build recycler view.
        buildRecyclerView();

        TextView namaakun = findViewById(R.id.namaakun);

        TextView txt = findViewById(R.id.akun);

        //inflater immplementasi

//        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View cellView = inflater.inflate(R.layout.inflater_menu, null);


//        Button btn_akun = findViewById(R.id.btn_akun);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            txt.setText("menggunakan akun..");
            FirebaseUser user = mAuth.getCurrentUser();
            String hello = "Hi";
            hello = hello.concat(" ").concat(user.getEmail());

            //sensor kata dari string hello
            hello = hello.replaceAll("@gmail.com","..@");
            hello = hello.replaceAll("a","..");

            namaakun.setText(hello);

            //menambahkan icon lewat java simple mode
            namaakun.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_account_circle_24, 0, 0, 0);

//            btn_akun.setText("Logout");
//            btn_akun.setBackgroundColor(Color.parseColor("#EC3F3F"));
//


        }else {
            txt.setVisibility(View.GONE);
            namaakun.setText("Ranairu Creation");
//            btn_akun.setText("Login");
//            btn_akun.setBackgroundColor(Color.parseColor("#4CAF50"));

        }
//        Toolbar judul = findViewById(R.id.toolbar);

        TextView menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater objek2 = getLayoutInflater();
                View v2 = objek2.inflate(R.layout.inflater_menu, null);
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setView(v2);
                a.show();
                a.setCancelable(true);
            }
        });
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                courseRV.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    // creating a new json object and
                    // getting each object from our json array.
                    try {
                        // we are getting each json object.
                        JSONObject responseObj = response.getJSONObject(i);

                        // now we get our response from API in json object format.
                        // in below line we are extracting a string with
                        // its key value from our json object.
                        // similarly we are extracting all the strings from our json object.
                        String courseName = responseObj.getString("judul");
                        String courseTracks = responseObj.getString("link");
                        String courseMode = responseObj.getString("deskripsi");
                        String courseImageURL = responseObj.getString("gambar");
                        courseModalArrayList.add(new CourseModal(courseName, courseImageURL, courseMode, courseTracks));
                        buildRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new CourseAdapter(courseModalArrayList, MainActivity.this);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        courseRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        courseRV.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent navHome = new Intent(MainActivity.this, Settings.class);
            MainActivity.this.startActivity(navHome);
            return true;
        } else if (id == R.id.aboutus){
            Intent navHome = new Intent(MainActivity.this, About.class);
            startActivity(navHome);
            return true;
        } else if (id == R.id.favorited){
            Intent navHome = new Intent(MainActivity.this, Favorited.class);
            MainActivity.this.startActivity(navHome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void idSplash(View view) {
        Intent intent = new Intent(MainActivity.this,CustomSplash.class);
        startActivity(intent);
    }

    public void manga(View view) {
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void Akun(View view) {
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }

    public void Favorite(View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.inflater_pilih_favorite, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

//        Intent intent = new Intent(MainActivity.this,Favorite_Akun.class);
//        startActivity(intent);
    }

    public void Lokal(View view) {
        Intent intent = new Intent(MainActivity.this,Favorited.class);
        startActivity(intent);
    }

    public void Cloud(View view) {
        Intent intent = new Intent(MainActivity.this,Favorite_Akun.class);
        startActivity(intent);
    }

    public void Pengaturan(View view) {
        Intent intent = new Intent(MainActivity.this,Settings.class);
        startActivity(intent);
    }

    public void Aboutus(View view) {
        Intent intent = new Intent(MainActivity.this,About.class);
        startActivity(intent);
    }

}