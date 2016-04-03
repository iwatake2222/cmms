package ca.on.conestogac.cmms;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    public static final String MAINTENANCE_LOG_RELATED_REQUEST_ID = "ca.on.conestogac.cmms.MAINTENANCE_LOG_RELATED_REQUEST_ID";
    public static final String MODE_CREATE = "CreateMaintenanceLogActivityMODE";
    public static final String MODE_VIEW = "ViewMaintenanceLogActivityMODE";
    public static final String MODE_EDIT = "EditMaintenanceLogActivityMODE";
    private String maintenanceLogMode;
    private String receivedMaintenanceLog;
    private MaintenanceLog currentMaintenanceLog;
    private String mMachine;
    private String mMaintenanceLogID;
    private String mMaintenanceLogDate;
    private String mMaintenanceLogCompletedBy;
    private String mMaintenanceLogRelatedRequestID;
    private String mMaintenanceLogMaintenanceRequired;
    private String mMaintenanceLogActionTaken;
    private String mMaintenanceLogPartsRequired;
    private String mMaintenanceLogApproxCost;
    private String mMaintenanceLogRequisitionNumber;
    private String mMaintenanceLogContractorName;
    private String mMaintenanceLogContractorCompany;
    private Machine mMachineObject;
    private String createdMaintenanceLogID;

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

        changeViewVisibilityByUserPrivilege();
        initMachineInformation();
    }

    private void changeViewVisibilityByUserPrivilege() {
        if (!User.getInstance().canModifyMaintenanceLog()) {
            ((Button)findViewById(R.id.buttonMaintenanceLogEditMaintenanceLog)).setEnabled(false);
            ((Button)findViewById(R.id.buttonMaintenanceLogSaveEditMaintenanceLog)).setEnabled(false);
        }
        if (!User.getInstance().canCreateMaintenanceLog()) {
            ((Button)findViewById(R.id.buttonMaintenanceLogCreateMaintenanceLog)).setEnabled(false);
        }
        if (!User.getInstance().canDisplayWorkRequest()) {
            ((Button)findViewById(R.id.buttonMaintenanceLogShowRelatedWorkRequest)).setEnabled(false);
        }
    }

    private void configureActivityEditMode() {
        // edit mode
        setTitle("Edit Maintenance Log");

        // Switch do Edits and Spinner
        SwitchViewsMode(0);

        // Disable work request id
        EditText editTextMaintenanceLogRelatedWorkRequestID = (EditText) findViewById(R.id.editTextMaintenanceLogRelatedWorkRequestID);
        editTextMaintenanceLogRelatedWorkRequestID.setEnabled(false);

        // Auto-fill date text
        EditText editTextMaintenanceLogDate = (EditText) findViewById(R.id.editTextMaintenanceLogDate);
        editTextMaintenanceLogDate.setEnabled(false);

        // Auto-fill date text
        EditText editTextMaintenanceLogID = (EditText) findViewById(R.id.editTextMaintenanceLogID);
        editTextMaintenanceLogID.setEnabled(false);

        // Auto-fill completed by text
        EditText editTextMaintenanceLogCompletedBy = (EditText) findViewById(R.id.editTextMaintenanceLogCompletedBy);
        editTextMaintenanceLogCompletedBy.setEnabled(false);

        // Configure buttons visibility
        Button buttonMaintenanceLogCreateMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogCreateMaintenanceLog);
        buttonMaintenanceLogCreateMaintenanceLog.setVisibility(View.GONE);
        Button buttonMaintenanceLogSaveEditMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogSaveEditMaintenanceLog);
        buttonMaintenanceLogSaveEditMaintenanceLog.setVisibility(View.VISIBLE);
        Button buttonMaintenanceLogEditMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogEditMaintenanceLog);
        buttonMaintenanceLogEditMaintenanceLog.setVisibility(View.GONE);

        // Checkbox visibility
        CheckBox checkBoxMaintenanceLogCloseRequest = (CheckBox) findViewById(R.id.checkBoxMaintenanceLogCloseRequest);
        checkBoxMaintenanceLogCloseRequest.setVisibility(View.GONE);
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

        // Checkbox visibility
        Button checkBoxMaintenanceLogCloseRequest = (Button) findViewById(R.id.checkBoxMaintenanceLogCloseRequest);
        checkBoxMaintenanceLogCloseRequest.setVisibility(View.GONE);
    }

    private void configureActivityCreateMode() {
        setTitle("Create Maintenance Log");

        // Related work request ID should be sent via intent when this is create mode
        mMaintenanceLogRelatedRequestID = getIntent().getStringExtra(MAINTENANCE_LOG_RELATED_REQUEST_ID);

        // Auto-fill date text
        Calendar calendar = Calendar.getInstance();
        EditText editTextMaintenanceLogDate = (EditText) findViewById(R.id.editTextMaintenanceLogDate);
        editTextMaintenanceLogDate.setEnabled(false);
        editTextMaintenanceLogDate.setText(Utility.convertDateYYYYMMDDToShow(Utility.convertDateToStringRaw(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))));

        // Auto-fill completed by text
        EditText editTextMaintenanceLogCompletedBy = (EditText) findViewById(R.id.editTextMaintenanceLogCompletedBy);
        editTextMaintenanceLogCompletedBy.setText(User.getInstance().userID);
        editTextMaintenanceLogCompletedBy.setEnabled(false);

        // Auto-fill related work request id
        EditText editTextMaintenanceLogRelatedWorkRequestID = (EditText) findViewById(R.id.editTextMaintenanceLogRelatedWorkRequestID);
        editTextMaintenanceLogRelatedWorkRequestID.setText(mMaintenanceLogRelatedRequestID);
        editTextMaintenanceLogRelatedWorkRequestID.setEnabled(false);

        // Hide LOG ID field since it's a new log
        LinearLayout logIDlinearLayout = (LinearLayout) findViewById(R.id.middleRow1Col1);
        logIDlinearLayout.setVisibility(View.GONE);

        // Switch do Edits and Spinner
        SwitchViewsMode(0);

        // Configure buttons visibility
        Button buttonMaintenanceLogCreateMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogCreateMaintenanceLog);
        buttonMaintenanceLogCreateMaintenanceLog.setVisibility(View.VISIBLE);
        Button buttonMaintenanceLogSaveEditMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogSaveEditMaintenanceLog);
        buttonMaintenanceLogSaveEditMaintenanceLog.setVisibility(View.GONE);
        Button buttonMaintenanceLogEditMaintenanceLog = (Button) findViewById(R.id.buttonMaintenanceLogEditMaintenanceLog);
        buttonMaintenanceLogEditMaintenanceLog.setVisibility(View.GONE);

        // Checkbox visibility
        Button checkBoxMaintenanceLogCloseRequest = (Button) findViewById(R.id.checkBoxMaintenanceLogCloseRequest);
        checkBoxMaintenanceLogCloseRequest.setVisibility(View.VISIBLE);
    }

    private void fillMaintenanceLogFields(MaintenanceLog maintenanceLog) {
        // Fill information regardless if it is a TextView or EditText
        ViewSwitcher viewSwitcherMaintenanceLogID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogID);
        TextView maintenanceLogID = (TextView) viewSwitcherMaintenanceLogID.getCurrentView();
        maintenanceLogID.setText(maintenanceLog.getMaintenanceLogID());

        ViewSwitcher viewSwitcherMaintenanceLogDate = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogDate);
        TextView maintenanceLogDate = (TextView) viewSwitcherMaintenanceLogDate.getCurrentView();
        maintenanceLogDate.setText(Utility.convertDateYYYYMMDDToShow(maintenanceLog.getDate()));

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

    private void getMaintenanceLogFields() {
        ViewSwitcher viewSwitcherMaintenanceLogID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogID);
        TextView maintenanceLogID = (TextView) viewSwitcherMaintenanceLogID.getCurrentView();
        mMaintenanceLogID = maintenanceLogID.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogDate = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogDate);
        TextView maintenanceLogDate = (TextView) viewSwitcherMaintenanceLogDate.getCurrentView();
        mMaintenanceLogDate = Utility.convertFormattedDateToRaw(maintenanceLogDate.getText().toString());

        ViewSwitcher viewSwitcherMaintenanceLogCompletedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogCompletedBy);
        TextView completedBy = (TextView) viewSwitcherMaintenanceLogCompletedBy.getCurrentView();
        mMaintenanceLogCompletedBy = completedBy.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogRelatedWorkRequestID = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogRelatedWorkRequestID);
        TextView relatedWorkRequestID = (TextView) viewSwitcherMaintenanceLogRelatedWorkRequestID.getCurrentView();
        mMaintenanceLogRelatedRequestID = relatedWorkRequestID.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogMaintenanceRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogMaintenanceRequired);
        TextView maintenanceRequired = (TextView) viewSwitcherMaintenanceLogMaintenanceRequired.getCurrentView();
        mMaintenanceLogMaintenanceRequired = maintenanceRequired.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogActionTaken = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogActionTaken);
        TextView actionTaken = (TextView) viewSwitcherMaintenanceLogActionTaken.getCurrentView();
        mMaintenanceLogActionTaken = actionTaken.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogPartsRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogPartsRequired);
        TextView partsRequired = (TextView) viewSwitcherMaintenanceLogPartsRequired.getCurrentView();
        mMaintenanceLogPartsRequired = partsRequired.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogApproximateCost = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogApproximateCost);
        TextView approxCost = (TextView) viewSwitcherMaintenanceLogApproximateCost.getCurrentView();
        mMaintenanceLogApproxCost = approxCost.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogRequisitionNumber = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogRequisitionNumber);
        TextView requisitionNumber = (TextView) viewSwitcherMaintenanceLogRequisitionNumber.getCurrentView();
        mMaintenanceLogRequisitionNumber = requisitionNumber.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogContractor = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogContractor);
        TextView contractorName = (TextView) viewSwitcherMaintenanceLogContractor.getCurrentView();
        mMaintenanceLogContractorName = contractorName.getText().toString().trim();

        ViewSwitcher viewSwitcherMaintenanceLogContractorCompany = (ViewSwitcher) findViewById(R.id.viewSwitcherMaintenanceLogContractorCompany);
        TextView contractorCompany = (TextView) viewSwitcherMaintenanceLogContractorCompany.getCurrentView();
        mMaintenanceLogContractorCompany = contractorCompany.getText().toString().trim();
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

    private void FillJsonObject(JSONObject jsonParam) throws JSONException {
        jsonParam.put("userID", User.getInstance().userID);
        jsonParam.put("machineID", mMachineObject.getMachineID());
        jsonParam.put("date", mMaintenanceLogDate);
        jsonParam.put("completedBy", mMaintenanceLogCompletedBy);
        jsonParam.put("requestID", mMaintenanceLogRelatedRequestID);
        jsonParam.put("maintenanceRequired", mMaintenanceLogMaintenanceRequired);
        jsonParam.put("actionTaken", mMaintenanceLogActionTaken);
        jsonParam.put("partsRequired", mMaintenanceLogPartsRequired);
        jsonParam.put("partCost", mMaintenanceLogApproxCost);
        jsonParam.put("partRequisitionNum", mMaintenanceLogRequisitionNumber);
        jsonParam.put("contractor", mMaintenanceLogContractorName);
        jsonParam.put("contractorCompany", mMaintenanceLogContractorCompany);
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

        try {
            JSONObject jsonMachine = new JSONObject(mMachine);
            mMachineObject = new Machine(jsonMachine);
        }
        catch (JSONException e) {
            Utility.logError(e.getMessage());
        }

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

                if (!maintenanceLogMode.equals(MODE_CREATE))
                    fillMaintenanceLogFields(currentMaintenanceLog);
            }

            if (jsonObject.has("createdMaintenanceLogID")) {
                createdMaintenanceLogID = jsonObject.getString("createdMaintenanceLogID");
                Utility.showToast(this, "LOG mock created with ID: " + createdMaintenanceLogID);

                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxMaintenanceLogCloseRequest);

                if (checkBox.isChecked()) {
                    Utility.logDebug("createdMaintenanceLogID callback: changing workRequest to closed");
                    ChangeRelatedWorkRequestStatus("Closed"); // TODO: do not hard-code
                }
                else {
                    Utility.logDebug("createdMaintenanceLogID callback: changing workRequest to Working");
                    ChangeRelatedWorkRequestStatus("Working"); // TODO: do not hard-code
                }

                return;
            }

            if (jsonObject.has("modifiedRequestID")) {
                Utility.logDebug("modifiedRequestID callback: Work Request successfully modified. Going to Displaymode (maintenance log)");
                // after closing or changing the request to working, go to view mode
                SearchMaintenanceLog(createdMaintenanceLogID);

                return;
            }

            if (jsonObject.has("modifiedMaintenanceLogID")) {
                String modifiedMaintenanceLogID = jsonObject.getString("modifiedMaintenanceLogID");
                Utility.showToast(this, "LOG mock modified within ID: " + modifiedMaintenanceLogID);

                SearchMaintenanceLog(modifiedMaintenanceLogID);
                return;
            }

            if (jsonObject.has("maintenanceLogID")) {
                // Search result - redirect to view mode
                MaintenanceLog ml = new MaintenanceLog(jsonObject);
                Intent intent = new Intent(DisplayMaintenanceLogActivity.this, DisplayMaintenanceLogActivity.class);
                intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MAINTENANCE_LOG, ml.createJson());
                intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MACHINE, mMachine);
                intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_MODE, DisplayMaintenanceLogActivity.MODE_VIEW);
                startActivity(intent);
                return;
            }

            if (jsonObject.has("requestID")) {
                WorkRequest wr = new WorkRequest(jsonObject);
                Intent intent = new Intent(this, DisplayRequestActivity.class);
                intent.putExtra(DisplayRequestActivity.EXTRA_REQUEST, wr.createJson());
                intent.putExtra(DisplayRequestActivity.WORK_REQUEST_MODE, DisplayRequestActivity.MODE_VIEW);
                startActivity(intent);
                return;
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void ChangeRelatedWorkRequestStatus(String status) {
        String requestID = GetRelatedWorkRequestID();

        JSONObject jsonModifyWorkRequest = new JSONObject();

        try {
            jsonModifyWorkRequest.put("userID", User.getInstance().userID);
            jsonModifyWorkRequest.put("requestID", requestID);
            jsonModifyWorkRequest.put("status", status);

            if (status.equals("Closed")) { // TODO: do not hardcode
                Calendar calendar = Calendar.getInstance();
                String dateResolved = Utility.convertDateToStringRaw(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                jsonModifyWorkRequest.put("dateResolved", dateResolved);
                jsonModifyWorkRequest.put("completedBy", User.getInstance().userID);
            }
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("ModifyWorkRequest", jsonModifyWorkRequest);
    }

    private void SearchMaintenanceLog(String maintenanceLogID) {
        JSONObject jsonSearchRequest = new JSONObject();

        try {
            jsonSearchRequest.put("userID", User.getInstance().userID);
            jsonSearchRequest.put("maintenanceLogID", maintenanceLogID);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("SearchMaintenanceLog", jsonSearchRequest);
    }

    public void onClickModifyMaintenanceLogActivity(View view) {
        // Change to edit mode
        Intent intent = new Intent(this, DisplayMaintenanceLogActivity.class);

        intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MACHINE, mMachine);
        intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MAINTENANCE_LOG, currentMaintenanceLog.createJson());
        intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_MODE, DisplayMaintenanceLogActivity.MODE_EDIT);

        startActivity(intent);
    }

    public void onClickMaintenanceLogActivitySaveEditedLog(View view) {
        getMaintenanceLogFields();
        JSONObject jsonParam = new JSONObject();

        try {
            FillJsonObject(jsonParam);

            jsonParam.put("maintenanceLogID", currentMaintenanceLog.getMaintenanceLogID());
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("ModifyMaintenanceLog", jsonParam);
    }

    public void onClickMaintenanceLogShowRelatedWorkRequest(View view) {
        String relatedRequestID = GetRelatedWorkRequestID();

        SearchWorkRequest(relatedRequestID);
    }

    private String GetRelatedWorkRequestID() {
        String relatedRequestID;

        if (maintenanceLogMode.equals(MODE_CREATE)) {
            relatedRequestID = mMaintenanceLogRelatedRequestID;
        } else {
            relatedRequestID = currentMaintenanceLog.getRequestID();
        }
        return relatedRequestID;
    }

    public void onClickCreateRequestActivityCreateRequest(View view) {
        getMaintenanceLogFields();
        JSONObject jsonParam = new JSONObject();

        try {
            FillJsonObject(jsonParam);

            // attach related work request ID
            jsonParam.put("requestID", mMaintenanceLogRelatedRequestID);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("CreateMaintenanceLog", jsonParam);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MachineInformationActivity.class);
        startActivity(intent);
    }

    private void SearchWorkRequest(String workRequestID) {
        JSONObject jsonSearchRequest = new JSONObject();

        try {
            jsonSearchRequest.put("userID", User.getInstance().userID);
            jsonSearchRequest.put("requestID", workRequestID);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("SearchWorkRequest", jsonSearchRequest);
    }
}
