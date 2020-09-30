package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactosActivity extends AppCompatActivity {

    Cursor cursor;
    String[] columnas;
    TextView permissionStatus;
    ContactsAdapter adaptador;
    ListView listaContactos;
    private static final int statusContacts = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        listaContactos = findViewById(R.id.listaContactos);
        columnas = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
        };
        adaptador = new ContactsAdapter(this,null,0);
        listaContactos.setAdapter(adaptador);

        PermissionUtil.requestPermission(this, Manifest.permission.READ_CONTACTS,"Es para el funcionamiento",statusContacts);

        usarPermiso();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case statusContacts: {
                usarPermiso();
                return;
            }

        }
    }

    public void usarPermiso(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,columnas,null,null,null);
            adaptador.changeCursor(cursor);
        }
    }
}