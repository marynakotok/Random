package com.example.marina.random;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

     private Button insert;
     private Button see;
     private Button delete;
     DBHelper dbHelper;
     int number_rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        see = (Button) findViewById(R.id.see);
        insert = (Button) findViewById(R.id.insert);
        delete = (Button) findViewById(R.id.delete);

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
                final SQLiteDatabase database = dbHelper.getWritableDatabase();
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
}
