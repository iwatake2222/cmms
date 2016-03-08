package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DisplayRequestActivity extends BaseActivity {
    public static final String EXTRA_REQUEST = "ca.on.conestogac.cmms.EXTRA_REQUEST";
    public static final String WORK_REQUEST_MODE = "ca.on.conestogac.cmms.WORK_REQUEST_MODE";
    public static final String MODE_CREATE = "CreateRequestActivityMODE";
    public static final String MODE_VIEW = "ViewRequestActivityMODE";
    public static final String MODE_EDIT = "EditRequestActivityMODE";

    private String currentMode;
    private String mDateCreated;
    private String mCreatedBy;
    private String mProgress;
    private String mTitle;
    private String mRequestFor;
    private String mStatus;
    private String mPriority;
    private String mMachineIsRequired;
    private String mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for debug
        String request = getIntent().getStringExtra(EXTRA_REQUEST);
        String workRequestMode = getIntent().getStringExtra(WORK_REQUEST_MODE);

        // TODO: refactor into specific methods
        try {
            if (request == null || workRequestMode == null)
            {
                Utility.logError("Unexpected call");
                throw new IllegalArgumentException();
            }
            else if (workRequestMode.equals(MODE_EDIT))
            {
                // edit mode
                TextView textViewTest = (TextView) findViewById(R.id.textViewTest);
                textViewTest.setText("Edit Work-Request");

                WorkRequest workRequest = new WorkRequest(new JSONObject(request));

                fillWorkRequestFields(workRequest);

                Utility.logDebug(workRequest.toString());
            }
            else if (workRequestMode.equals(MODE_VIEW))
            {
                // view-mode
                TextView textViewTest = (TextView) findViewById(R.id.textViewTest);
                textViewTest.setText("Work-Request Details");

                // TODO: setup screen to view mode
                WorkRequest workRequest = new WorkRequest(new JSONObject(request));
                fillWorkRequestFields(workRequest);
            }
            else if (workRequestMode.equals(MODE_CREATE))
            {
                // create
                // TODO: create JSON for machine ID and use this to create the work request
                TextView textViewTest = (TextView) findViewById(R.id.textViewTest);
                textViewTest.setText("Create Work-Request");

                Calendar now = new GregorianCalendar();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

                EditText editTextDateCreated = (EditText) findViewById(R.id.editTextDateCreated);
                editTextDateCreated.setText(sdf.format(now.getTime()));
                // TODO: hide create maintenance log button
            }
            else {
                // unrecognized mode
                Utility.logError("unexpected call");
                throw new IllegalArgumentException();
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void fillWorkRequestFields(WorkRequest workRequest) {
        EditText requestIdTextView = (EditText) findViewById(R.id.editTextRequestID);
        requestIdTextView.setText(workRequest.getRequestID());

        EditText editTextDateCreated = (EditText) findViewById(R.id.editTextDateCreated);
        editTextDateCreated.setText(workRequest.getDateRequested());

        EditText editTextCreatedBy = (EditText) findViewById(R.id.editTextCreatedBy);
        editTextCreatedBy.setText(workRequest.getCreatedBy());

        EditText editTextProgress = (EditText) findViewById(R.id.editTextProgress);
        editTextProgress.setText(workRequest.getProgress());

        EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextTitle.setText(workRequest.getTitle());

        EditText editTextRequestFor = (EditText) findViewById(R.id.editTextRequestFor);
        editTextRequestFor.setText(workRequest.getRequestFor());

        EditText editTextStatus = (EditText) findViewById(R.id.editTextStatus);
        editTextStatus.setText(workRequest.getStatus());

        EditText editTextPriority = (EditText) findViewById(R.id.editTextPriority);
        editTextPriority.setText(workRequest.getPriority());

        EditText editTextMachineIsRequired = (EditText) findViewById(R.id.editTextMachineIsRequired);
        editTextMachineIsRequired.setText(workRequest.getDateDue());

        EditText editTextDescriptionOfRequest = (EditText) findViewById(R.id.editTextDescriptionOfRequest);
        editTextDescriptionOfRequest.setText(workRequest.getDescription());
    }

    private void getWorkRequestFields() {
        // TODO: validate required fields and formats
//        EditText requestIdTextView = (EditText) findViewById(R.id.editTextRequestID);
//        requestIdTextView.getText();

        EditText editTextDateCreated = (EditText) findViewById(R.id.editTextDateCreated);
        mDateCreated = editTextDateCreated.getText().toString().trim();

        EditText editTextCreatedBy = (EditText) findViewById(R.id.editTextCreatedBy);
        mCreatedBy = editTextCreatedBy.getText().toString().trim();

        EditText editTextProgress = (EditText) findViewById(R.id.editTextProgress);
        mProgress = editTextProgress.getText().toString().trim();

        EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        mTitle = editTextTitle.getText().toString().trim();

        EditText editTextRequestFor = (EditText) findViewById(R.id.editTextRequestFor);
        mRequestFor = editTextRequestFor.getText().toString().trim();

        EditText editTextStatus = (EditText) findViewById(R.id.editTextStatus);
        mStatus = editTextStatus.getText().toString().trim();

        EditText editTextPriority = (EditText) findViewById(R.id.editTextPriority);
        mPriority = editTextPriority.getText().toString().trim();

        EditText editTextMachineIsRequired = (EditText) findViewById(R.id.editTextMachineIsRequired);
        mMachineIsRequired = editTextMachineIsRequired.getText().toString().trim();

        EditText editTextDescriptionOfRequest = (EditText) findViewById(R.id.editTextDescriptionOfRequest);
        mDescription = editTextDescriptionOfRequest.getText().toString().trim();
    }

    @Override
    void onAPIResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");
            if (result.compareTo(ValueConstants.RET_OK) != 0 ) {
                // do something if needed when error happens
            }
            if(jsonObject.has("createdRequestID")){
                String createdRequestID = jsonObject.getString("createdRequestID");
                Utility.showToast(this, "Request mock created with ID: " + createdRequestID);
                // TODO: redirect to machine information?
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public void onClickCreateMaintenanceLog(View view) {
        Intent intent = new Intent(this, DisplayMaintenanceLogActivity.class);
        startActivity(intent);
    }

    public void onClickCreateRequestActivityCreateRequest(View view) {
        getWorkRequestFields();
        JSONObject jsonParam = new JSONObject();

        try{
            jsonParam.put("userID", User.getInstance().userID);
            // TODO fill machine ID
            jsonParam.put("machineID", "123");
            jsonParam.put("createdBy", mCreatedBy);
            jsonParam.put("dateRequested", mDateCreated);
            jsonParam.put("dateDue", mRequestFor);
            // TODO: leave it blank?
            jsonParam.put("dateResolved", "");
            jsonParam.put("progress", mProgress);
            jsonParam.put("title", mTitle);
            jsonParam.put("requestFor", mMachineIsRequired);
            jsonParam.put("status", mStatus);
            jsonParam.put("priority", mPriority);
            jsonParam.put("description", mDescription);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("CreateWorkRequest", jsonParam);
    }
}
