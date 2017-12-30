package com.example.myapp.mealplanner.Activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapp.mealplanner.Fragment.FoodMenuFrag;
import com.example.myapp.mealplanner.Fragment.FoodTypeTableFrag;
import com.example.myapp.mealplanner.Fragment.HomeFrag;
import com.example.myapp.mealplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomeFrag.OnFragmentInteractionListener {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //Fireapp Authentication
        mAuth = FirebaseAuth.getInstance();

        //Toolbar Set Up
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Float button Set Up
        fab = (FloatingActionButton) findViewById(R.id.fabBtn_main_Act);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createMenuIntent = new Intent(MainActivity.this, CreateMenuFragCtn.class);
                startActivity(createMenuIntent);
            }
        });

        setMainDefaultFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navDrawer_main_Act);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendUserToStart();
        }
    }

    private void sendUserToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_logout_btn:
                FirebaseAuth.getInstance().signOut();
                sendUserToStart();
                return true;

            case R.id.main_view_menu_btn:
                Intent createMenuIntent = new Intent(MainActivity.this, CreateMenuFragCtn.class);
                startActivity(createMenuIntent);
                return true;

            /*case R.id.action_addMenu:
            case R.id.action_editMenu:
            case R.id.custom_logout_btn:
            case R.id.action_addNewDish:
                return false;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction;
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.nav_home:
                setMainDefaultFragment();
                break;

            case R.id.nav_favourite:
                FoodMenuFrag foodFavouriteFragment = new FoodMenuFrag();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frag_main_Act, foodFavouriteFragment, "FoodFavouriteFragment");
                transaction.commit();
                break;

            case R.id.nav_plan:
                FoodMenuFrag foodPlanFragment = new FoodMenuFrag();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frag_main_Act, foodPlanFragment, "FoodPlanFragment");
                transaction.commit();
                break;

            case R.id.nav_list:
                FoodTypeTableFrag foodTypeTableFrag = new FoodTypeTableFrag();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frag_main_Act, foodTypeTableFrag, "FoodPlanFragment");
                transaction.commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void setMainDefaultFragment() {
        HomeFrag fragDefault = new HomeFrag();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_main_Act, fragDefault, "DefaultFrag");
        transaction.commit();
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
