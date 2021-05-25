package com.example.sep4_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sep4_android.viewmodels.MainActivityViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private MainActivityViewModel viewModel;
    private final String CHANNEL_ID = "misc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main_app);
        checkIfSignedIn();
        setNavigationViewListener();
        createNotificationChannel();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_logout: {
                viewModel.signOut();
            }
        }
        return true;
    }

    private void checkIfSignedIn() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                viewModel.getUserStatus(user.getUid()).observe(this, status -> {
                    prepareToolbar(status.isStatus());
                    setNavigationHeader();
                });

            } else
                startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void prepareToolbar(boolean status) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment_main);
        NavController navController = navHostFragment.getNavController();
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main_app_graph);
        if (status) {
            navGraph.setStartDestination(R.id.gardenerHomepageFragment);
        } else {
            navGraph.setStartDestination(R.id.assistantHomepageFragment);
        }

        navController.setGraph(navGraph);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setNavigationHeader() {
        View header = navigationView.getHeaderView(0);
        TextView headerName = header.findViewById(R.id.nav_header_name);
        ImageView headerAvatar = header.findViewById(R.id.nav_header_avatar);

        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                headerName.setText(user.getDisplayName());
                Glide.with(this).load(user.getPhotoUrl()).into(headerAvatar);
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}