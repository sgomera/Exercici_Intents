package com.example.exercici_intents;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "He entrat en onActivityResult";
    private boolean permissionGranted;
    private Button takePictureButton;
    private Button selectFileButton;
    private ImageView imageView;
    private Uri file;


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


    //mètode per fer la foto
    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
            }
        }

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using data.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                imageView.setImageURI(uri);
                Log.i(TAG, "Uri: " + uri.toString());
         //       showImage(uri);
            }
        }
    }




    //mètode per guardar la foto en un fitxer
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

//mètodes per sel·leccionar un fitxer: Fires an intent to spin up the "file chooser" UI and select an image
    public void performFileSearch(View view) {

    // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
    // browser.
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

    // Filter to only show results that can be "opened", such as a
    // file (as opposed to a list of contacts or timezones)
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    // Filter to show only images, using the image MIME data type.
    intent.setType("image/*");

    startActivityForResult(intent, 200);
}


    //obre el navegador amb la pàgina web introduïda
    public void onGoWebButton(View view){
        EditText webPage = (EditText)findViewById(R.id.webPage);

        if(URLUtil.isValidUrl(webPage.getText().toString())) {
            Uri uri = Uri.parse(webPage.getText().toString());
            Intent intentWeb = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intentWeb);
        } else {
            Toast.makeText(MainActivity.this,"Enter a valid URL", Toast.LENGTH_SHORT).show();
        }
    }


    //truca al número de telèfon introduït
    public void onCallPhoneNumber (View view){
        EditText phoneNumber = (EditText)findViewById(R.id.phone);

        Uri uri = Uri.parse("tel:" + phoneNumber.getText().toString());
        Intent intentPhone = new Intent(Intent.ACTION_DIAL);
        intentPhone.setData(uri);
        startActivity(intentPhone);

    }

    //Obre el text introduït en una nova pantalla
    public void onOpenText (View view){
        TextView textView = (TextView)findViewById(R.id.text);
        Intent intentText = new Intent(this, ShowText.class);

        String userText = textView.getText().toString();
        intentText.putExtra("text", userText);


        startActivity(intentText);
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
