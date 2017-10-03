package com.example.acessolivre.combineclothes;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.*;
        import android.view.*;
        import android.content.*;

public class CombineClothes extends AppCompatActivity  {

    private ImageButton btn_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_foto = (ImageButton)findViewById(R.id.btn_foto);

    }

    public void openCamera(View v){
        Intent it = new Intent(this, TelaCamera.class);
        startActivity(it);
    }
}

