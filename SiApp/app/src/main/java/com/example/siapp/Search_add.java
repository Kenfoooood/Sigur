package com.example.siapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search_add extends AppCompatActivity {

    private TextView heading2;
    private EditText info;
    private Button searching;
    private Button addPersonal;
    private Button quit;
    private ProgressDialog progressDialog2;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add);

        info = findViewById(R.id.infodata);
        searching = findViewById(R.id.searching);
        addPersonal = findViewById(R.id.add_personal);
        quit = findViewById(R.id.quit);
        radioGroup = findViewById(R.id.radio_group);

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setCancelable(false);
        progressDialog2.setTitle("Поиск...");
        progressDialog2.setMessage("Пожалуйста, подождите");

        searching.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                String urlinfo = MainActivity.url + "searching_person_from_phone.php";
                JSONObject json = new JSONObject();
                MyPost postOnClick = new MyPost();
                progressDialog2.show();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.select_search_by_name:
                        try {
                            json.put("radioId", 1);
                            json.put("data", info.getText());
                            json.put("cid", MainActivity.cid);
                            System.out.println("json строка с именем:");
                            System.out.println(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.select_search_by_number:
                        try {
                            json.put("radioId", 2);
                            json.put("data", info.getText());
                            json.put("cid", MainActivity.cid);
                            System.out.println("json строка с именем:");
                            System.out.println(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.select_search_by_cardNumber:
                        String str = info.getText().toString();
                        int key = 0;
                        try {
                            key = Integer.parseInt(str);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Search_add.this, "Введите число", Toast.LENGTH_LONG).show();
                            progressDialog2.dismiss();
                            return;
                        }

                        try {
                            long left = key/100000;
                            long right = key%100000;
                            long digit = (((0x18 << 8) + left << 16) + right) << 32;
                            //String strdigit = String.valueOf(digit);
                            json.put("radioId", 3);
                            json.put("data", digit);
                            json.put("cid", MainActivity.cid);
                            System.out.println("json строка с именем:");
                            System.out.println(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                postOnClick.dowork(urlinfo, json.toString(), new ICallback() {
                    @Override
                    public void callback(String answer) {
                        System.out.println("ОТВЕТ ОТ СЕРВЕРА" + answer);
                        Intent intentlist = new Intent(Search_add.this, ListOfPerson.class);
                        intentlist.putExtra("answer", answer);
                        progressDialog2.dismiss();
                        if (!answer.equals("{\"arr\":[]}")) {
                            startActivity(intentlist);
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Search_add.this, "Такого студента или сотрудника нет", Toast.LENGTH_LONG).show();
                                    progressDialog2.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });


        addPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnext = new Intent(Search_add.this, Add_person.class);
                startActivity(intentnext);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPersonal.setEnabled(MainActivity.isAdmin);

    }

    @Override
    public void onBackPressed() {

    }
}