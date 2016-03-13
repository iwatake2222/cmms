package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        callAPI("logout", jsonParam);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    void onAPIResponse(String jsonString) {

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
}
