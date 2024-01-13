package com.example.as_chats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {

    String receiverimg, receiveruid, receivername, senderuid;
    CircleImageView profile;
    TextView receiverNName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        receivername = getIntent().getStringExtra("nameee");
        receiverimg = getIntent().getStringExtra("receiverimg");
        receiveruid = getIntent().getStringExtra("uid");

        profile = findViewById(R.id.profileimgg);
        receiverNName = findViewById(R.id.recivername);

        Picasso.get().load(receiverimg).into(profile);
        receiverNName.setText(""+receivername);
    }
}