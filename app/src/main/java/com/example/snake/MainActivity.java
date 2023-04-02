package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private final List <Snakepuan> snakepuanList =new ArrayList<>();

    private SurfaceView surfaceView;
    private TextView puan_tablosu;
    private SurfaceHolder surfaceHolder;
    private String movingPosition = "right";
    private int puan = 0;
    private static final int snakeSize = 30 ;
    private static final int defaultTalePoints =3;
    private static final int snakeColor = Color.BLUE;
    private static final int snakeMovingSpeed =800;

public int positionX,positionY;
public Timer timer;
public Canvas canvas=null ;
public Paint pointColor=null ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView=findViewById(R.id.surfaceView);
        puan_tablosu=findViewById(R.id.puan_tablosu);

        final AppCompatImageButton up=findViewById(R.id.up_button);
        final AppCompatImageButton down=findViewById(R.id.down_button);
        final AppCompatImageButton right=findViewById(R.id.right_button);
        final AppCompatImageButton left=findViewById(R.id.left_button);

        //adding callback to surfaceview
        surfaceView.getHolder().addCallback(this);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(!movingPosition.equals("down")){
             movingPosition ="up";
}
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!movingPosition.equals("right")){
                    movingPosition ="left";
                }

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!movingPosition.equals("up")){
                    movingPosition ="down" ;
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!movingPosition.equals("left")){
                    movingPosition ="right";
                }
            }
        });

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
    this.surfaceHolder =surfaceHolder;

    init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
    private void init(){
    snakepuanList.clear();

    puan_tablosu.setText("0");

    puan = 0 ;
    movingPosition ="right";
            int startPositionX =(snakeSize) * defaultTalePoints;
            for(int i=0;i<defaultTalePoints;i++){

                Snakepuan snakepuan =new Snakepuan (startPositionX,snakeSize);
                snakepuanList.add(snakepuan);
                startPositionX=startPositionX -(snakeSize * 2);

            }
            addPoint();

            movesnake();
    }
    public void addPoint(){
        int surfaceWidth = surfaceView.getWidth() -(snakeSize * 2);
        int surfaceHeight = surfaceView.getHeight() -(snakeSize * 2);

        int randomXPosition = new Random().nextInt(surfaceWidth/snakeSize);
        int randomYPosition = new Random().nextInt(surfaceHeight/snakeSize);

        if((randomXPosition %2 )!= 0){
            randomXPosition = randomXPosition+1;
        }

        if((randomYPosition %2 )!= 0){
            randomYPosition = randomYPosition+1;
        }
         positionX=(snakeSize * randomXPosition)+snakeSize;
        positionY=(snakeSize * randomYPosition)+snakeSize;
    }
    public void movesnake(){

        timer =new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
             int headPositionX =snakepuanList.get(0).getPositionX();
                int headPositionY =snakepuanList.get(0).getPositionY();

                 if(headPositionX == positionX && headPositionY == positionY )    {
                     growSnake();
                     addPoint();
                 }
                 switch (movingPosition){
                     case "right":
                         snakepuanList.get(0).setPositionX(headPositionX + (snakeSize * 2));
                         snakepuanList.get(0).setPositionY(headPositionY );
                         break;
                     case "left":
                         snakepuanList.get(0).setPositionX(headPositionX - (snakeSize * 2));
                         snakepuanList.get(0).setPositionY(headPositionY );
                         break;
                     case "up":
                         snakepuanList.get(0).setPositionX(headPositionX );
                         snakepuanList.get(0).setPositionY(headPositionY - (snakeSize * 2));
                         break;
                     case "down":
                         snakepuanList.get(0).setPositionX(headPositionX );
                         snakepuanList.get(0).setPositionY(headPositionY + (snakeSize * 2));
                 break;
                 }
                 if(checkGameOver(headPositionX,headPositionY)){
                     timer.purge();
                     timer.cancel();
                     AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                     builder.setMessage("Puanınız = " +puan);
                     builder.setTitle("GAME OVER");
                     builder.setCancelable(false);
                     builder.setPositiveButton("Yeniden Oyna", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                             init();
                         }
                     });
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   builder.show();

                         }
                     });
                 }
                 else {

                     canvas = surfaceHolder.lockCanvas( );
                     canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                     canvas.drawCircle(snakepuanList.get(0).getPositionX(),snakepuanList.get(0).getPositionY(),snakeSize,createPointColor() );
                     canvas.drawCircle(positionX,positionY,snakeSize,createPointColor());

                     for(int i=1;i<snakepuanList.size();i++){
                         int getTempPositionX =snakepuanList.get(i).getPositionX();
                         int getTempPositionY =snakepuanList.get(i).getPositionY();

                         snakepuanList.get(i).setPositionX(headPositionX);
                         snakepuanList.get(i).setPositionY(headPositionY);
                         canvas.drawCircle(snakepuanList.get(i).getPositionX(),snakepuanList.get(i).getPositionY(),snakeSize,createPointColor());

                    headPositionX=getTempPositionX;
                     headPositionY=getTempPositionY;
                     }
                     surfaceHolder.unlockCanvasAndPost(canvas);
                 }
            }
        },1000-snakeMovingSpeed,1000-snakeMovingSpeed);
    }
public void growSnake(){

        Snakepuan snakepuan =new Snakepuan(0,0);
        snakepuanList.add(snakepuan);
        puan++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                puan_tablosu.setText(String.valueOf(puan));
            }
        });
}
    public boolean checkGameOver(int headPositionX,int headPositionY){
    boolean gameOver =false;
if(             snakepuanList.get(0).getPositionX() < 0  ||
                snakepuanList.get(0).getPositionY()<0 ||
                snakepuanList.get(0).getPositionX() >=surfaceView.getWidth() ||
                snakepuanList.get(0).getPositionY() >=surfaceView.getHeight()){
    gameOver=true;

        }
else{
    for (int i=0; i < snakepuanList.size();i++){
        if(headPositionX==snakepuanList.get(i).getPositionX() &&
                headPositionY==snakepuanList.get(i).getPositionY()) {

            gameOver=true;
            break;
        }
    }
}

    return gameOver;

    }
    public Paint createPointColor(){
 if(pointColor ==null){
     pointColor=new Paint();
     pointColor.setColor(snakeColor);
     pointColor.setStyle(Paint.Style.FILL);
     pointColor.setAntiAlias(true);

     return pointColor;
 }
 else {
     return pointColor;
 }
    }
}