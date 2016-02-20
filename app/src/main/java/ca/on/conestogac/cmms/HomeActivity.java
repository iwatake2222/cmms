package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_USER_LEVEL = "ca.on.conestogac.cmms.EXTRA_USER_LEVEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String userLevel = getIntent().getStringExtra(EXTRA_USER_LEVEL);
        if (userLevel == null) {
            DebugUtility.logError("unexpedted call");
            //throw new IllegalArgumentException();
        } else {
            DebugUtility.showToast(this.getApplicationContext(), "Login as  " + userLevel);
        }
    }

    public void onClickSearchMachine(View view) {
        Intent intent = new Intent(this, SearchMachineActivity.class);
        startActivity(intent);
    }

    public void onClickSearchRequest(View view) {
        Intent intent = new Intent(this, SearchRequestActivity.class);
        startActivity(intent);
    }

    public void onClickBusinessReport(View view) {
        Intent intent = new Intent(this, BusinessReportActivity.class);
        startActivity(intent);
    }

    public void onClickLogout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
