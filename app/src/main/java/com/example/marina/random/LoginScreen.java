package com.example.marina.random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

public class LoginScreen extends AppCompatActivity {

    private Button login;
    private EditText username, password;
    String usernamexml, passwordHashedxml, saltxml;
    String usernameString, passwordString;
    private static final String TAG = Crypto.class.getSimpleName();
    SecretKey key;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String _phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        sharedPreferences = getSharedPreferences("com.example.marina.random", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login = (Button) findViewById(R.id.loginB);
        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);

        usernamexml = getResources().getString(R.string.username);
        saltxml = getResources().getString(R.string.salt);
        passwordHashedxml = getResources().getString(R.string.password);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });
    }

    public void login()
    {

        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        if (passwordString.length() == 4 && usernameString.length() > 0)
        {
            String passwordStringToHash = saltxml + passwordString;
            String passwordHashed = toHash(passwordStringToHash);

            editor.remove("_phrase");
            _phrase = encrypt(passwordString, passwordHashed);
            editor.putString("_phrase", _phrase);
            editor.commit();

            if (!passwordHashed.equals("-1"))
            {
                //checking if credentials are correct
                if (usernameString.equals(usernamexml) && passwordHashed.equals(passwordHashedxml))
                {
                    Intent openConfirm = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(openConfirm);
                    Toast.makeText(this, "Your credentials are correct. You are logged in!", Toast.LENGTH_LONG).show();
                } else Toast.makeText(this, "Your credentials are not valid. Try again!", Toast.LENGTH_LONG).show();
            } else Toast.makeText(this, "Error 0005: Developer knows(external)", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Password should include 4 digits and username cannot be empty!", Toast.LENGTH_LONG).show();

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

    String getRawKey() {
        if (key == null) {
            return null;
        }
        return Crypto.toHex(key.getEncoded());
    }

    public SecretKey deriveKey(String password, byte[] salt) {
        return Crypto.deriveKeyPbkdf2(salt, password);
    }

    public String encrypt(String plaintext, String password) {
        byte[] salt = Crypto.generateSalt();
        key = deriveKey(password, salt);
        Log.d(TAG, "Generated key: " + getRawKey());
        return Crypto.encrypt(plaintext, key, salt);
    }

}
