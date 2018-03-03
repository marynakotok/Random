package com.example.marina.random;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Insertion extends AppCompatActivity implements View.OnClickListener {

    private Button add;
    private RadioGroup genderGroup;
    private EditText number_users, reg_date;
    private TextView error;
    private DatePickerDialog datePickerDialog;
    private Spinner nat;
    String selectedNationality;
    int number_users_int;
    String number_users_string ;
    String reg_date_string;
    String gender;

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertion);
        bindViews();
        setViewActions();
        PrepareDatePickerDialog();
        natList();
        dbHelper = new DBHelper(this);

    }

    private void natList() {
        List<String> nationalities = new ArrayList<String>();

        nationalities.add("");  nationalities.add("FR");
        nationalities.add("AU");nationalities.add("GB");
        nationalities.add("BR");nationalities.add("IE");
        nationalities.add("CA");nationalities.add("IR");
        nationalities.add("CH");nationalities.add("NL");
        nationalities.add("DK");nationalities.add("NZ");
        nationalities.add("ES");nationalities.add("TR");
        nationalities.add("FI");nationalities.add("US");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nationalities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nat.setAdapter(dataAdapter);
    }

    private void bindViews() {
        add = (Button) findViewById(R.id.add);
        genderGroup = (RadioGroup) findViewById(R.id.gender_radiogroup);
        number_users = (EditText) findViewById(R.id.amount_users);
        reg_date = (EditText) findViewById(R.id.reg_date);
        error = (TextView) findViewById(R.id.error);
        nat = (Spinner) findViewById(R.id.nationality);
    }

    private void setViewActions() {
        add.setOnClickListener(this);
        reg_date.setOnClickListener(this);
        nat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ "selected", Toast.LENGTH_SHORT).show();
                selectedNationality = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.reg_date:
                datePickerDialog.show();
                break;
            case R.id.add:
                add();
                break;
        }
    }

    private void PrepareDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                reg_date.setText(year + "-" + month + "-" + day);
                datePickerDialog.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void add() {

        int counterError = 0;
        String errorActual = " ";
        error.setText(" ");
        //getting every single field as String or int
        number_users_int = Integer.parseInt(number_users.getText().toString());
        number_users_string = number_users.getText().toString();
        reg_date_string = reg_date.getText().toString();
        RadioButton selectedRadioButton = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());
        gender = selectedRadioButton == null ? "" : selectedRadioButton.getText().toString();
//        Log.v("gender : ", gender);
//        Log.v("spinner : ", selectedNationality);
//        Log.v("number : ", number_users_string);
//        Log.v("reg_date : ", reg_date_string);

        //TODO fix the form - problem with number field doesn't check for empty field
         if (number_users_string.matches(" "))
         {
             counterError++;
             errorActual = "Please, provide number of users!\n";
         }
            if (number_users_int > 100 || number_users_int < 1 )
                {
                    counterError++;
                    errorActual = "Number of users must be between 1 and 100!\n";
                }

        if (counterError>0)
        {
            error.setText(errorActual);
        }
        else {
            getJson();
            Toast.makeText(this, "The users are added", Toast.LENGTH_SHORT).show();
            Intent openInsert = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(openInsert);
        }
    }

    private void getJson() {
        new BackgroundTask().execute();
    }


    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String json_url;
        String JSON_STRING;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            json_url = "https://randomuser.me/api/?results=" + number_users_string + "&gender=" +
                    gender + "&registered=" + reg_date_string + "&nat=" + selectedNationality +
                    "&inc=name,gender,registered,picture,location,login,nat";
            Log.v("url: ", json_url);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //Here result is the JSON code of generated users
            json_string = result;
            Log.v("json_string", json_string);

            if (json_string == null) {
                //Toast.makeText(getApplicationContext(), "No Information", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(), "Exist Information", Toast.LENGTH_LONG).show();}
                try {

                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("results");
                    int count = 0;
                    String gender, name, localization, username, password, registered, picture, nat;
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();

                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);

                        gender = JO.getString("gender");
                        registered = JO.getString("registered");
                        nat = JO.getString("nat");
                        name = JO.getJSONObject("name").getString("first");
                        localization = JO.getJSONObject("location").getString("city");
                        username = JO.getJSONObject("login").getString("username");
                        password = JO.getJSONObject("login").getString("password");
                        picture = JO.getJSONObject("picture").getString("thumbnail");

                        Log.v("data", gender + registered + nat + name + localization + username + password + picture);
                        //DATABASE
                        //setting data to database
                        contentValues.put(DBHelper.KEY_GENDER, gender);
                        contentValues.put(DBHelper.KEY_NAME, name);
                        contentValues.put(DBHelper.KEY_LOCALIZATION, localization);
                        contentValues.put(DBHelper.KEY_NAT, nat);
                        contentValues.put(DBHelper.KEY_PASSWORD, password);
                        contentValues.put(DBHelper.KEY_USERNAME, username);
                        contentValues.put(DBHelper.KEY_PICTURE, picture);
                        contentValues.put(DBHelper.KEY_REGISTERED, registered);

                        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dbHelper.close();
            }

        }

    }
}
