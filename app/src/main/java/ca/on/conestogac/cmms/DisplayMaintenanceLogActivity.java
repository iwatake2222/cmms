package ca.on.conestogac.cmms;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DisplayMaintenanceLogActivity extends BaseActivity {

    public static final String EXTRA_MAINTENANCE_LOG = "ca.on.conestogac.cmms.EXTRA_MAINTENANCE_LOG";
    public static final String EXTRA_MACHINE = "ca.on.conestogac.cmms.EXTRA_MACHINE";
    public static final String MAINTENANCE_LOG_MODE = "ca.on.conestogac.cmms.MAINTENANCE_LOG_MODE";
    public static final String MODE_CREATE = "CreateMaintenanceLogActivityMODE";
    public static final String MODE_VIEW = "ViewMaintenanceLogActivityMODE";
    public static final String MODE_EDIT = "EditMaintenanceLogActivityMODE";
    private String maintenanceLogMode;
    private String receivedMaintenanceLog;
    private MaintenanceLog currentMaintenanceLog;
    private String mMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_maintenance_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receivedMaintenanceLog = getIntent().getStringExtra(EXTRA_MAINTENANCE_LOG);
        maintenanceLogMode = getIntent().getStringExtra(MAINTENANCE_LOG_MODE);

        try {
            if (maintenanceLogMode == null) {
                Utility.logError("Unexpected call");
                throw new IllegalArgumentException();
            } else if (maintenanceLogMode.equals(MODE_EDIT)) {
                currentMaintenanceLog = new MaintenanceLog(new JSONObject(receivedMaintenanceLog));
                Utility.logDebug(currentMaintenanceLog.toString());
                configureActivityEditMode();
            } else if (maintenanceLogMode.equals(MODE_VIEW)) {
                currentMaintenanceLog = new MaintenanceLog(new JSONObject(receivedMaintenanceLog));
                Utility.logDebug(currentMaintenanceLog.toString());
                configureActivityViewMode();
            } else if (maintenanceLogMode.equals(MODE_CREATE)) {
                configureActivityCreateMode();
            } else {
                // unrecognized mode
                Utility.logError("unexpected call");
                throw new IllegalArgumentException();
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void configureActivityEditMode() {
        // edit mode
        setTitle("Edit Maintenance Log");

        // Switch do Edits and Spinner
        SwitchViewsMode(0);
    }

    private void configureActivityViewMode() {
        // view-mode
        setTitle("Maintenance Log Details");

        // Switch do ViewText
        SwitchViewsMode(1);
    }

    private void configureActivityCreateMode() {
        setTitle("Create Maintenance Log");

        // Switch do Edits and Spinner
        SwitchViewsMode(0);
    }

    private void SwitchViewsMode(int viewIndex) {
        ViewSwitcher viewSwitcherMaintenanceLogID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogID);
        ViewSwitcher viewSwitcherMaintenanceLogDate = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogDate);
        ViewSwitcher viewSwitcherMaintenanceLogCompletedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogCompletedBy);
        ViewSwitcher viewSwitcherMaintenanceLogRelatedWorkRequestID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogRelatedWorkRequestID);
        ViewSwitcher viewSwitcherMaintenanceLogMaintenanceRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogMaintenanceRequired);
        ViewSwitcher viewSwitcherMaintenanceLogActionTaken = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogActionTaken);
        ViewSwitcher viewSwitcherMaintenanceLogPartsRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogPartsRequired);
        ViewSwitcher viewSwitcherMaintenanceLogApproximateCost = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogApproximateCost);
        ViewSwitcher viewSwitcherMaintenanceLogRequisitionNumber = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogRequisitionNumber);
        ViewSwitcher viewSwitcherMaintenanceLogContractor = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogContractor);
        ViewSwitcher viewSwitcherMaintenanceLogContractorCompany = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogContractorCompany);

        viewSwitcherMaintenanceLogID.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogDate.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogCompletedBy.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogRelatedWorkRequestID.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogMaintenanceRequired.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogActionTaken.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogPartsRequired.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogApproximateCost.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogRequisitionNumber.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogContractor.setDisplayedChild(viewIndex);
        viewSwitcherMaintenanceLogContractorCompany.setDisplayedChild(viewIndex);

    }

    private void initMachineInformation()
    {
        if (maintenanceLogMode == null) {
            return;
        } else if (maintenanceLogMode.equals(MODE_EDIT) || maintenanceLogMode.equals(MODE_VIEW)) {
            String machineId = currentMaintenanceLog.getMachineID();
            if(machineId != "") {
                JSONObject jsonParam = new JSONObject();
                try{
                    jsonParam.put("userID", User.getInstance().userID);
                    jsonParam.put("machineID", machineId);
                } catch (JSONException e) {
                    Utility.logDebug(e.getMessage());
                }
                callAPI("SearchMachine", jsonParam);
            }
        } else if (maintenanceLogMode.equals(MODE_CREATE)) {
            mMachine = getIntent().getStringExtra(EXTRA_MACHINE);
            setMachineInformation();
        } else {
            return;
        }
    }

    private void setMachineInformation ()
    {
        if(mMachine == null) return;
        /* Common Machine Information */
        Fragment fragment = MachineFragment.newInstance(mMachine);
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
        /* !Common Machine Information */
    }

    @Override
    void onAPIResponse(String jsonString) {

    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MachineInformationActivity.class);
        startActivity(intent);
    }
}
