package com.example.acessolivre.combineclothes;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;

public class CombineClothes extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSION_CAMERA = 100;
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


    public void openCamera(View v) {
        Intent it = new Intent(this, TelaCamera.class);
        startActivity(it);
    }


    public void openDicas(View v) {
        Intent telaDicas = new Intent(this, TelaDicas.class);
        startActivity(telaDicas);
    }
}

