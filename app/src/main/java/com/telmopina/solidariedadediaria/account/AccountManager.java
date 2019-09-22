package com.telmopina.solidariedadediaria.account;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.telmopina.solidariedadediaria.AdActivity;
import com.telmopina.solidariedadediaria.MainActivity;
import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.base.BaseActivity;
import com.telmopina.solidariedadediaria.utils.AppGlobals;

public class AccountManager extends BaseActivity {

    private static AccountManager sInstance;

    public static AccountManager getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppGlobals.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_account_manager);
        sInstance = this;
        loadLoginFragment(new Login());

        registerBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadLoginFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment, backStateName);
        fragmentTransaction.commit();
    }

    public void loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment, backStateName);
        FragmentManager manager = getSupportFragmentManager();
        Log.i("TAG", backStateName);
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
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
                            startActivity(new Intent(AccountManager.this, AdActivity.class));
                        }
                    } else {
                        if (AdActivity.getInstance() == null) {
                            System.out.println("Screen off " + "UNLOCKED");
                            startActivity(new Intent(AccountManager.this, AdActivity.class));
                        }
                    }
                }
            }
        };

        getApplicationContext().registerReceiver(screenOnOffReceiver, theFilter);
    }
}
