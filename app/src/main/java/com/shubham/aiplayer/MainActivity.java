package com.shubham.aiplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    RelativeLayout parent,upper,lower;
    SpeechRecognizer speechRecognizer;
    Intent speechintent;
    String keeper = "";
    Button voiceplay;
    GifImageView image;
    ImageView play,next,prev;
    TextView songnametxt;
    public String mode="OFF";
    private MediaPlayer mediaPlayer;
    private  int position;
    private ArrayList<File> mysong;
    private  String msongname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check_voice_permission();


        parent = findViewById(R.id.parent);
        upper =findViewById(R.id.upper);
        lower =findViewById(R.id.lower);

        voiceplay=findViewById(R.id.voicebutton);
        image=findViewById(R.id.songimg);
        play=findViewById(R.id.play);

        next=findViewById(R.id.next);
        prev=findViewById(R.id.prev);
        songnametxt=findViewById(R.id.songname);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);

        speechintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        validate();

        image.setBackgroundResource(R.drawable.e);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(MainActivity.this, "Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                String message;
                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        message = "Audio recording error";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        message = "Client side error";
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        message = "Insufficient permissions";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        message = "Network error";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        message = "Network timeout";
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        message = "No match";
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        message = "RecognitionService busy";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        message = "error from server";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        message = "No speech input";
                        break;
                    default:
                        message = "Didn't understand, please try again.";
                        break;
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    if(mode.equals("ON")){speechRecognizer.startListening(speechintent);
                    keeper = matches.get(0);
                    if(keeper.equals("pause the song") || keeper.equals("pause") || keeper.equals("pause music") || keeper.equals("stop") ){
                        playpausesong();
                        Toast.makeText(MainActivity.this, "" + keeper, Toast.LENGTH_LONG).show();
                    }
                    if(keeper.equals("play the song")|| keeper.equals("play") || keeper.equals("play music")|| keeper.equals("play the music")){
                        playpausesong();
                        Toast.makeText(MainActivity.this, "" + keeper, Toast.LENGTH_LONG).show();
                    }
                        if(keeper.equals("play next song")||keeper.equals("next")||keeper.equals("play next")){
                            playnextsong();
                            Toast.makeText(MainActivity.this, "" + keeper, Toast.LENGTH_LONG).show();
                        }
                        if(keeper.equals("play previous song")||keeper.equals("previous")||keeper.equals("play previous")){
                            playprevsong();
                            Toast.makeText(MainActivity.this, "" + keeper, Toast.LENGTH_LONG).show();
                        }
                }
                    Toast.makeText(MainActivity.this, "You Said:" + keeper, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechintent);
                        keeper = "";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;

                }
                return false;
            }
        });

        voiceplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("ON")){mode="OFF";
                    speechRecognizer.stopListening();
                voiceplay.setText("VOICE ENABLE - OFF");
                lower.setVisibility(View.VISIBLE);
                voiceplay.setBackgroundResource(R.drawable.shape);
                    voiceplay.setTextColor(getResources().getColor(R.color.white));
                }else{mode="ON";speechRecognizer.startListening(speechintent);
                    voiceplay.setText("VOICE ENABLE - ON");
                    lower.setVisibility(View.INVISIBLE);
                    voiceplay.setBackgroundResource(R.drawable.shape1);
                    voiceplay.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpausesong();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.getCurrentPosition()>0){
                playnextsong();}
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.getCurrentPosition()>0)
                playprevsong();
            }
        });



    }
    private void validate(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysong=(ArrayList) bundle.getParcelableArrayList("song");
        msongname=mysong.get(position).getName();
        String name= i.getStringExtra("name");
        songnametxt.setText(name);
        songnametxt.setSelected(true);
        position=bundle.getInt("position",0);
        Uri uri=Uri.parse(mysong.get(position).toString());
        mediaPlayer =MediaPlayer.create(MainActivity.this,uri);
        mediaPlayer.start();

    }
    private void check_voice_permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED)) {
                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(i);
                finish();
            }
        }
    }
    private void playnextsong(){
        ((GifDrawable)image.getBackground()).start();
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        position=((position+1)%mysong.size());
        Uri uri=Uri.parse(mysong.get(position).toString());
        mediaPlayer=MediaPlayer.create(MainActivity.this,uri);
        msongname=mysong.get(position).toString();
        songnametxt.setText(msongname);
        mediaPlayer.start();
        image.setBackgroundResource(R.drawable.b);
        if(mediaPlayer.isPlaying()){
            play.setImageResource(R.drawable.pause);
            ((GifDrawable)image.getBackground()).start();
        }else{
            play.setImageResource(R.drawable.play);
            image.setBackgroundResource(R.drawable.c);
            ((GifDrawable)image.getBackground()).stop();
        }
    }
    private void playprevsong() {
        ((GifDrawable)image.getBackground()).start();
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        position=((position-1)<0 ? (mysong.size()-1):(position-1));
        Uri uri=Uri.parse(mysong.get(position).toString());
        mediaPlayer=MediaPlayer.create(MainActivity.this,uri);
        msongname=mysong.get(position).toString();
        songnametxt.setText(msongname);
        mediaPlayer.start();
        image.setBackgroundResource(R.drawable.f);
        if(mediaPlayer.isPlaying()){
            play.setImageResource(R.drawable.pause);
            ((GifDrawable)image.getBackground()).start();
        }else{
            play.setImageResource(R.drawable.play);
            image.setBackgroundResource(R.drawable.b);
            ((GifDrawable)image.getBackground()).stop();
        }

    }

        private void playpausesong(){
            ((GifDrawable)image.getBackground()).start();
        if(mediaPlayer.isPlaying()){
            play.setImageResource(R.drawable.play);
            ((GifDrawable)image.getBackground()).stop();
            mediaPlayer.pause();
        }else{
            play.setImageResource(R.drawable.pause);
            ((GifDrawable)image.getBackground()).start();
            mediaPlayer.start();
        }
    }
}
