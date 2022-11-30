package com.ranairu.creation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Favorited extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorited);

        //Inflater SERVICE
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.layer_favorite, null);

// ((v.findViewById)) untuk mengarahkan ke inflater, berbeda dengan (v.) biasanya
        TextView textView = (TextView) v.findViewById(R.id.judulFavo);
        Button btnBacaFavo = v.findViewById(R.id.bacafavo);
        Button btnHapusFavo = v.findViewById(R.id.hapusfavo);


        final ListView listview = (ListView) findViewById(R.id.listView1);
//        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
//                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//                "Android", "iPhone", "WindowsMobile"};

        //pencarian files di folder
        String path = Environment.getExternalStorageDirectory().toString() + "/RanairuCreation/Favorite";
        File directory = new File(path);
        //fix 2.0 buat folder
        directory.mkdirs();
        File[] files = directory.listFiles();

        if (directory.exists()){

            //fungsi listview
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < files.length; ++i) {
                list.add(files[i].getName().replaceAll(".txt",""));

            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                //fungsi fileslist ketika di klik
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//klik yg akan terjadi

                    AlertDialog.Builder a = new AlertDialog.Builder(Favorited.this);
                    a.setView(v);
                    a.show();
                    a.setCancelable(true);

                    //Fungsi Seting Array Terpilih dari variable "name"
                    String name = (String) parent.getItemAtPosition(position);

                    textView.setText(name);


                    btnHapusFavo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //folder
                            String arah = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RanairuCreation/Favorite/"+name;

                            //fungsi hapus
                            File file = new File(arah);
                            file.delete();
                            if(file.exists()){
                                try {
                                    file.getCanonicalFile().delete();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(file.exists()){
                                    getApplicationContext().deleteFile(file.getName());
                                }
                            }

                            //Reload Activity
                            finish();
                            startActivity(getIntent());

                        }
                    });

                    btnBacaFavo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
                            File sdcard = Environment.getExternalStorageDirectory();
//Get the text file
                            File file = new File(sdcard,"/RanairuCreation/Favorite/"+name);

//Read text from file
                            StringBuilder text = new StringBuilder();

                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;

                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();
                            }
                            catch (IOException e) {
                                //You'll need to add proper error handling here
                            }

                            Intent MoveintentWithData = new Intent(view.getContext(), Baca.class);
                            MoveintentWithData.putExtra(Baca.EXTRA,text.toString());
                            //ambil data dan memasukkan ke public static String EXTRA = "extra_name1" di Baca.java;
                            startActivity(MoveintentWithData);
                        }
                    });
                }

            });

        } else {
            Toast.makeText(this, "belum ada favorite", Toast.LENGTH_SHORT).show();
        }


    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}