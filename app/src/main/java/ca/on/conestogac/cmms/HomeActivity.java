package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends BaseActivity {

    public void onClickSearchMachine(View view) {
        Intent intent = new Intent(this, SearchMachineActivity.class);
        startActivity(intent);
    }

    public void onClickSearchRequest(View view) {
        Intent intent = new Intent(this, SearchRequestActivity.class);
        startActivity(intent);
    }


    public void onClickLogout(View view) {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        callAPI("Logout", jsonParam);

    }
    @Override
    void onAPIResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");
            if (result.compareTo(ValueConstants.RET_OK) != 0 ) {
                // do something if needed when error happens
            }
            else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // disable unnecessary menu in Login screen
        MenuItem item = menu.findItem(R.id.action_back);
        item.setVisible(false);
        item = menu.findItem(R.id.action_home);
        item.setVisible(false);
        return true;
    }
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
