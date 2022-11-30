package com.ranairu.creation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CustomSplash extends Helper {

    public static String EXTRA_DOWNLOAD = "extra_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_splash);

        ProgressBar pg = findViewById(R.id.splashloading);

        WebView webView = (WebView) findViewById(R.id.splash_webview);
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
                if (url.startsWith("https://ranairu")) {
                    pg.setVisibility(View.VISIBLE);

                    //fungsi di dalam fungsi setWebViewClient
                    webView.setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            // do your stuff here
                            pg.setVisibility(View.GONE);
                        }
                    });

//                    LayoutInflater objek2 = getLayoutInflater();
//                    View v2 = objek2.inflate(R.layout.layer_favorite, null);
//                    AlertDialog.Builder a = new AlertDialog.Builder(CustomSplash.this);
//                    a.setView(v2);
//                    a.show();
//                    a.setCancelable(true);
                }
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                pg.setVisibility(View.GONE);
            }
        });
        webView.loadUrl("https://ranairucreation.000webhostapp.com/splash/index.html");

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading File...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File " + URLUtil.guessFileName(url, contentDisposition, mimetype), Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(getApplicationContext(), CustomSplash.class);
//                intent.putExtra(EXTRA_DOWNLOAD,URLUtil.guessFileName(url, contentDisposition, mimetype));

                SharedPreferences.Editor editor = getSharedPreferences("pref_key", MODE_PRIVATE).edit();
                editor.putString("name", URLUtil.guessFileName(url, contentDisposition, mimetype));
                editor.apply();

            }
        });

        //download compleate
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Toast.makeText(ctxt, "Download Finish" + "", Toast.LENGTH_SHORT).show();

                //tambahan fungsi 2.0
                //dialog
                new AlertDialog.Builder(ctxt)
                        .setTitle("Download Sukses")
                        .setMessage("Gasken Ganti Splashscreen ??")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = getSharedPreferences("pref_key", MODE_PRIVATE);
                                String name = prefs.getString("name", "No name defined");

                                Toast.makeText(ctxt, "Prosessing File..", Toast.LENGTH_LONG).show();
                                //copy file
                                File finalsource = new File("/" + Environment.DIRECTORY_DOWNLOADS + "/" + name);
                                File dest = new File(Environment.getExternalStorageDirectory(), "/RanairuCreation/" + name);

                                try {
                                    copy(finalsource, dest);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                Toast.makeText(ctxt, "Selesai..", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_menu_save)
                        .show();

            }
        };
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        Button btnsplash = findViewById(R.id.gotosplash);
        btnsplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navHome = new Intent(CustomSplash.this, Settings.class);
                startActivity(navHome);
            }
        });
    }
}