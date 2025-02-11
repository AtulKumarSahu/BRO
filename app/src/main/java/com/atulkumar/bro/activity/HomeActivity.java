package com.atulkumar.bro.activity;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.atulkumar.bro.R;
import com.atulkumar.bro.fragment.ChatListFragment;
import com.atulkumar.bro.fragment.HomeFragment;
import com.atulkumar.bro.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         navigationView=findViewById(R.id.nav_bottom);
         replaceFragment(new HomeFragment());
         navigationView.setOnItemSelectedListener(item -> {
             int id = item.getItemId();
             if (id==R.id.home){
                 replaceFragment(new HomeFragment());
             }
             if (id==R.id.chat){
                 replaceFragment(new ChatListFragment());
             }
             if (id==R.id.profile){
                 replaceFragment(new ProfileFragment());
             }
             return false;
         });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_open,R.anim.anim_out)
                .replace(R.id.main_container,fragment).commit();

    }
    public void hideBottomNavigation() {
        if (navigationView != null) {
            navigationView.setVisibility(View.GONE);
        }
    }
    public void showBottomNavigation() {
        if (navigationView != null) {
            navigationView.setVisibility(View.VISIBLE);
        }
    }
}