package ca.on.conestogac.cmms;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MaintenanceLogListActivity extends BaseActivity {
    public static final String EXTRA_MAINTENANCELOG_LIST = "ca.on.conestogac.cmms.EXTRA_MAINTENANCELOG_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_log_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    void onAPIResponse(String jsonString) {

    }


}
