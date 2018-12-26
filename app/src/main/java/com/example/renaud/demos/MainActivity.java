package com.example.renaud.demos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.renaud.demos.abstracted.SongDB;

public class MainActivity extends AppCompatActivity {


    private SongDB mSingletonSongs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final int REQUEST_SONGS_GOOOOOO = 6384;
    @Override
    protected void onStart() {
        super.onStart();

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPerms();
        } else {
            Refresh();
        }
    }

    private void requestPerms() {
        requestPermissions(
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                REQUEST_SONGS_GOOOOOO
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                ) {
            mSingletonSongs = SongDB.getInstance(this);
        } else {
            requestPerms();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SONGS_GOOOOOO: {
                if(permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Refresh();
                }
                break;
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            }
        }

    }

    private void Refresh() {
        if(mSingletonSongs == null) {
            mSingletonSongs = SongDB.getInstance(this);
            ((RecyclerView) findViewById(R.id.rvTunes)).setAdapter(mSingletonSongs);
        }
        mSingletonSongs.Refresh(this);
    }
}
