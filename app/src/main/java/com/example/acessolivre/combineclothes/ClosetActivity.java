package com.example.acessolivre.combineclothes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.acessolivre.combineclothes.R;
import com.example.acessolivre.combineclothes.adapters.PhotoAdapter;
import com.example.acessolivre.combineclothes.model.Photo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.value;

public class ClosetActivity extends AppCompatActivity implements ChildEventListener{
//    private ListView listView;
    private PhotoAdapter adapter;
    private List<Photo> listPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);
        listPhotos = new ArrayList<>();
        adapter = new PhotoAdapter(this, listPhotos);
        GridView gridView = (GridView) findViewById(R.id.gv);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ClosetActivity.this, VotePhotoActivity.class);
                it.putExtra("id", listPhotos.get(position).getId());
                startActivity(it);
            }
        });
//        listView = (ListView) findViewById(R.id.lv);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent it = new Intent(ClosetActivity.this, VotePhotoActivity.class);
//                it.putExtra("id", listPhotos.get(position).getId());
//                startActivity(it);
//            }
//        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("photos");
        // Read from the database
        myRef.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        listPhotos.add(dataSnapshot.getValue(Photo.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Photo p = dataSnapshot.getValue(Photo.class);
        if(listPhotos.indexOf(p) > -1) {
            Photo aux = listPhotos.get(listPhotos.indexOf(p));
            aux = dataSnapshot.getValue(Photo.class);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        listPhotos.remove(dataSnapshot.getValue(Photo.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i("Script", "Moved"  + dataSnapshot.getValue(Photo.class).toString());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("Script", databaseError.getMessage());
    }
}
