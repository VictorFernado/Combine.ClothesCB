package com.example.acessolivre.combineclothes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.acessolivre.combineclothes.model.Photo;
import com.example.acessolivre.combineclothes.network.VolleySingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class VotePhotoActivity extends AppCompatActivity {

    private Photo photo;
    DatabaseReference photoRef;
    private TextView nota_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_photo);
        nota_tv = (TextView) findViewById(R.id.tx_nota);
        Intent it = getIntent();

        if(it == null) {
            finish();
            return;
        }
        String id = it.getStringExtra("id");
        // Intent Filter
        Uri data = it.getData();
        if (data != null) {
            id = data.getPathSegments().get(data.getPathSegments().size()-1);
            if(data != null && "".equals(id)){
                finish();
                return;
            }
        }


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        photoRef = database.getReference("photos").child(id);
        photoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photo = dataSnapshot.getValue(Photo.class);
                carregaPreenchePhoto();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(CombineClothes.TAG, this.getClass().getSimpleName()+ " " + databaseError.getMessage());
                finish();
                Toast.makeText(VotePhotoActivity.this, "Erro ao carregar photo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void carregaPreenchePhoto() {
        ((TextView) findViewById(R.id.tx_nota_media)).setText(photo.getNota().toString());
        NetworkImageView avatar = (NetworkImageView) findViewById(R.id.imageView);
        avatar.setImageUrl(photo.getUrlImage(), VolleySingleton.getInstance().getImageLoader());
    }

    public void compartilhar(View view){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "CombineClothes");
        share.putExtra(Intent.EXTRA_TEXT, "CombineClothes, Vote na minha combinação: http://combineclothes.tk/vote/"+photo.getId());
        startActivity(Intent.createChooser(share, "Compartilhar"));
    }

    private Double getNota(){
        return Double.parseDouble(nota_tv.getText().toString());
    }


    public void noteIncrement(View view){
        Double n = getNota();
        if(n < 10) {
            nota_tv.setText(String.valueOf(n+1));
        }
    }

    public void noteDecrement(View view){
        Double n = getNota();
        if(n > 1) {
            nota_tv.setText(String.valueOf(n-1));
        }
    }

    public void confirmarVoto(View view){
        final ProgressDialog progress = ProgressDialog.show(this,
                "Aguarde...",
                "Estamos registrando sua nota.", true);
        if(photo != null){
            photo.addNota(getNota());
            photoRef.setValue(photo);
        }
        view.setEnabled(false);
        findViewById(R.id.btn_min).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_max).setVisibility(View.INVISIBLE);
        //noinspection ResourceAsColor
        nota_tv.setTextColor(R.color.colorPrimaryDark);
        progress.dismiss();
    }
}
