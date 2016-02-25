package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DisplayMaintenanceLogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_maintenance_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    void onAPIResponse(String jsonString) {

    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MachineInformationActivity.class);
        startActivity(intent);
    }
}
