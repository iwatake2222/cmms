package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MachineInformationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickCreateRequest(View view) {
        Intent intent = new Intent(this, DisplayRequestActivity.class);
        // TODO: get actual machine information JSON and sent to activity
        intent.putExtra(DisplayRequestActivity.EXTRA_REQUEST, "machine information");
        intent.putExtra(DisplayRequestActivity.WORK_REQUEST_MODE, DisplayRequestActivity.MODE_CREATE);
        startActivity(intent);
    }

    public void onClickDisplayRequest(View view) {
        Intent intent = new Intent(this, SearchRequestListActivity.class);
        startActivity(intent);
    }

    public void onClickDisplayLog(View view) {
        Intent intent = new Intent(this, MaintenanceLogListActivity.class);
        startActivity(intent);
    }


    @Override
    void onAPIResponse(String jsonString) {

    }

}
