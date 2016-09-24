package com.example.anusha.library;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anusha.library.gcm.RegistrationIntentService;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static final int LOCATION_PERMISSION = 780;
    static boolean value = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestPermission();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_userfirstname = (TextView)hView.findViewById(R.id.firstname);
        TextView nav_userlastname = (TextView)hView.findViewById(R.id.lastname);
        ImageView nav_userphoto = (ImageView)hView.findViewById(R.id.userpic);
        nav_userfirstname.setText(Sharedpreference.get_data_from_sharedpreference(this,"firstname",""));
        nav_userlastname.setText(Sharedpreference.get_data_from_sharedpreference(this,"lastname",""));
        Picasso.with(this)
                .load(Sharedpreference.get_data_from_sharedpreference(this,"photo","http://www.freedigitalphotos.net/images/img/homepage/87357.jpg"))
                .placeholder(R.drawable.abc_ab_share_pack_mtrl_alpha)
                .resize(250, 250)
                .error(R.drawable.errortriangle)
                .transform(new CircleTransform())
                .into(nav_userphoto);
        startService(new Intent(MainActivity.this,RegistrationIntentService.class));
       // Picasso.with(this).load(mayorShipImageLink).transform(new CircleTransform()).into(ImageView);
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, ProfileView.newInstance()).addToBackStack("HomeScreen").commit();
            // Handle the camera action
        } else if (id == R.id.nav_orderbook) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, Fragmentfirst.newIntsance("hello")).commit();
        } else if (id == R.id.nav_renew) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, bookRenew.newIntsance("hello")).commit();
        } else if (id == R.id.nav_return) {

        } else if (id == R.id.nav_findlibrary) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, new MapFragment()).addToBackStack("HomeScreen").commit();

        }
        else if(id == R.id.Log_Out){
            value = create_dialog_display();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean create_dialog_display(){
        AlertDialog.Builder alert= new AlertDialog.Builder(this);

        alert.setTitle("LogOut");
        alert.setMessage("would you like to log out");
        alert.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Sharedpreference.clear_data(MainActivity.this);
                finish();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                //onBackPressed();
                value = true;
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                value = false;
            }
        });
        alert.show();
        return value;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //openFilePicker();
        }
        if(requestCode == LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);
        } /*else {
            //openFilePicker();
            //requestPermission();
        }*/
    }
}
