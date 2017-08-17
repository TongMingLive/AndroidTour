package com.example.tong.androidtour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.tong.androidtour.Bean.Friend;

/**
 * Created by tong on 17-3-30.
 */

public class FriendPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        TextView title = (TextView) findViewById(R.id.friend_title);
        TextView name = (TextView) findViewById(R.id.friend_name);
        TextView page = (TextView) findViewById(R.id.friend_page);

        Friend friend = (Friend) getIntent().getSerializableExtra("friend");

        title.setText(friend.getFriendTitle());
        name.setText(friend.getUserName());
        page.setText(friend.getFriendPage());


    }
}
