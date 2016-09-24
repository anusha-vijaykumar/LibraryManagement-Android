package com.example.anusha.library.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anusha.library.ItemDetails;
import com.example.anusha.library.MainActivity;
import com.example.anusha.library.Modelitem;
import com.example.anusha.library.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anusha on 7/18/2016.
 */

// get all the books from the database with one service call and then filter them in an arraylist based on what has been entered in the search bar
// writing sepearte method to filter becoz hav extended baseadapter if use arrayadapter not required
public class Adapter extends BaseAdapter implements Filterable{

    ValueFilter valueFilter;
    ArrayList<Modelitem> originalList = new ArrayList<Modelitem>();
    Context c;
    ArrayList<Modelitem> filterList=new ArrayList<Modelitem>();

    // constructor to link adapter and arraylist items
    public Adapter(Context context, ArrayList arrayList)
    {
        filterList=arrayList;
        this.c = context;
        this.originalList = arrayList;
        getFilter();
    }



    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filterList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.listviewmenu,parent,false);

        final Viewholder v = new Viewholder(convertView);
        final Modelitem modelitem = (Modelitem) filterList.get(position);
        v.Booktitle.setText(modelitem.getBooktitle());
        Picasso.with(c)
                .load(modelitem.getBookimage())
                .placeholder(R.drawable.abc_ab_share_pack_mtrl_alpha)
                .error(R.drawable.errortriangle)
                .into(v.Bookimage);
        //Picasso.with(c).load(modelitem.getBookimage()).into(v.Bookimage);
        v.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = ((MainActivity)c).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, ItemDetails.newInstance( modelitem.getBooktitle(),modelitem.getAuthor())).addToBackStack("Fragment1").commit();

            }
        });
        v.Price.setText(modelitem.getAuthor());
        v.Genre.setText(modelitem.getGenre());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(valueFilter == null)
        valueFilter = new ValueFilter();

        return valueFilter;
    }


class Viewholder {

    TextView Booktitle;
    ImageView Bookimage;
    TextView Price;
    TextView Genre;
    ImageView arrow;

    Viewholder(View v) {

        Bookimage = (ImageView) v.findViewById(R.id.list_image);
        Booktitle = (TextView) v.findViewById(R.id.title);
        Price = (TextView) v.findViewById(R.id.price);
        Genre = (TextView) v.findViewById(R.id.genre);
        arrow  = (ImageView)v.findViewById(R.id.arrow);
    }
}
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();

            if(constraint!=null && constraint.length()>0){
                List<Modelitem> nfilterList = new ArrayList<Modelitem>();
                    for (Modelitem contacts : originalList) {
                    if((contacts.getBooktitle().toUpperCase())
                            .contains(constraint.toString().toUpperCase())  || (contacts.getAuthor().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        nfilterList.add(contacts);
                    }
                }
                results.count=nfilterList.size();
                results.values=nfilterList;
            }else{
                results.count=originalList.size();
                results.values=originalList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                filterList = (ArrayList<Modelitem>) results.values;
                notifyDataSetChanged();
            }
        }


}
}

