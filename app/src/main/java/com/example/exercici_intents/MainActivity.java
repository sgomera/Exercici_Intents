package com.example.exercici_intents;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_READ = 1001;
    private static final int REQUEST_PERMISSION_CAMERA = 2000;
    private boolean permissionGranted;
    private Button takePictureButton;
    private Button selectFileButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = findViewById(R.id.button_image);
        selectFileButton = findViewById(R.id.button_file);
        imageView = findViewById(R.id.imageview);

        if (!permissionGranted) {
            checkPermissions();
        }
    }

    // Initiate check for permissions.
    private boolean checkPermissions() {

        int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionCheckReadStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((permissionCheckCamera != PackageManager.PERMISSION_GRANTED) || permissionCheckReadStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
            return false;
        } else {
            return true;
        }
    }


    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    //          && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Camera permission granted",
                            Toast.LENGTH_SHORT).show();
                    takePictureButton.setEnabled(true);
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        selectFileButton.setEnabled(true);
                        Toast.makeText(this, "Read storage permission granted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Select file button disabled", Toast.LENGTH_SHORT).show();
                        selectFileButton.setEnabled(false);
                    }
                    break;
                } else {
                    Toast.makeText(this, "Take picture button disabled", Toast.LENGTH_SHORT).show();
                    takePictureButton.setEnabled(false);
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        selectFileButton.setEnabled(true);
                        Toast.makeText(this, "Read storage permission granted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Select file button disabled", Toast.LENGTH_SHORT).show();
                        selectFileButton.setEnabled(false);
                    }
                    break;
                }

        }
    }
}
