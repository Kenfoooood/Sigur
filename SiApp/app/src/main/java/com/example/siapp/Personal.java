package com.example.siapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Personal {
    public String id = "";
    public String parent_id = "";
    public String type = "";
    public String name = "";
    public String description = "";
    public String sms_targetnumber = "";
    public String status = "";
    public String codekey = "";
    public String parent_group = "";

    public void add(JSONObject info) {
        try {
            this.id = info.getString("ID");
            this.parent_id = info.getString("PARENT_ID");
            this.name = info.getString("NAME");
            this.sms_targetnumber = info.getString("SMS_TARGETNUMBER");
            this.status = info.getString("STATUS");
            this.codekey = info.getString("CODEKEY");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

