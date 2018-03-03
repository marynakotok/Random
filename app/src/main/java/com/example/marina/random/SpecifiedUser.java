package com.example.marina.random;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class SpecifiedUser extends AppCompatActivity {

    private Button back;
    private Button delete;
    private TextView mname;
    private TextView mgender;
    private TextView mdate;
    private ImageView mpicture;
    private TextView mpassword;
    private TextView musername;
    private TextView mcity;
    DBHelper dbHelper;
    DBHelper dbHelper2;
    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specified_user);
        delete = (Button) findViewById(R.id.delete);
        back = (Button) findViewById(R.id.back);
        mname = (TextView) findViewById(R.id.name_sp_value);
        mgender = (TextView) findViewById(R.id.gender_sp_value);
        mdate = (TextView) findViewById(R.id.date_sp_value);
        mpicture = (ImageView) findViewById(R.id.photo_sp);
        mpassword = (TextView) findViewById(R.id.password_sp_value);
        musername = (TextView) findViewById(R.id.username_sp_value);
        mcity = (TextView) findViewById(R.id.city_sp_value);
        dbHelper = new DBHelper(this);
        dbHelper2 = new DBHelper(this);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            idUser = mBundle.getInt("id");
        }
        //DATABASE
        SQLiteDatabase database = dbHelper2.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

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
                if (id == idUser)
                {
                    String nationality = cursor.getString(natIndex);
                    String name = cursor.getString(nameIndex);
                    String localization = cursor.getString(localizationIndex);
                    String gender = cursor.getString(genderIndex);
                    String username = cursor.getString(usernameIndex);
                    String password = cursor.getString(passwordIndex);
                    String picture = cursor.getString(pictureIndex);
                    String registered = cursor.getString(registeredIndex);
                    String firstGender = String.valueOf(gender.charAt(0)).toUpperCase();
                    mname.setText(name);
                    mgender.setText(gender);
                    mdate.setText(registered);
                    new DownLoadImageTask(mpicture).execute(picture);
                    mpassword.setText(password);
                    musername.setText(username);
                    break;
                }
            } while (cursor.moveToNext());
        }
            cursor.close();
            dbHelper2.close();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openList = new Intent(getApplicationContext(), Vision.class);
                    startActivity(openList);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //deleting the user from database by id
                    final SQLiteDatabase database = dbHelper.getWritableDatabase();
                    database.execSQL("delete from " + DBHelper.TABLE_CONTACTS
                            + " where " + DBHelper.KEY_ID + "='" + idUser + "';"); //Iduser from another activity
                    Toast.makeText(view.getContext(), "User is deleted", Toast.LENGTH_SHORT).show();
                    //back to list
                    Intent openList2 = new Intent(getApplicationContext(), Vision.class);
                    startActivity(openList2);
                    dbHelper.close();
                }
            });

        }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {

        ImageView imageView;

        public DownLoadImageTask (ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
    }

