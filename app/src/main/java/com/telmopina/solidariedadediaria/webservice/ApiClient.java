package com.telmopina.solidariedadediaria.webservice;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.byteshaft.solidariedadediria.account.AccountManager;
import com.byteshaft.solidariedadediria.base.BaseActivity;
import com.byteshaft.solidariedadediria.dao.UserResponse;
import com.byteshaft.solidariedadediria.sidebar_fragments.Home;
import com.byteshaft.solidariedadediria.sidebar_fragments.Institution;
import com.byteshaft.solidariedadediria.sidebar_fragments.Movements;
import com.byteshaft.solidariedadediria.sidebar_fragments.Support;
import com.byteshaft.solidariedadediria.sidebar_fragments.UserInfo;
import com.byteshaft.solidariedadediria.utils.AppGlobals;
import com.byteshaft.solidariedadediria.webservice.ApiCallbacks;
import com.byteshaft.solidariedadediria.webservice.ApiClient;
import com.byteshaft.solidariedadediria.webservice.ApiInterface;
import com.byteshaft.solidariedadediria.webservice.WebApiConstants;
import com.byteshaft.solidariedadediria.webservice.WebServiceCaller;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountManager.getInstance() != null) {
            AccountManager.getInstance().finish();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new Home());
    }

    @Override
    protected void onResume() {
        super.onResume();

        //api call to login user to get updated data everytime
        mActivity.showProgress();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));
        jsonObject.addProperty("password", AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_PASSWORD));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getUser(jsonObject);
        WebServiceCaller.CallWebApi(call, WebApiConstants.LOGIN, mActivity, this);
        System.out.println("user Id is: " + AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ID));
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);

        BroadcastReceiver screenOnOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();

                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

                if (strAction.equals(Intent.ACTION_SCREEN_OFF) || strAction.equals(Intent.ACTION_SCREEN_ON)) {
                    if (myKM.inKeyguardRestrictedInputMode()) {
                        System.out.println("Screen off " + "LOCKED");
                        if (AdActivity.getInstance() == null) {
                            startActivity(new Intent(MainActivity.this, AdActivity.class));
                        }
                    } else {
                        if (AdActivity.getInstance() == null) {
                            System.out.println("Screen off " + "UNLOCKED");
                            startActivity(new Intent(MainActivity.this, AdActivity.class));
                        }
                    }
                }
            }
        };

        getApplicationContext().registerReceiver(screenOnOffReceiver, theFilter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new Home());

        } else if (id == R.id.nav_institution) {
            loadFragment(new Institution());

        } else if (id == R.id.nav_movements) {
            loadFragment(new Movements());

        } else if (id == R.id.nav_user_info) {
            loadFragment(new UserInfo());

        } else if (id == R.id.nav_support) {
            loadFragment(new Support());
        } else if (id == R.id.nav_logout) {
            logOutDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.commit();
    }

    private void logOutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirmation!");
        alertDialogBuilder.setMessage("Do you want to logout?")
                .setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppGlobals.clearSettings();
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), AccountManager.class));
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        if (anEnum == WebApiConstants.LOGIN) {
            // api response. get data and saving in shared preferences
            Type type = new TypeToken<UserResponse>() {
            }.getType();
            UserResponse response = new Gson().fromJson(WebServiceCaller.getResponsePacket(jsonObject), type);
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, response.getUsername());
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_EMAIL, response.getEmail());
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, response.getPassword());
            AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, response.getAmount());
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_ID, String.valueOf(response.getId()));
        }
    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }
}
