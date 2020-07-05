package com.aditya.usergithub.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.aditya.usergithub.R;
import com.aditya.usergithub.broadcast.ReminderBroadcast;
import com.aditya.usergithub.preference.AppPreference;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingActivity extends AppCompatActivity {

    Button btnLanguage;
    SwitchMaterial switchReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnLanguage = findViewById(R.id.btn_language);
        switchReminder = findViewById(R.id.switch_setting);
        ReminderBroadcast reminderBroadcast = new ReminderBroadcast();

        AppPreference appPreference = new AppPreference(this);
        Boolean notifState = appPreference.getNotifState();

        switchReminder.setChecked(notifState);

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
            }
        });

        switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reminderBroadcast.setReminder(SettingActivity.this, getString(R.string.app_name), getString(R.string.check_new));
                    Toast.makeText(getApplicationContext(), "Reminder Set On", Toast.LENGTH_SHORT).show();
                    switchReminder.setText(getString(R.string.notif_off));
                    appPreference.setNotif(true);
                } else {
                    reminderBroadcast.cancelReminder(SettingActivity.this);
                    Toast.makeText(getApplicationContext(), "Reminder Set Off", Toast.LENGTH_SHORT).show();
                    switchReminder.setText(getString(R.string.notif_on));
                    appPreference.setNotif(false);
                }
            }
        });
    }
}
