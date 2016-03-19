package ca.on.conestogac.cmms;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
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

        initMachineInformation();
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

        // Configure buttons visibility
        Button buttonMaintenanceLogCreateMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogCreateMaintenanceLog);
        buttonMaintenanceLogCreateMaintenanceLog.setVisibility(View.GONE);
        Button buttonMaintenanceLogSaveEditMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogSaveEditMaintenanceLog);
        buttonMaintenanceLogSaveEditMaintenanceLog.setVisibility(View.GONE);
        Button buttonMaintenanceLogEditMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogEditMaintenanceLog);
        buttonMaintenanceLogEditMaintenanceLog.setVisibility(View.VISIBLE);
    }

    private void configureActivityCreateMode() {
        setTitle("Create Maintenance Log");

        // Switch do Edits and Spinner
        SwitchViewsMode(0);
    }

    private void fillMaintenanceLogFields(MaintenanceLog maintenanceLog) {
        // Fill information regardless if it is a TextView or EditText
        ViewSwitcher viewSwitcherMaintenanceLogID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogID);
        TextView maintenanceLogID = (TextView) viewSwitcherMaintenanceLogID.getCurrentView();
        maintenanceLogID.setText(maintenanceLog.getMaintenanceLogID());

        ViewSwitcher viewSwitcherMaintenanceLogDate = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogDate);
        TextView maintenanceLogDate = (TextView) viewSwitcherMaintenanceLogDate.getCurrentView();
        maintenanceLogDate.setText(maintenanceLog.getDate());

        ViewSwitcher viewSwitcherMaintenanceLogCompletedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogCompletedBy);
        TextView completedBy = (TextView) viewSwitcherMaintenanceLogCompletedBy.getCurrentView();
        completedBy.setText(maintenanceLog.getCompletedBy());

        ViewSwitcher viewSwitcherMaintenanceLogRelatedWorkRequestID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogRelatedWorkRequestID);
        TextView relatedWorkRequestID = (TextView) viewSwitcherMaintenanceLogRelatedWorkRequestID.getCurrentView();
        relatedWorkRequestID.setText(maintenanceLog.getRequestID());

        ViewSwitcher viewSwitcherMaintenanceLogMaintenanceRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogMaintenanceRequired);
        TextView maintenanceRequired = (TextView) viewSwitcherMaintenanceLogMaintenanceRequired.getCurrentView();
        maintenanceRequired.setText(maintenanceLog.getMaintenanceRequired());

        ViewSwitcher viewSwitcherMaintenanceLogActionTaken = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogActionTaken);
        TextView actionTaken = (TextView) viewSwitcherMaintenanceLogActionTaken.getCurrentView();
        actionTaken.setText(maintenanceLog.getActionTaken());

        ViewSwitcher viewSwitcherMaintenanceLogPartsRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogPartsRequired);
        TextView partsRequired = (TextView) viewSwitcherMaintenanceLogPartsRequired.getCurrentView();
        partsRequired.setText(maintenanceLog.getPartsRequired());

        ViewSwitcher viewSwitcherMaintenanceLogApproximateCost = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogApproximateCost);
        TextView approxCost = (TextView) viewSwitcherMaintenanceLogApproximateCost.getCurrentView();
        approxCost.setText(maintenanceLog.getPartCost());

        ViewSwitcher viewSwitcherMaintenanceLogRequisitionNumber = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogRequisitionNumber);
        TextView requisitionNumber = (TextView) viewSwitcherMaintenanceLogRequisitionNumber.getCurrentView();
        requisitionNumber.setText(maintenanceLog.getPartRequisitionNum());

        ViewSwitcher viewSwitcherMaintenanceLogContractor = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogContractor);
        TextView contractorName = (TextView) viewSwitcherMaintenanceLogContractor.getCurrentView();
        contractorName.setText(maintenanceLog.getContractor());

        ViewSwitcher viewSwitcherMaintenanceLogContractorCompany = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogContractorCompany);
        TextView contractorCompany = (TextView) viewSwitcherMaintenanceLogContractorCompany.getCurrentView();
        contractorCompany.setText(maintenanceLog.getContractorCompany());
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
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");

            if (result.compareTo(ValueConstants.RET_OK) != 0) {
                // do something if needed when error happens
            }

            // note: if response contains linkToDocument, I assume it the response for SearchMachine
            if (jsonObject.has("linkToDocument")) {
                // first callback
                mMachine = jsonString;
                setMachineInformation();
                fillMaintenanceLogFields(currentMaintenanceLog);
            }

        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MachineInformationActivity.class);
        startActivity(intent);
    }
}
