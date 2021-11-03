package com.example.siapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener, View.OnClickListener {

    private Spinner colleges;
    private EditText login;
    private EditText password;
    private Button enter;
    private Button quit;
    private ProgressDialog progressDialog;
    public static boolean isAdmin = false;

    public static int cid = 1;
    public static String url = "http://smsinfb9.beget.tech/";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colleges = findViewById(R.id.colleges);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        enter = findViewById(R.id.enter);
        quit = findViewById(R.id.quit_destroy);

        quit.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Соединение...");
        progressDialog.setMessage("Пожалуйста, подождите");

        String urlcombobox = url + "comboboxinfo.php";

        MyPost post = new MyPost();
        post.dowork(urlcombobox, "", (String answer) -> {
            ArrayList<String> data = new ArrayList<>();
            try {
                JSONObject jb = new JSONObject(answer);
                JSONArray ja = jb.getJSONArray("arr");
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray x = ja.getJSONArray(i);
                    for (int j = 0; j < 1; j++) {
                        data.add(x.get(1).toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(data);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    colleges.setAdapter(adapter);
                    colleges.setSelection(0);
                    colleges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cid = 1 + colleges.getSelectedItemPosition();
                            //Toast.makeText(getBaseContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            System.out.println(cid);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login1 = login.getText().toString();
                String password1 = password.getText().toString();
                String urlCompare = url + "comparelogpass.php";
                JSONObject json = new JSONObject();
                try {
                    json.put("cid", cid);
                    json.put("login", login1);
                    json.put("password", password1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String enterInfo = json.toString();
                System.out.println(enterInfo);
                progressDialog.show();
                MyPost postOnClick = new MyPost();
                postOnClick.dowork(urlCompare, enterInfo, new ICallback() {
                    @Override
                    public void callback(String answer) {
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (answer.equals("0")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this,"Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                                }
                                if (answer.equals("1")) {
                                    isAdmin = false;
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, Search_add.class);
                                    startActivity(intent);
                                }
                                if (answer.equals("2")) {
                                    isAdmin = true;
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, Search_add.class);
                                    startActivity(intent);
                                }
                                System.out.println(answer);
                            }
                        });
                    }
                });

            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        if (v.equals(quit)) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
