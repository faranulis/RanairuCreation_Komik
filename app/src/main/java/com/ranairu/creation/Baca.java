package com.ranairu.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Baca extends AppCompatActivity {
    public static String EXTRA = "extra_name1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baca);

        //url textview diatas
//        TextView tx = findViewById(R.id.bacalah_cok);

        //Mengambil data dari public static String EXTRA = "extra_name1";
        String nama1 = getIntent().getStringExtra(EXTRA);
        String text1 = nama1;
//        tx.setText(text1);

        WebView webView = (WebView) findViewById(R.id.webviewUtama);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

// Tiga baris di bawah ini agar laman yang dimuat dapat
// melakukan zoom.
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUserAgentString(null);

// Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

//------------------------------//
//next aktifity
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //deteksi dalam html apakah ada (string) maka akan berubah fungsi ke sini dan di deklarasi
                if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mailto:") || url.startsWith("mms:") || url.startsWith("mmsto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    view.loadUrl(url);
                }
                if (url.startsWith("favorite:htt")) {
//                    webView.loadUrl("https://ranairucreation.000webhostapp.com/index.php");
//                    LayoutInflater objek2 = getLayoutInflater();
//                    View v2 = objek2.inflate(R.layout.layer_favorite, null);
//                    AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
//                    a.setView(v2);
//                    a.show();
//                    a.setCancelable(true);
                }
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
//                ProgressBar pg = findViewById(R.id.loading);
//                pg.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(text1);
    }
}