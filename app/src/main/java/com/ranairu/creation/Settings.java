package com.ranairu.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ranairu.creation.Filemanager.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Settings extends AppCompatActivity {

    public static final int REQUEST_PATH = 1;
    String curFileName, curDir;
    public int PICKFILE_ACTIVESAV = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        File currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/RanairuCreation/DownloadSplash");
        currentDir.mkdirs();

        Button CustomSplash = findViewById(R.id.customsplash);
        CustomSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("video/*");
//                startActivityForResult(Intent.createChooser(intent, "Select file"), 1);

                Intent intent1 = new Intent(Settings.this, FileChooser.class);
                startActivityForResult(intent1, REQUEST_PATH);
            }
        });

        Button stocksplash = findViewById(R.id.stocksplash);
        stocksplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hapus = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RanairuCreation/splash.mp4";
                deleteFiles(hapus);
                Toast.makeText(Settings.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

        //menampilkan ukuran layar sesuai dengan aplikasi
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        TextView reso = findViewById(R.id.reso);
        reso.setText("Rekomendasi Resolusi Video : " + height + " x " + width);
    }

    //for delete folder
    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // See which child activity is calling us back.
        super.onActivityResult(requestCode, resultCode, data);

        curFileName = data.getStringExtra("GetFileName");
        curDir = data.getStringExtra("GetPath");
        final String FinalPath = curDir + "/" + curFileName;

        if (requestCode == PICKFILE_ACTIVESAV && resultCode == Helper.RESULT_OK) {
            if (resultCode == RESULT_OK) {
                if (FinalPath.contains(".mp4")) {
                    File finalsource = new File(FinalPath);
                    File dest = new File(Environment.getExternalStorageDirectory(), "/RanairuCreation/splash.mp4");

                    File buatfolder = new File(Environment.getExternalStorageDirectory(), "/RanairuCreation");
                    buatfolder.mkdirs();

                    try {
                        copy(finalsource, dest);
                        if (dest.exists()) {
                            Toast.makeText(getApplicationContext(), "Saved...", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed, Check Your Permission...", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Or Android Not Supported", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Format Harus .mp4!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}