package com.example.stokkacamata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private ImageView btnLogout3;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView TextNamaToko;
    DatabaseReference ref;
    ArrayList<ProfileBarang> list;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btnLogout3 = findViewById(R.id.btnLogout3);
        TextNamaToko = findViewById(R.id.TextNamaToko);
        ref = FirebaseDatabase.getInstance().getReference().child("ProfileBarang");
        recyclerView = findViewById(R.id.recyclerview2);
        searchView = findViewById(R.id.searchView2);

        TextNamaToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(Search.this, HalamanToko.class);
                startActivity(history);
            }
        });

        btnLogout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(Search.this, Login.class);
                startActivity(intToMain);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref != null)
        {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        list = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            list.add(dataSnapshot1.getValue(ProfileBarang.class));
                        }
                        AdapterClassBarang adapterClassBarang = new AdapterClassBarang(list);
                        recyclerView.setAdapter(adapterClassBarang);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Search.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    //search(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String string)
    {
        ArrayList<ProfileBarang> myList = new ArrayList<>();
        for(ProfileBarang object : list)
        {
            if(object.getMerk().toLowerCase().contains(string.toLowerCase()));
            {
                if(object.getTipe().toLowerCase().contains(string.toLowerCase()));
                {
                    if(object.getWarna().toLowerCase().contains(string.toLowerCase()));
                    {
                        if(object.getDeskripsi().toLowerCase().contains(string.toLowerCase()));
                        {
                            myList.add(object);
                        }
                    }
                }
            }
            /*
            if(object.getMerk().toLowerCase().contains(string.toLowerCase()));
            {
                myList.add(object);
            }
            if(object.getTipe().toLowerCase().contains(string.toLowerCase()));
            {
                myList.add(object);
            }
            if(object.getWarna().toLowerCase().contains(string.toLowerCase()));
            {
                myList.add(object);
            }
            if(object.getDeskripsi().toLowerCase().contains(string.toLowerCase()));
            {
                myList.add(object);
            }
            */
        }
        AdapterClassBarang adapterClassBarang = new AdapterClassBarang(myList);
        recyclerView.setAdapter(adapterClassBarang);
    }
}