package com.shubham.aiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MusicFiles extends AppCompatActivity {
private String[] itemsAll;
    ListView songlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_files);
        songlist = (ListView)  findViewById(R.id.songlist);

        apppermission();

    }
    private void apppermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */
                    display();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */
                    token.continuePermissionRequest();
                    }
                }).check();
    }
    public ArrayList<File> readonlyAudio(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] allfiles=file.listFiles();
        for(File indi: allfiles){
            if(indi.isDirectory() && indi.isHidden()){
                arrayList.addAll(readonlyAudio(indi));
            }else{
                if(indi.getName().endsWith(".mp3") || indi.getName().endsWith(".aac") || indi.getName().endsWith(".wav") || indi.getName().endsWith(".wma")){
                    arrayList.add(indi);
                }
            }
        }
        return arrayList;
    }
    public void display(){
        final ArrayList<File> audio=readonlyAudio(Environment.getExternalStorageDirectory());
        itemsAll=new String[audio.size()];
        for(int songcounter=0;songcounter<audio.size();songcounter++ ){
            itemsAll[songcounter]=audio.get(songcounter).getName();
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MusicFiles.this,android.R.layout.simple_list_item_1,itemsAll);

        songlist.setAdapter(arrayAdapter);

        songlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName=songlist.getItemAtPosition(position).toString();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("song",audio);
                i.putExtra("name",songName);
                i.putExtra("position",position);
                startActivity(i);
            }
        });
    }
}
