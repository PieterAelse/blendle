package com.piotapps.blendle;

import android.content.ComponentName;
import android.os.Bundle;
import android.provider.Settings;

import com.piotapps.blendle.fragments.PopularTodayFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMessage("Welcome! This is Blendle by Pieter Otten"); // TODO extract

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragmentHolder, PopularTodayFragment.newInstance())
                .commit();

        getDreamComponentsForUser();
    }

    private ComponentName[] getDreamComponentsForUser() {
        String names = Settings.Secure.getString(getContentResolver(), "screensaver_components");
        return names == null ? null : componentsFromString(names);
    }

    private static ComponentName[] componentsFromString(String names) {
        String[] namesArray = names.split(",");
        ComponentName[] componentNames = new ComponentName[namesArray.length];
        for (int i = 0; i < namesArray.length; i++) {
            componentNames[i] = ComponentName.unflattenFromString(namesArray[i]);
        }
        return componentNames;
    }
}
