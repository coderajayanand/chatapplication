package com.example.as_chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//public class MainActivity extends AppCompatActivity {
//
//    FirebaseAuth auth;
//    RecyclerView mainUserRecyclerView;
//    UserAdapter adapter;
//    FirebaseDatabase database;
//    ArrayList<Users> usersArrayList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        database = FirebaseDatabase.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//        DatabaseReference reference = database.getReference().child("user");
//
//        usersArrayList = new ArrayList<>();
//
//        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
//        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new UserAdapter(MainActivity.this, usersArrayList);
//        mainUserRecyclerView.setAdapter(adapter);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Users users = dataSnapshot.getValue(Users.class);
//                    usersArrayList.add(users);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//        if(auth.getCurrentUser() == null)
//        {
//            Intent intent = new Intent(MainActivity.this, login.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//}

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("main actitivity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        } else {
            // User is already logged in, proceed with database setup
            System.out.println("Set up database is starting");
            setupDatabase();
        }
    }

    private void setupDatabase() {
        System.out.println("Started");
        DatabaseReference reference = database.getReference().child("user");

        usersArrayList = new ArrayList<>();
        System.out.println("List created");

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
   //             usersArrayList.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                    System.out.println("All users names are :- ");
                    System.out.println(users);
                    System.out.println(users.userName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
        adapter = new UserAdapter(MainActivity.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
    }
}



