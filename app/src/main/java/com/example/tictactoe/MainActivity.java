package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView playeronescore,playertwoscore,playerstatus;
    private Button [] buttons;
    private Button resetgame;
    private int playeronescorecount,playertwoscorecount,rountcount;
    boolean activeplayer;
    //p1=>0
    //p2=>1
    //empty=>2
    private SoundPool soundpool;
    private int sound1,sound2,sound3;

    int [] gamestate = {2,2,2,2,2,2,2,2,2};
    int [][] winningpositions={
            {0,1,2}, {3,4,5},{6,7,8},//rows
            {0,3,6}, {1,4,7}, {2,5,8},//collumns
            {0,4,8},{2,4,6}//cross
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.help:
                Toast.makeText(this, "helping...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.searchos:
                Toast.makeText(this, "searching...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.namename:
                Toast.makeText(this, "saving...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sayNo:
                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sayYes:
                Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playeronescore=findViewById(R.id.playeronescore);
        playertwoscore=findViewById(R.id.playertwoscore);
        playerstatus=findViewById(R.id.playerstatus);
        resetgame = (Button) findViewById(R.id.reset_game);
        buttons=new Button[9];
       for(int i=0;i<buttons.length;i++){
           String buttonID= "btn_"+i;
           int resourceID=getResources().getIdentifier(buttonID,"id",getPackageName());
           buttons[i]=(Button) findViewById(resourceID);
           buttons[i].setOnClickListener(this);}
       rountcount=0;
       playeronescorecount=0;
       playertwoscorecount=0;
       activeplayer=true;


       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
           AudioAttributes audioAttributes =new AudioAttributes.Builder()
                   .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                   .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                   .build();
           soundpool=new SoundPool.Builder()
                   .setMaxStreams(3)
                   .setAudioAttributes(audioAttributes)
                   .build();
       }else{
                   soundpool= new SoundPool(3,AudioManager.STREAM_MUSIC,0);
       }
       sound1=soundpool.load(this,R.raw.windows_error,1);
       //sound2=soundpool.load(this,R.raw.ringtone5,1);
      // sound3=soundpool.load(this,R.raw.ringtone7,1);
    }
   public void playsound(View v){
        switch (v.getId()){
            case R.id.reset_game:
                soundpool.play(sound1,1,1,0,0,1);
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundpool.release();
        soundpool=null;
    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")){
            return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gamestatepointer =Integer.parseInt(buttonID.substring(buttonID.length()-1,buttonID.length()));
        if (activeplayer)
        {
            ((Button)v).setText("x");
            ((Button)v).setTextColor(Color.parseColor("#FFC34A"));
            gamestate[gamestatepointer]=0;
        }
        else{
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#70FFEA"));
            gamestate[gamestatepointer]=1;
        }
        rountcount++;
        if(checkwinner()){
            if(activeplayer){
                playeronescorecount++;
                updateplayerscore();
                Toast.makeText(this, "player one won!!!!",Toast.LENGTH_SHORT).show();
                playagain();
            }else{
                playertwoscorecount++;
                updateplayerscore();
                Toast.makeText(this, "player two won!!!!",Toast.LENGTH_SHORT).show();
                playagain();
            }
        }else if(rountcount==9){
            playagain();
            Toast.makeText(this, "no winner!!!!",Toast.LENGTH_SHORT).show();
        }
        else {
            activeplayer=!activeplayer;
        }
        if(playeronescorecount>playertwoscorecount){
            playerstatus.setText("player one is winning!!!!");
        }else if(playertwoscorecount>playeronescorecount){
            playerstatus.setText("player two is winning!!!!");
        }else{
            playerstatus.setText("");
        }
        resetgame.setOnClickListener(new View.OnClickListener(){
            @Override
                    public  void onClick(View v){
                playagain();
                playeronescorecount=0;
                playertwoscorecount=0;
                playerstatus.setText("");
                updateplayerscore();
            }
        });
    }
    public boolean checkwinner(){
        boolean winnerresult= false;
        for(int [] winningposion : winningpositions){
            if(gamestate[winningposion[0]]==gamestate[winningposion[1]]
                    &&gamestate[winningposion[1]] ==gamestate[winningposion[2]]
                    &&gamestate[winningposion[0]]!=2){
                winnerresult= true;


            }
        }
        return winnerresult;
    }
    public void updateplayerscore(){
        playeronescore.setText(Integer.toString(playeronescorecount));
        playertwoscore.setText(Integer.toString(playertwoscorecount));

    }
    public void playagain(){
        rountcount = 0;
        activeplayer=true;
        for (int i=0;i< buttons.length;i++){
            gamestate[i] = 2;
            buttons[i].setText("");
        }
    }


}