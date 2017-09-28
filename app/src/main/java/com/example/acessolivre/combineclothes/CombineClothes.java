package com.example.acessolivre.combineclothes;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.*;
        import android.view.*;
        import android.content.*;

public class CombineClothes extends AppCompatActivity implements View.OnClickListener {

    private ImageButton makeClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeClick = (ImageButton)findViewById(R.id.makeClick);
        makeClick.setOnClickListener(this);

    }

    public void onClick(View v){
        Intent it = new Intent(this, TelaCamera.class);
        startActivity(it);
    }
}

