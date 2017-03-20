package com.android.yihl.catmouseelephant;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void startGame(View btn) { //here must be View, not Button
        Intent gameIntent = new Intent(this,GameActivity.class);
        startActivity(gameIntent);
    }

    public void quitGame(View view) {
        finishAndRemoveTask ();
    }

    public void aboutGame(View view) {
        Intent aboutIntent = new Intent(this,AboutActivity.class);
        startActivity(aboutIntent);
    }


}
