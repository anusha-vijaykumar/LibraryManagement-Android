package com.example.anusha.library.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anusha.library.BufferReaderOutput;
import com.example.anusha.library.ItemDetails;
import com.example.anusha.library.Library;
import com.example.anusha.library.MainActivity;
import com.example.anusha.library.Modelitem;
import com.example.anusha.library.R;
import com.example.anusha.library.RenewbookDone;
import com.example.anusha.library.Sharedpreference;
import com.example.anusha.library.api.RenewAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by anusha on 8/12/2016.
 */
public class RenewAdapter extends BaseAdapter {

    ArrayList<Modelitem> modelitems = new ArrayList<Modelitem>();

    Context c;

    public RenewAdapter(Context context, ArrayList<Modelitem> arrayList)
    {
        modelitems = arrayList;
        this.c = context;
      }

    @Override
    public int getCount() {
        return modelitems.size();
    }
    @Override
    public long getItemId(int position) {
        return modelitems.get(position).hashCode();
    }

    @Override
    public Object getItem(int position) {
        return modelitems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.bookrenew,parent,false);

        final Viewholder v = new Viewholder(convertView);
        final Modelitem modelitem = (Modelitem) modelitems.get(position);
        v.Booktitle.setText(modelitem.getBooktitle());
        Picasso.with(c)
                .load(modelitem.getBookimage())
                .placeholder(R.drawable.abc_ab_share_pack_mtrl_alpha)
                .error(R.drawable.errortriangle)
                .into(v.Bookimage);
        //Picasso.with(c).load(modelitem.getBookimage()).into(v.Bookimage);
        v.Renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = ((MainActivity)c).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, RenewbookDone.newInstance( modelitem.getBooktitle(),modelitem.getAuthor())).addToBackStack("Fragment1").commit();

                //service_call_renewbook(modelitem.getBooktitle(),modelitem.getAuthor());

            }
        });
        v.Author.setText(modelitem.getAuthor());
        v.Genre.setText(modelitem.getGenre());
        return convertView;
    }

    class Viewholder {

        TextView Booktitle;
        ImageView Bookimage;
        TextView Author;
        TextView Genre;
        TextView Renew;

        Viewholder(View v) {

            Bookimage = (ImageView) v.findViewById(R.id.list_image);
            Booktitle = (TextView) v.findViewById(R.id.title);
            Author = (TextView) v.findViewById(R.id.author);
            Genre = (TextView) v.findViewById(R.id.genre);
            Renew  = (TextView)v.findViewById(R.id.renew);
        }
    }


}
