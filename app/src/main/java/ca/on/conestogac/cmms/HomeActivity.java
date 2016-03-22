package ca.on.conestogac.cmms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

    public void onClickNews(View view) {
    }

    public void onClickAbout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.text_about)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
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

        changeViewVisibilityByUserPriviledge();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void changeViewVisibilityByUserPriviledge(){
        if(User.getInstance().canDisplayMachine()){
            ((Button)findViewById(R.id.buttonHomeSearchMachine)).setEnabled(true);
        } else {
            ((Button)findViewById(R.id.buttonHomeSearchMachine)).setEnabled(false);
        }
        if(User.getInstance().canDisplayWorkRequest()){
            ((Button)findViewById(R.id.buttonHomeSearchRequest)).setEnabled(true);
        } else {
            ((Button)findViewById(R.id.buttonHomeSearchRequest)).setEnabled(false);
        }
    }
}
