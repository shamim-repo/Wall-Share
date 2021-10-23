package com.msbshamim60.wallshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.PopupMenu;

import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    private SmoothBottomBar bottomBar;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar=findViewById(R.id.bubbleTabBar);

        if (isUIModeNight()){
            bottomBar.setBarBackgroundColor(getResources().getColor(R.color.theme_dark_color_for_background));
            bottomBar.setItemIconTint(getResources().getColor(R.color.white));
            bottomBar.setBarIndicatorColor(getResources().getColor(R.color.theme_light_color_for_surface));
        }else {
            bottomBar.setBarBackgroundColor(getResources().getColor(R.color.theme_light_color_for_background));
            bottomBar.setItemIconTint(getResources().getColor(R.color.black));
            bottomBar.setBarIndicatorColor(getResources().getColor(R.color.theme_dark_color_for_surface));
        }

        navController = Navigation.findNavController(this,R.id.nav_host_fragment_activity_main);
        setupSmoothBottomMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void  setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.bottom_nav_menu);
        Menu menu = popupMenu.getMenu();
        bottomBar.setupWithNavController(menu, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private Boolean isUIModeNight(){
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_NO:
                return false;
            default:return true;
        }
    }
}