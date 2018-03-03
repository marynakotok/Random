package com.example.marina.random;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Vision extends AppCompatActivity {

    DBHelper dbHelper;
    DBHelper dbHelper_h;
    ListView list;
    ListView list_h;
    myListView_h adaptor_h;
    myListView adaptor;
    int[] ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper_h = new DBHelper(this);


        int orientation = getScreenOrientation();
        if (orientation == 1)
        {
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_vision);
            display_vertical();
        }
            if (orientation == 2)
            {
                //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_vision_h);
                display_horizontal();
            }

//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent toSpecific = new Intent (getApplicationContext(), SpecifiedUser.class);
//                    Toast.makeText(view.getContext(), "clicked",Toast.LENGTH_LONG).show();
//                    Log.v("ids: ", Integer.toString(ids[0]));
//                    toSpecific.putExtra("id", ids[i]);
//                    StartActivity(toSpecific);
//                }
//            });

//            list_h.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                     Intent toSpecific2 = new Intent (getApplicationContext(), SpecifiedUser.class);
//                     Toast.makeText(view.getContext(), "clicked",Toast.LENGTH_LONG).show();
//                     toSpecific.putExtra("id", id[i]);
//                     StartActivity(toSpecific2);
//                }
//            });
    }

    public int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
              setContentView(R.layout.activity_vision_h);
              display_horizontal();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
              setContentView(R.layout.activity_vision);
              display_vertical();
        }
    }

    private void display_vertical() {
        int number_rows;
        int index = 0;

        list = (ListView) findViewById(R.id.listView);
        //header
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.header_style, list, false);
        list.addHeaderView(header);

        //DATABASE
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS,null,null,null,null,null,null);
        number_rows = cursor.getCount();
        //setting the arrays
        String[] names = new String[number_rows];
        String[] genders = new String[number_rows];
        String[] pictures = new String[number_rows];
        String[] localizations = new String[number_rows];
        String[] dates = new String[number_rows];
        ids = new int[number_rows];

        //Log.v("names 1", Integer.toString(names.length));
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int localizationIndex = cursor.getColumnIndex(DBHelper.KEY_LOCALIZATION);
            int genderIndex = cursor.getColumnIndex(DBHelper.KEY_GENDER);
            int usernameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int pictureIndex = cursor.getColumnIndex(DBHelper.KEY_PICTURE);
            int natIndex = cursor.getColumnIndex(DBHelper.KEY_NAT);
            int registeredIndex = cursor.getColumnIndex(DBHelper.KEY_REGISTERED);
            do {
//                Log.d("Data-contact: ", "ID: " + cursor.getInt(idIndex) +
//                        " NAT: " + cursor.getString(natIndex) +
//                        " NAME: " + cursor.getString(nameIndex) +
//                        " LOCALIZATION: " + cursor.getString(localizationIndex) +
//                        " GENDER: " + cursor.getString(genderIndex) +
//                        " USERNAME: " + cursor.getString(usernameIndex) +
//                        " PASSWORD: " + cursor.getString(passwordIndex) +
//                        " PICTURE: " + cursor.getString(pictureIndex) +
//                        " REGISTERED: " + cursor.getString(registeredIndex)
//                );
                int id = cursor.getInt(idIndex);
                String nationality = cursor.getString(natIndex);
                String name = cursor.getString(nameIndex);
                String localization = cursor.getString(localizationIndex);
                String gender = cursor.getString(genderIndex);
                String username = cursor.getString(usernameIndex);
                String password = cursor.getString(passwordIndex);
                String picture = cursor.getString(pictureIndex);
                String registered = cursor.getString(registeredIndex);
                String firstGender = String.valueOf(gender.charAt(0)).toUpperCase();

                names[index] = name;
                genders[index] = firstGender;
                dates[index] = registered;
                pictures[index] = picture;
                localizations[index] = localization;
                ids[index] = id;
                index++;


            } while (cursor.moveToNext());
            adaptor = new myListView(Vision.this, names, genders, dates, localizations, pictures);
            list.setAdapter(adaptor);

        } else{
            //Log.d("Error", "No users");
            Intent openSorry = new Intent(getApplicationContext(), Sorry.class);
            startActivity(openSorry);
            //Toast.makeText(this, "No users yet", Toast.LENGTH_LONG).show();
        }
        cursor.close();
        dbHelper.close();
     }

    private void display_horizontal() {

        int number_rows;
        int index = 0;
        list_h = (ListView) findViewById(R.id.listView_h);
        //header
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.header_style_h, list_h, false);
        list_h.addHeaderView(header);

        //DATABASE

        SQLiteDatabase database = dbHelper_h.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS,null,null,null,null,null,null);
        number_rows = cursor.getCount();
        //setting the arrays
        String[] names = new String[number_rows];
        String[] genders = new String[number_rows];
        String[] usernames = new String[number_rows];
        String[] passwords = new String[number_rows];
        String[] dates = new String[number_rows];

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int localizationIndex = cursor.getColumnIndex(DBHelper.KEY_LOCALIZATION);
            int genderIndex = cursor.getColumnIndex(DBHelper.KEY_GENDER);
            int usernameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int pictureIndex = cursor.getColumnIndex(DBHelper.KEY_PICTURE);
            int natIndex = cursor.getColumnIndex(DBHelper.KEY_NAT);
            int registeredIndex = cursor.getColumnIndex(DBHelper.KEY_REGISTERED);
            do {
                int id = cursor.getInt(idIndex);
                String nationality = cursor.getString(natIndex);
                String name = cursor.getString(nameIndex);
                String localization = cursor.getString(localizationIndex);
                String gender = cursor.getString(genderIndex);
                String username = cursor.getString(usernameIndex);
                String password = cursor.getString(passwordIndex);
                String picture = cursor.getString(pictureIndex);
                String registered = cursor.getString(registeredIndex);
                String firstGender = String.valueOf(gender.charAt(0)).toUpperCase();

                names[index] = name;
                genders[index] = firstGender;
                dates[index] = registered;
                usernames[index] = username;
                passwords[index] = password;
                index++;
            }
            while (cursor.moveToNext());

            adaptor_h = new myListView_h(Vision.this, names, genders, dates, usernames, passwords);
            list_h.setAdapter(adaptor_h);

        } else
            {
                Intent openSorry2 = new Intent(getApplicationContext(), Sorry.class);
                startActivity(openSorry2);
                //Toast.makeText(this, "No users yet!", Toast.LENGTH_LONG).show();
                //Log.d("Error", "No users");
            }
        cursor.close();
        dbHelper_h.close();
    }
}


