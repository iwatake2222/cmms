package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayRequestActivity extends BaseActivity {
    public static final String EXTRA_REQUEST = "ca.on.conestogac.cmms.EXTRA_REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // for debug
        String request = getIntent().getStringExtra(EXTRA_REQUEST);
        if (request == null) {
            Utility.logError("unexpedted call");
            throw new IllegalArgumentException();
        } else {
            try{
                WorkRequest workRequest = new WorkRequest(new JSONObject(request));
                Utility.logDebug(workRequest.toString());
            } catch (JSONException e) {
                Utility.logError(e.getMessage());
            }
        }
    }

    @Override
    void onAPIResponse(String jsonString) {

    }

    public void onClickCreateMaintenanceLog(View view) {
        Intent intent = new Intent(this, DisplayMaintenanceLogActivity.class);
        startActivity(intent);
    }
}
