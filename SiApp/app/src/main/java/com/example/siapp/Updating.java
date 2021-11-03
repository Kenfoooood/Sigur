package com.example.siapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Updating extends AppCompatActivity {

    private EditText name;
    private EditText phone_number;
    private EditText pass_number;
    private EditText group;
    private RadioGroup radioGroup;
    private RadioButton activate;
    private RadioButton fire;
    private Button save;
    private Button back;

    Personal pers_up = new Personal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating);

        name = findViewById(R.id.name_text);
        phone_number = findViewById(R.id.tabid_text);
        pass_number = findViewById(R.id.codekey_text);
        group = findViewById(R.id.group_text);
        radioGroup = findViewById(R.id.radio_group_activasion);
        activate = findViewById(R.id.activate);
        fire = findViewById(R.id.fire);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back_to_list);

        Intent intent = getIntent();
        pers_up.id = intent.getStringExtra("id");
        pers_up.name = intent.getStringExtra("name");
        pers_up.sms_targetnumber = intent.getStringExtra("sms_targetnumber");
        pers_up.status = intent.getStringExtra("status");
        pers_up.codekey = intent.getStringExtra("codekey");
        pers_up.parent_group = intent.getStringExtra("parent_group");




        name.setText(pers_up.name);
        phone_number.setText(pers_up.sms_targetnumber);
        pass_number.setText(pers_up.codekey);
        group.setText(pers_up.parent_group);

        if (pers_up.status.equals("AVAILABLE")) {
            activate.setChecked(true);
        }
        if (pers_up.status.equals("FIRED") || pers_up.codekey.equals("null")) {
            fire.setChecked(true);
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fire.isChecked()) {
                    pers_up.status = "FIRED";
                }
                else {
                    pers_up.status = "AVAILABLE";
                }

                String urlupdate = MainActivity.url + "update_person.php";
                MyPost postup = new MyPost();
                JSONObject jsonup = new JSONObject();
                int key = Integer.parseInt(pass_number.getText().toString());
                long left = key/100000;
                long right = key%100000;
                long digit = (((0x18 << 8) + left << 16) + right) << 32;
                String str_true_number = String.valueOf(digit);
                try {
                    jsonup.put("cid", MainActivity.cid);
                    jsonup.put("cmd", 1);
                    jsonup.put("id", pers_up.id);
                    jsonup.put("name", name.getText());
                    jsonup.put("sms_targetnumber", phone_number.getText());
                    jsonup.put("status", pers_up.status);
                    jsonup.put("codekey", str_true_number);
                    System.out.println(jsonup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postup.dowork(urlupdate, jsonup.toString(), new ICallback() {
                    @Override
                    public void callback(String answer) {
                        System.out.println(answer);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Updating.this, "Запись о сотруднике/учащемся обновлена", Toast.LENGTH_LONG).show();
                            }
                        });
                        finish();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setEnabled(MainActivity.isAdmin);
    }

    @Override
    public void onBackPressed() {

    }

}