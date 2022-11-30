package com.ranairu.creation;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<CourseModal> courseModalArrayList;
    private Context context;

    // creating a constructor for our variables.
    public CourseAdapter(ArrayList<CourseModal> courseModalArrayList, Context context) {
        this.courseModalArrayList = courseModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view.

        CourseModal modal = courseModalArrayList.get(position);
        holder.courseNameTV.setText(modal.getCourseName());
        holder.courseTracksTV.setText(modal.getCourseTracks());
        holder.courseModeTV.setText(modal.getCourseMode());

        //sembunyikan layout
        holder.courseTracksTV.setVisibility(View.GONE);

        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/RanairuCreation/Favorite");
                dir.mkdir();
                File file = new File(dir, "/"+modal.getCourseName()+".txt");
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    os.write(modal.getCourseTracks().toString().getBytes());
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, modal.getCourseName()+"Favoritmu :D", Toast.LENGTH_SHORT).show();

                Intent MoveintentWithData = new Intent(view.getContext(), Favorited.class);
                view.getContext().startActivity(MoveintentWithData);

            }

        });

        //tombol baca
        holder.btnBaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent MoveintentWithData = new Intent(view.getContext(), Baca.class);

                //ambil data dan memasukkan ke public static String EXTRA = "extra_name1" di Baca.java;
                MoveintentWithData.putExtra(Baca.EXTRA,modal.getCourseTracks());
                view.getContext().startActivity(MoveintentWithData);

//                LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View v2 = inflater.inflate(R.layout.layer_favorite, null);
//                AlertDialog.Builder a = new AlertDialog.Builder(view.getContext());
//                a.setView(v2);
//                a.show();
//                a.setCancelable(false);
            }
        });
        Picasso.get().load(modal.getCourseimg()).into(holder.courseIV);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return courseModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView courseNameTV, courseModeTV, courseTracksTV, tx;
        private ImageView courseIV;
        private Button btnBaca, btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseModeTV = itemView.findViewById(R.id.idTVBatch);
            courseTracksTV = itemView.findViewById(R.id.idTVTracks);
            courseIV = itemView.findViewById(R.id.idIVCourse);
            btnBaca = itemView.findViewById(R.id.idBaca);

            tx = itemView.findViewById(R.id.bacalah_cok);
            btnFavorite = itemView.findViewById(R.id.idfavorite);
        }
    }
}
