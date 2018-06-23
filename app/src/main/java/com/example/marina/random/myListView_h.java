package com.example.marina.random;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class myListView_h extends ArrayAdapter<String>{

    private String[] name;
    private String localization;
    private String[] username;
    private String[] password;
    private String[] gender;
    private String nationality;
    private String[] registered;
    private String picture;
    private int id;


    Context context;

    public myListView_h(Context context, String name[], String gender[],
                        String registered[], String username[], String password[]) {
        super(context, R.layout.items_style_h);

        this.name = name;
        this.gender = gender;
        this.registered = registered;
//        this.picture = picture;
//        this.localization = localization;
//        this.nationality = nationality;
        this.username = username;
        this.password = password;
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

        myListView_h.ViewHolder viewHolder = new myListView_h.ViewHolder();
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_style_h, parent, false);
            viewHolder.name_user = (TextView) convertView.findViewById(R.id.name);
            viewHolder.username_user = (TextView) convertView.findViewById(R.id.username);
            viewHolder.date_user = (TextView) convertView.findViewById(R.id.date);
            viewHolder.gender_user = (TextView) convertView.findViewById(R.id.gender);
            viewHolder.password_user = (TextView) convertView.findViewById(R.id.password);

            convertView.setTag(viewHolder);}
        else
            {
                viewHolder = (myListView_h.ViewHolder) convertView.getTag();
            }

        viewHolder.name_user.setText(name[position]);
        viewHolder.date_user.setText(registered[position]);
        viewHolder.gender_user.setText(gender[position]);
        viewHolder.username_user.setText(username[position]);
        viewHolder.password_user.setText(password[position]);

        return convertView;
    }

    static class ViewHolder
    {
        TextView name_user;
        TextView username_user;
        TextView date_user;
        TextView gender_user;
        TextView password_user;

    }
}
