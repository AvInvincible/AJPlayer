/*
//This is a media player app with  functionalites
// For only one song - to reference to device song list
// Application support play, pause, forward and rewind functionality
*/

package com.avinc.ajplayer;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.util.concurrent.TimeUnit;

import static com.avinc.ajplayer.R.id.seekbar;

public class MainActivity extends AppCompatActivity {


    private ImageView play, forward, rewind;
    private TextView songTitle, startTime,endTime; // seek bar info
    private MediaPlayer player;
    private int songStatus = 0;
    private SeekBar seekBar;
    private int forwardTime, backwardtime = 5000;
    private double currentTime = 0;
    private double finalTime = 0;
    public static int seekbarFinishTime = 0;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (ImageView)findViewById(R.id.play);
        player = MediaPlayer.create(this, R.raw.mehram);
        seekBar = (SeekBar)findViewById(seekbar);
        seekBar.setClickable(false);

        //get current and final time
        currentTime = player.getCurrentPosition();
        finalTime = player.getDuration();

        //setting the seebbar finish time
        if (seekbarFinishTime == 0) {
            seekBar.setMax((int) finalTime);
            seekbarFinishTime = 1;
        }


        //Setting the song start and final time
        startTime = (TextView)findViewById(R.id.startTime);
        startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime),
                TimeUnit.MILLISECONDS.toSeconds((long) currentTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                currentTime))));

        endTime = (TextView)findViewById(R.id.endTime);
        endTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime))));

        //seekbar progress
        seekBar.setProgress((int)currentTime);
        myHandler.postDelayed(UpdateSongTime,100);


        //Setting song title
        songTitle = (TextView)findViewById(R.id.SongTitle);
        songTitle.setText("Mehram Kahaani");

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songStatus == 0) {
                    play.setImageResource(R.drawable.pause);
                    Toast.makeText(getApplicationContext(), "Playing Song", Toast.LENGTH_SHORT).show();
                    player.start();
                    songStatus = 1;
                }else{
                    play.setImageResource(R.drawable.playbutton);
                    Toast.makeText(getApplicationContext(),"Song Stopped", Toast.LENGTH_SHORT).show();
                    player.pause();
                    songStatus = 0;
                }
            }
        });

        //code to forward song by 5 sec
        forward = (ImageView)findViewById(R.id.fast_forward);
        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int temp = (int)currentTime;
                if((temp+forwardTime)<=finalTime){
                    currentTime = currentTime + forwardTime;
                    player.seekTo((int)currentTime);
                    Toast.makeText(getApplicationContext(),"Jumped 5 sec",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Can not jump",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //code to rewing song by 5 sec
        rewind = (ImageView)findViewById(R.id.rewind);
        rewind.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int temp = (int)currentTime;
                if((temp-backwardtime)<=finalTime){
                    currentTime = currentTime - backwardtime;
                    player.seekTo((int)currentTime);
                    Toast.makeText(getApplicationContext(),"Rewind 5 sec",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No Action",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            currentTime = player.getCurrentPosition();
            startTime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) currentTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) currentTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) currentTime)))
            );
            seekBar.setProgress((int)currentTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
