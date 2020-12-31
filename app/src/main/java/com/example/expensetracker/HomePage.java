package com.example.expensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigation;
    private FrameLayout frameLayout;

    //Fragement
    private DashBoardFragment dashBoardFragment;
    private IncomeFragment incomeFragment;
    private ExpensesFragment expensesFragment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        bottomNavigation=findViewById(R.id.bottomNavigationbar);
        Toolbar toolbar=findViewById(R.id.my_toolbar);

        toolbar.setTitle("Expense Tracker");
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);
        dashBoardFragment=new DashBoardFragment();
        incomeFragment=new IncomeFragment();
        expensesFragment=new ExpensesFragment();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        setFragment(dashBoardFragment);
                        bottomNavigation.setItemBackgroundResource(R.color.colorAccent);
                        return true;
                    case R.id.income:
                        setFragment(incomeFragment);
                        bottomNavigation.setItemBackgroundResource(R.color.incomeColor);
                        return true;
                    case R.id.expenses:
                        setFragment(expensesFragment);
                        bottomNavigation.setItemBackgroundResource(R.color.expenseColor);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
//        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
//            drawerLayout.closeDrawer(GravityCompat.END);
//       }else {
//            super.onBackPressed();
//        }
//    }
    public void displaySelectedListener(int itemId){
        Fragment fragment=null;

        switch (itemId){
            case R.id.dashboard:

                fragment=new DashBoardFragment();
                break;
            case R.id.income:
                fragment=new IncomeFragment();
                break;
            case R.id.expenses:
                fragment=new ExpensesFragment();
                break;
            case R.id.signout:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Want To Logout?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomePage.this,MainActivity.class));

                        finish();

                    }
                });
                builder.setNegativeButton("NO",null).create().show();


                break;

        }
        if(fragment!=null){
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame,fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}
