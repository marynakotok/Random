package com.example.marina.random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;


public class myListView extends ArrayAdapter<String>{


    private String [] name;
    private String [] localization;
    private String username;
    private String password;
    private String [] gender;
    private String nationality;
    private String [] registered;
    private String [] picture;
    private int id;

    Context context;


    public myListView(@NonNull Context context, String name[], String gender[],
                      String registered[], String localization[], String picture[]) {
        super(context, R.layout.items_style);

        this.name = name;
        this.gender = gender;
        this.registered = registered;
        this.picture = picture;
        this.localization = localization;
//        this.nationality = nationality;
//        this.username = username;
//        this.password = password;
//        this.id = id;
        this.context = context;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_style, parent,false);
            viewHolder.name_user = (TextView) convertView.findViewById(R.id.name);
            viewHolder.photo_user = (ImageView) convertView.findViewById(R.id.photo_user);
            viewHolder.date_user = (TextView) convertView.findViewById(R.id.date);
            viewHolder.gender_user = (TextView) convertView.findViewById(R.id.gender);
            viewHolder.localization_user = (Button) convertView.findViewById(R.id.location);

            convertView.setTag(viewHolder);
        } else
            {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            viewHolder.name_user.setText(name[position]);

            new DownLoadImageTask(viewHolder.photo_user).execute(picture[position]);

            viewHolder.date_user.setText(registered[position]);
            viewHolder.gender_user.setText(gender[position]);
            viewHolder.localization_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openSee = new Intent(context.getApplicationContext(), ForMap.class);
                    context.startActivity(openSee);
                    //localization
                    //Toast.makeText(context, "Gonna be map", Toast.LENGTH_SHORT).show();
                }
            });

        return convertView;
    }

    static class ViewHolder{

        TextView name_user;
        ImageView photo_user;
        TextView date_user;
        TextView gender_user;
        Button localization_user;
    }


public class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {

    ImageView imageView;

    public DownLoadImageTask(ImageView imageView){
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

