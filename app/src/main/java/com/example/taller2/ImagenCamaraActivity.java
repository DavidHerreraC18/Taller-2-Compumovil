package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImagenCamaraActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView image;
    private Button btnElegido,btnCamara;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_camara);
        image = findViewById(R.id.imagenElegida);
        btnElegido = findViewById(R.id.btnElegir);
        btnCamara = findViewById(R.id.btnCamara);
        btnElegido.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                PermissionUtil.requestPermission(ImagenCamaraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE,"Es para el funcionamiento",IMAGE_PICKER_REQUEST);
                abrirAlmacenamiento();
            }
        });
        btnCamara.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                PermissionUtil.requestPermission(ImagenCamaraActivity.this, Manifest.permission.CAMERA,"Es para el funcionamiento",REQUEST_IMAGE_CAPTURE);
                takePicture();
            }
        });
    }

    private void abrirAlmacenamiento()
    {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");
            startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
        }
    }

    private void takePicture()
    {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                takePicture();
                return;
            case IMAGE_PICKER_REQUEST:
                abrirAlmacenamiento();
                return;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case IMAGE_PICKER_REQUEST:
                if(resultCode==RESULT_OK)
                {
                    try{
                        final Uri imageUri= data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode==RESULT_OK)
                {
                    Bundle extras =data.getExtras();
                    Bitmap imageBitmap=(Bitmap)extras.get("data");
                    image.setImageBitmap(imageBitmap);
                }
                return;
        }
        setContentView(R.layout.activity_imagen_camara);
    }
}