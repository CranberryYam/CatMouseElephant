package com.android.yihl.catmouseelephant;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {
    private boolean gameIsBegan;
    private String playerRole = "void";
    private String computerRole = "void";

    private int winTimes = 0;
    private int failTimes = 0;
    private int tieTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameIsBegan = false;
    }

    public void fightPikachu(View btn){
        String IdAsString = btn.getResources().getResourceName(btn.getId());
        String[] IdArray = IdAsString.split("_");
        String name = IdArray[1];

        if(gameIsBegan != true){
            gameIsBegan = true;
            playerRole = name;
            computerRole = computerFight();
            moveDownFence();
        }

    }



    public void moveDownFence() {
        int fencedownDuration = 600;
        int fadeInDuration = 200;
        int timeBetween = 100;

        String fenceString = "fence_" + playerRole;
        int id = getResId(fenceString, R.id.class);
        ImageView image = (ImageView)findViewById(id);
        Animation moveDown = new TranslateAnimation(0,0,0,100);
        moveDown.setFillEnabled(true);
        moveDown.setFillAfter(true);
        moveDown.setDuration(fencedownDuration);
        image.startAnimation(moveDown);


        String roleString = "role_" + playerRole;
        int id2 = getResId(roleString, R.id.class);
        ImageView role = (ImageView)findViewById(id2);
        Animation fadeIn = new AlphaAnimation(1, 0);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(fadeInDuration);
        fadeIn.setStartOffset(fencedownDuration+timeBetween);
        fadeIn.setFillAfter(true);
        fadeIn.setFillEnabled(true);
        role.startAnimation(fadeIn);

        addRoleOnStage(playerRole, true, fencedownDuration+timeBetween+fadeInDuration+400);
        addRoleOnStage(computerRole,false,fencedownDuration+timeBetween+fadeInDuration+400+800);

    }

    public void addRoleOnStage(String name, final boolean isPlayer, int delayTime) {
        int id;
        if(isPlayer == true){
            id = R.id.player;
        }else {
            id = R.id.computer;
        }
        ImageView role = (ImageView)findViewById(id);
        int imageID = getResId(name, R.drawable.class);
        role.setImageResource(imageID);

        AnimationSet set = new AnimationSet(false);
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(700);
        Animation grow = new ScaleAnimation(0.5f,1,0.5f,1);
        grow.setInterpolator(new AccelerateInterpolator());
        grow.setDuration(700);
        set.addAnimation(fadeOut);
        set.addAnimation(grow);
        set.setStartOffset(delayTime);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(playerRole!="void" && computerRole!="void"&&!isPlayer){
                    showResult(playerRole, computerRole);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        role.setAnimation(set);
    }



    public void showResult(final String pl, final String com) {
        int result = caculate3Objects(pl, com);
        ImageView medol = (ImageView)findViewById(R.id.medolImage);
        switch (result){
            case 0:
                Log.v("good", "tie");
                tieTimes++;
                medol.setImageResource(R.drawable.medol_tie);
                break;
            case 1:
                Log.v("good", "player win");
                winTimes++;
                medol.setImageResource(R.drawable.medol_win);
                break;
            case 2:
                Log.v("good", "computer win");
                failTimes++;
                medol.setImageResource(R.drawable.medol_fail);
                break;
        }
        ObjectAnimator animation = ObjectAnimator.ofFloat(medol, "rotationY", 0.0f, 360f);
        animation.setDuration(1000);
        animation.setRepeatCount(1);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

        //renewGame(pl, com);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                renewGame(pl, com);
            }
        }, 3000);
    }

    public void renewGame(String pl, String com) {
        TextView recordPanel = (TextView)findViewById(R.id.recordPanel);
        int rounds = winTimes+failTimes+tieTimes;
        String record = "win:"+String.valueOf(winTimes)+"   fail:"+String.valueOf(failTimes)
                +"   tie:"+String.valueOf(tieTimes)+"   round:"+String.valueOf(rounds);
        recordPanel.setText(record);

        ImageView playerRole = (ImageView)findViewById(R.id.player);
        ImageView computerRole = (ImageView)findViewById(R.id.computer);
        ImageView medol = (ImageView)findViewById(R.id.medolImage);
        playerRole.setImageResource(0);
        computerRole.setImageResource(0);
        medol.setImageResource(0);

        String fenceString = "fence_" + pl;
        int id = getResId(fenceString, R.id.class);
        ImageView image = (ImageView)findViewById(id);
        Animation moveDown = new TranslateAnimation(0,0,100,0);
        moveDown.setFillEnabled(true);
        moveDown.setFillAfter(true);
        moveDown.setDuration(0);
        image.startAnimation(moveDown);


        String roleString = "role_" + pl;
        int id2 = getResId(roleString, R.id.class);
        ImageView role = (ImageView)findViewById(id2);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(0);
        fadeIn.setFillAfter(true);
        fadeIn.setFillEnabled(true);
        role.startAnimation(fadeIn);

        gameIsBegan = false;
    }
//*************         foundation function             ***************/
    public String computerFight(){
      int Min = 1;
      int Max = 4;
      int animalID = Min + (int)(Math.random() * ((Max - Min) + 1));
      String name;
      if(animalID == 1){
        name = "cat";
      }else if(animalID == 2){
        name = "mouse";
      }else{
        name = "elephant";
      }
      return name;
    }

    public int caculate3Objects(String pl1, String pl2) {
        String r = "cat";
        String p = "mouse";
        String s = "elephant";
        if(pl1.equals(pl2)){
            return 0;
        }else{
            if(pl1.equals(r)&&pl2.equals(p)||pl1.equals(p)&&pl2.equals(s)||
                                              pl1.equals(s)&&pl2.equals(r)){
                return 1;
            }else{
                return 2;
            }
        }
    }

    public int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


}
