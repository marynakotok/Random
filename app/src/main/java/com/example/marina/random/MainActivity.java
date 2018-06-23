package com.example.marina.random;

import android.content.Context;
import android.content.Intent;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.Cursor;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


     private Button insert;
     private Button see;
     private Button delete;
     DBHelper dbHelper;
     SQLiteDatabase database;
     int number_rows;
    SharedPreferences sharedPreferences;
    String db_contain;
    String decrypted_phrase;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        see = (Button) findViewById(R.id.see);
        insert = (Button) findViewById(R.id.insert);
        delete = (Button) findViewById(R.id.delete);

        sharedPreferences = getSharedPreferences("com.example.marina.random", Context.MODE_PRIVATE);

        db_contain = sharedPreferences.getString("_phrase", null);
        if (db_contain != null)
        {
            decrypted_phrase = decrypt(db_contain, getResources().getString(R.string.password));
        } else Toast.makeText(this, "Database problem!", Toast.LENGTH_LONG).show();

        see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openSee = new Intent(getApplicationContext(), Vision.class);
                    startActivity(openSee);
                }
            });
        insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openInsert = new Intent(getApplicationContext(), Insertion.class);
                    startActivity(openInsert);
                }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase.loadLibs(view.getContext());
                database = dbHelper.getWritableDatabase(decrypted_phrase);
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS,null,null,null,null,null,null);
                number_rows = cursor.getCount();
                if (number_rows == 0)
                {
                    Toast.makeText(view.getContext(),"There no users!", Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
                else
                {
                    database.execSQL("delete from "+ DBHelper.TABLE_CONTACTS);
                    Toast.makeText(view.getContext(),"Users are deleted", Toast.LENGTH_SHORT).show();
                }
                dbHelper.close();
            }
        });

    }

    public String decrypt(String ciphertext, String password) {
        return Crypto.decryptPbkdf2(ciphertext, password);
    }

}
