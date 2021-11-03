package com.example.siapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOfPerson extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Button back;
    private ListView founded_persons;
    private ArrayList<Personal> listForAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_person);

        back = findViewById(R.id.back_to_search_add1);
        founded_persons = findViewById(R.id.founded_persons);
        founded_persons.setOnItemClickListener(this);


        Intent intent = getIntent();
        String answerFromSearch = intent.getStringExtra("answer");
            try {
                JSONObject jb = new JSONObject(answerFromSearch);
                JSONArray ja = jb.getJSONArray("arr");
                for (int i = 0; i < ja.length(); i++) {
                    Personal pers = new Personal();
                    JSONObject tmpObj = ja.getJSONObject(i);
                    pers.add(tmpObj);
                    listForAdapter.add(pers);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdapterListOfPersons adapter = new AdapterListOfPersons(ListOfPerson.this, listForAdapter);
                founded_persons.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(founded_persons)) {
            String info_pers = listForAdapter.get(position).name;
            String str_true_number;
            long huge_number;
            long true_number;
            long left, right;
            if (!listForAdapter.get(position).codekey.equals("null")) {
                huge_number = Long.parseLong(listForAdapter.get(position).codekey);
                huge_number >>= 32;
                left = (huge_number >> 16) & 0xFF;
                right = huge_number & 0xFFFF;
                true_number = left * 100000 + right;
                str_true_number = String.valueOf(true_number);
                listForAdapter.get(position).codekey = str_true_number;
            } else
                listForAdapter.get(position).codekey = "null";
            String url = MainActivity.url + "search_group_name.php";
            JSONObject json = new JSONObject();
            try {
                json.put("group", listForAdapter.get(position).parent_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyPost post = new MyPost();
            final String[] group = new String[1];
            post.dowork(url, json.toString(), new ICallback() {
                @Override
                public void callback(String answer) {
                    try {
                        JSONObject jb = new JSONObject(answer);
                        JSONArray ja = jb.getJSONArray("arr");
                        listForAdapter.get(position).parent_group = ja.get(0).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(listForAdapter.get(position).parent_group);
                    if (info_pers != null) {
                        Intent intentupdating = new Intent(ListOfPerson.this, Updating.class);
                        intentupdating.putExtra("id", listForAdapter.get(position).id);
                        intentupdating.putExtra("name", listForAdapter.get(position).name);
                        intentupdating.putExtra("sms_targetnumber", listForAdapter.get(position).sms_targetnumber);
                        intentupdating.putExtra("status", listForAdapter.get(position).status);
                        intentupdating.putExtra("codekey", listForAdapter.get(position).codekey);
                        intentupdating.putExtra("parent_group", listForAdapter.get(position).parent_group);
                        startActivity(intentupdating);
                    }
                }
            });



        }
    }
}

