package com.example.acessolivre.combineclothes;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;

public class CombineClothes extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSION_CAMERA = 100;
    public static final String TAG = "Script";
    public static final int REQUEST_INVITE = 59;
    private ImageButton btn_foto;
    private ImageButton btn_dicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_foto = (ImageButton) findViewById(R.id.btn_foto);
        btn_dicas = (ImageButton) findViewById(R.id.btn_dicas);
    }



    public void openCloset(View view){
        Intent it = new Intent(this, ClosetActivity.class);
        startActivity(it);
    }

    private void checkPermission(){


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.INTERNET
                    }, REQUEST_CODE_PERMISSION_CAMERA);
        } else {
            openCamera();
        }
    }

    public void openCamera() {
        Intent it = new Intent(this, TelaCamera.class);
        startActivity(it);
    }

    public void openCamera(View v) {
        checkPermission();
    }


    public void openDicas(View v) {
        Intent telaDicas = new Intent(this, TelaDicas.class);
        startActivity(telaDicas);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera();
                }
                break;
        }
    }
}

