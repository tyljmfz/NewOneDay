
package com.example.newoneday;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.newoneday.friend.activity.NewFriendListActivity;
import com.example.newoneday.friend.activity.SearchFriendActivity;
import com.example.newoneday.goal.activity.CreateGoalActivity;
import com.example.newoneday.goal.activity.GoalManagementActivity;

public class NavSelectActivity extends AppCompatActivity {

    private TextView userNikeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_select);
        NavigationView navigationView = findViewById(R.id.nav_view);
        userNikeName = findViewById(R.id.username);
        if(!TextUtils.isEmpty(SignInActivity.userNikeName)){
            userNikeName.setText(SignInActivity.userNikeName);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_one:
                        openActivity(MainActivity.class);
                        Log.d("testOpen","main");
                        break;
                    case R.id.nav_two:
                        openActivity(SignInActivity.class);
                        Log.d("testOpen","Wish");
                        break;
                    case R.id.nav_three:
                        openActivity(CreateGoalActivity.class);
                        break;
                    case R.id.nav_four:
                        openActivity(GoalManagementActivity.class);
                        break;

                    case R.id.nav_five:
                        openActivity(SearchFriendActivity.class);
                        break;

                    case R.id.nav_six:
                        openActivity(NewFriendListActivity.class);
                        break;
//                    case R.id.nav_seven:
//                        openActivity(testActivity.class);
//                        break;
                    default:
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_menu,menu);
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.nav_one:
                openActivity(MainActivity.class);
                break;
            case R.id.nav_two:
                openActivity(SignInActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private <T extends FragmentActivity> void openActivity(Class<T> activity){
        startActivity(new Intent(this, activity));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


}
