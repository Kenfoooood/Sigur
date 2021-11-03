package com.example.siapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Add_person extends AppCompatActivity {

    private EditText enterName;
    private EditText enterNameOfGroup;
    private Button add;
    private Button back;
    private ProgressDialog progressDialog3;
    private String parent_id = "";
    private String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        enterName = findViewById(R.id.enter_name_of_person);
        enterNameOfGroup = findViewById(R.id.enter_name_of_thegroup);
        add = findViewById(R.id.add);
        back = findViewById(R.id.back_to_search_add2);
        String urladdperson = MainActivity.url + "add_person.php";
        String searchGroup = MainActivity.url + "searchGroup.php";

        progressDialog3 = new ProgressDialog(this);
        progressDialog3.setCancelable(false);
        progressDialog3.setTitle("Добавление пользователя...");
        progressDialog3.setMessage("Пожалуйста, подождите");



        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                progressDialog3.show();
                Handler handler = new Handler();
                MyPost postOnClick = new MyPost();
                JSONObject json1 = new JSONObject();
                try {
                    json1.put("group", enterNameOfGroup.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postOnClick.dowork(searchGroup, json1.toString(), new ICallback() {
                    @Override
                    public void callback(String answer) {
                        System.out.println(answer);
                        if (!answer.equals("{\"arr\":null}")) {
                            try {
                                JSONObject jb = new JSONObject(answer);
                                JSONArray ja = jb.getJSONArray("arr");
                                parent_id = ja.get(0).toString();
                                System.out.println(parent_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog3.dismiss();
                                            Toast.makeText(Add_person.this, "Такой группы нет", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }, 100);
                                }
                            });
                        }
                        System.out.println("parent_id: " + parent_id);
                        if (!parent_id.isEmpty()) {
                            JSONObject json2 = new JSONObject();
                            name = enterName.getText().toString();
                            try {
                                json2.put("cid", MainActivity.cid);
                                json2.put("cmd", 0);
                                json2.put("parent_id", parent_id);
                                json2.put("name", name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println(json2.toString());
                            MyPost postAdd = new MyPost();
                            postAdd.dowork(urladdperson, json2.toString(), new ICallback() {
                                @Override
                                public void callback(String answer) {
                                    System.out.println("added");
                                }
                            });
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog3.dismiss();
                                    Toast.makeText(Add_person.this, "Сотрудник добавлен", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }, 120000);
                        }
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
    }

    @Override
    public void onBackPressed() {

    }
}

