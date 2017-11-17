package com.example.acessolivre.combineclothes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.acessolivre.combineclothes.R;
import com.example.acessolivre.combineclothes.adapters.PhotoAdapter;
import com.example.acessolivre.combineclothes.model.Photo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClosetActivity extends AppCompatActivity implements ChildEventListener{
    private ListView listView;
    private PhotoAdapter adapter;
    private List<Photo> listPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);
        listView = (ListView) findViewById(R.id.lv);
        listPhotos = new ArrayList<>();
        adapter = new PhotoAdapter(this,android.R.layout.simple_list_item_1, listPhotos);
        listView.setAdapter(adapter);
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
        Log.e("Script", databaseError.getMessage());
    }
}
