package com.example.sep4_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.viewmodels.MainActivityViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Map;
import es.dmoral.toasty.Toasty;

public class MainAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private MainActivityViewModel viewModel;
    private final String CHANNEL_ID = "misc";
    private NavController navController;
    private BadgeDrawable badgeDrawable;
    private MaterialToolbar toolbar;
    private boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main_app);
        checkIfSignedIn();
        setNavigationViewListener();
        createNotificationChannel();
        checkConnectionStatus();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_logout:
                viewModel.signOut();
                break;
            case R.id.nav_item_delete:
                showConfirmation();
                break;
            case R.id.nav_switch:
                viewModel.updateUserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid(), !status);
                status = !status;
                break;
            case R.id.nav_open_window:
                viewModel.windowAction(true);
                break;
            case R.id.nav_close_window:
                viewModel.windowAction(false);
                break;
        }
        return true;
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        viewModel.getUserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(this, status -> {
            if (status.isStatus()) {
                menu.clear();
                getMenuInflater().inflate(R.menu.main_menu, menu);
                badgeDrawable = BadgeDrawable.create(this);
                BadgeUtils.attachBadgeDrawable(badgeDrawable, toolbar, R.id.action_notification);
                loadValues();
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_notification:
                bundle.putString("listType", "requests");
                navController.navigate(R.id.assistantListFragment, bundle);
                return true;
            case R.id.action_users:
                bundle.putString("listType", "all");
                navController.navigate(R.id.assistantListFragment, bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadValues() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                viewModel.getGarden(user.getUid()).observe(this, garden -> {
                    if (garden != null) {
                        viewModel.initializeGarden(garden.getName());
                        viewModel.getLiveGarden().observe(this, liveGarden -> {
                            if (liveGarden != null) {
                                int notifications = 0;
                                for (Map.Entry<String, Boolean> entry : liveGarden.getAssistantList().entrySet()) {
                                    if (!entry.getValue()) {
                                        notifications++;
                                    }
                                }
                                badgeDrawable.setNumber(notifications);
                            }
                        });
                    }
                });
            }
        });
    }

    private void showConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.remove_account_confirmation))
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    removeAccount();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void removeAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        viewModel.removeUserFromOtherGardens(user.getUid());
        viewModel.getOwnGarden(user.getUid()).observe(this, garden -> {
            if (garden != null) {
                viewModel.getPlantsForGarden(garden.getName()).observe(this, plants -> {
                    for (Plant plant : plants) {
                        viewModel.removePlant(plant.getPlantID());
                    }
                    viewModel.removeUserStatus(user.getUid());
                    viewModel.removeGarden(garden.getName());
                });
            } else {
                viewModel.removeUserStatus(user.getUid());
                viewModel.removeUser();
                viewModel.signOut();
            }
        });
    }

    private void checkIfSignedIn() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                viewModel.getUserStatus(user.getUid()).observe(this, status -> {
                    if (status == null) {
                        startActivity(new Intent(this, LoginActivity.class));
                        return;
                    }
                    this.status = status.isStatus();
                    prepareToolbar();
                    setNavigationHeader();
                });

            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }

    private void prepareToolbar() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment_main);
        navController = navHostFragment.getNavController();

        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main_app_graph);
        if (status) { navGraph.setStartDestination(R.id.gardenerHomepageFragment); }
        else { navGraph.setStartDestination(R.id.assistantHomepageFragment); }
        navController.setGraph(navGraph);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();

        toolbar = findViewById(R.id.toolbar);
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
        TextView headerEmail = header.findViewById(R.id.nav_header_email);
        ImageView headerAvatar = header.findViewById(R.id.nav_header_avatar);

        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                headerName.setText(user.getDisplayName());
                headerEmail.setText(user.getEmail());
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

    private void checkConnectionStatus() {
        viewModel.getConnectionStatus().observe(this, status -> {
            if (status.equals(ConnectionStatus.ERROR)) {
                Toasty.error(this, getString(R.string.connection_error), Toast.LENGTH_SHORT, true).show();
            }
            if (status.equals(ConnectionStatus.SUCCESS)) {
                Toasty.success(this, getString(R.string.action_success), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}