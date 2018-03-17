package com.example.marina.random;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginScreen extends AppCompatActivity {

    private Button login;
    private EditText username, password;
    DBHelper dbHelper;
    String usernamedb, passworddb;
    String usernameString, passwordString;
    String salt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        login = (Button) findViewById(R.id.loginB);
        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);

        dbHelper = new DBHelper(this);

        //insertion data to table login

        String usernameExample = "Alejandro";
        String passwordExample = "5555";
        salt = "ZcDy2N5WF8";
        String passwordToHash = salt + passwordExample;
        String passwordHasheddb = toHash(passwordToHash); //variable will be hacked

        if (!passwordHasheddb.equals("-1"))
        {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            //setting data to database
            contentValues.put(DBHelper.CHECK_USERNAME, usernameExample);
            contentValues.put(DBHelper.CHECK_PASSWORD, passwordHasheddb);

            database.insert(DBHelper.TABLE_LOGIN, null, contentValues);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });
        }
        else { Toast.makeText(this, "Error 0005: Developer knows(external)", Toast.LENGTH_LONG).show();}

    }

    public void login()
    {
        //retrieving from the database
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_LOGIN,null,null,null,null,null,null);
        int number_rows = cursor.getCount();

        if (cursor.moveToFirst())
    {
        int idIndex = cursor.getColumnIndex(DBHelper.CHECK_ID);
        int usernameIndex = cursor.getColumnIndex(DBHelper.CHECK_USERNAME);
        int passwordIndex = cursor.getColumnIndex(DBHelper.CHECK_PASSWORD);

        int id = cursor.getInt(idIndex);
        usernamedb = cursor.getString(usernameIndex);
        passworddb = cursor.getString(passwordIndex);

    } else{ Toast.makeText(this, "No USERS IN DATABASE", Toast.LENGTH_LONG).show();}

        cursor.close();

        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        if (passwordString.length() == 4 && usernameString.length() > 0)
        {
            String passwordStringToHash = salt + passwordString;
            String passwordHashed = toHash(passwordStringToHash);

            if (!passwordHashed.equals("-1"))
            {
                //checking if credentials are correct
                if (usernameString.equals(usernamedb) && passwordHashed.equals(passworddb))
                {
                    Intent openConfirm = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(openConfirm);
                    Toast.makeText(this, "Your credentials are correct. You are logged in!", Toast.LENGTH_LONG).show();
                    database.execSQL("delete from "+ DBHelper.TABLE_LOGIN);
                }
                else Toast.makeText(this, "Your credentials are not valid. Try again!", Toast.LENGTH_LONG).show();

                dbHelper.close();
            }
            else Toast.makeText(this, "Error 0005: Developer knows(external)", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Password should include 4 digits and username cannot be empty", Toast.LENGTH_LONG).show();

    }

    public String toHash(String input)
    {
        String hash;
        byte [] inputByte = input.getBytes();
        try {
            MessageDigest SHA256 = MessageDigest.getInstance("SHA-256");
            SHA256.update(inputByte);
            byte [] digest = SHA256.digest();
            //compute byte digest to hex format
            StringBuffer hexDigest = new StringBuffer();
            for (int i=0; i<digest.length; i++ )
            {
                hexDigest.append(Integer.toString((digest[i]&0xff)+0x100,16).substring(1));
            }
            hash = hexDigest.toString();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "-1";
        }
    }

}
