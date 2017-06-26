package com.gulbalasalamov.audioappdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private static final int KAYIT_SORGU_KODU = 101;
    private static final int HAFIZA_SORGU_KODU = 102;

    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static String dosyaKonumu;
    private static Button btn_oynat;
    private static Button btn_kayit;
    private static Button btn_dur;

    private boolean kayitEdiliyorMu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        btn_oynat = (Button) findViewById(R.id.button_oynat);
        btn_kayit = (Button) findViewById(R.id.button_kayit);
        btn_dur = (Button) findViewById(R.id.button_dur);

        if (!varMikrofon()) {
            btn_oynat.setEnabled(false);
            btn_kayit.setEnabled(false);
            btn_dur.setEnabled(false);
        } else {
            btn_oynat.setEnabled(false);
            btn_dur.setEnabled(false);
        }
        dosyaKonumu = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sesdosyam.3gp";
        requestPermission(Manifest.permission.RECORD_AUDIO, KAYIT_SORGU_KODU);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, HAFIZA_SORGU_KODU);
    }

    protected boolean varMikrofon() {
        PackageManager packageManager = this.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    public void kayitSes(View view) {
        kayitEdiliyorMu = true;
        btn_dur.setEnabled(true);
        btn_oynat.setEnabled(false);
        btn_kayit.setEnabled(false);

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(dosyaKonumu);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        Toast.makeText(this, "Kayıt başladı.", Toast.LENGTH_SHORT).show();
    }

    public void durSes(View view) {
        btn_dur.setEnabled(false);
        btn_oynat.setEnabled(true);

        if (kayitEdiliyorMu) {
            btn_kayit.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            kayitEdiliyorMu = false;
            Toast.makeText(this, "Kayıt durdu.", Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
            btn_kayit.setEnabled(true);
        }
    }

    public void oynatSes(View view) throws IOException {
        btn_oynat.setEnabled(false);
        btn_kayit.setEnabled(false);
        btn_dur.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(dosyaKonumu);
        mediaPlayer.prepare();
        mediaPlayer.start();
        Toast.makeText(this, "Kayıt oynatılıyor.", Toast.LENGTH_SHORT).show();
    }

    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this, permissionType);
        if (permission != PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestCode);
        }
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int[] grantResults) {
        switch (requestCode) {
            case KAYIT_SORGU_KODU: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    btn_kayit.setEnabled(false);
                    Toast.makeText(this, "İzin gerekiyor.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case HAFIZA_SORGU_KODU: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    btn_kayit.setEnabled(false);
                    Toast.makeText(this, "Hafıza kartı kayıt izni gerekiyor", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
