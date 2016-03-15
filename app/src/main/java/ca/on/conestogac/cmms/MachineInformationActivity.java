package ca.on.conestogac.cmms;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MachineInformationActivity extends BaseActivity {
    public static final String EXTRA_MACHINE = "ca.on.conestogac.cmms.EXTRA_MACHINE";
    Machine mMachine;
    String mMachineJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMachineJsonString = getIntent().getStringExtra(EXTRA_MACHINE);
        if (mMachineJsonString == null) {
            Utility.logError("unexpedted call");
        } else {
            Utility.logDebug(mMachineJsonString);
        }

        /* Common Machine Information */
        Fragment fragment = MachineFragment.newInstance(mMachineJsonString);
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
        /* !Common Machine Information */

        convertMachine(mMachineJsonString);
        setValues();


    }

    public void onClickCreateRequest(View view) {
        Intent intent = new Intent(this, DisplayRequestActivity.class);
        intent.putExtra(DisplayRequestActivity.EXTRA_MACHINE, mMachineJsonString);
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

    private void convertMachine(String machineJsonString){
        try {
            JSONObject machineJson = new JSONObject(machineJsonString);
            mMachine = new Machine(machineJson);
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void setValues() {
        ((TextView)findViewById(R.id.textViewMachineMachineID)).setText(mMachine.getMachineID());
        ((TextView)findViewById(R.id.textViewMachineDescription)).setText(mMachine.getDescription());

    }
}
