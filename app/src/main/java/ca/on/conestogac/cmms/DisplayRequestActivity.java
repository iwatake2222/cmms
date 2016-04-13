package ca.on.conestogac.cmms;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayRequestActivity extends BaseActivity {
    public static final String EXTRA_REQUEST = "ca.on.conestogac.cmms.EXTRA_REQUEST";
    public static final String EXTRA_MACHINE = "ca.on.conestogac.cmms.EXTRA_MACHINE";
    public static final String WORK_REQUEST_MODE = "ca.on.conestogac.cmms.WORK_REQUEST_MODE";
    public static final String MODE_CREATE = "CreateRequestActivityMODE";
    public static final String MODE_VIEW = "ViewRequestActivityMODE";
    public static final String MODE_EDIT = "EditRequestActivityMODE";

    private enum LIST_TYPE {
        PROGRESS,
        STATUS,
        PRIORITY,
        REQUESTFOR
    }

    private String mDateCreated;
    private String mDateResolved;
    private String mCreatedBy;
    private String mProgress;
    private String mTitle;
    private String mRequestFor;
    private String mStatus;
    private String mPriority;
    private String mMachineIsRequired;
    private String mDescription;
    private String mCompletedBy;
    private ArrayAdapter<String> mAdapterRequestFor;
    private ArrayAdapter<String> mAdapterStatus;
    private ArrayAdapter<String> mAdapterPriority;
    private ArrayAdapter<String> mAdapterProgress;
    private WorkRequest workRequest;
    private String receivedRequest;
    private String mMachine;
    private Machine mMachineObject;
    private String workRequestMode;
    private MaintenanceAdapter mMaintenanceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for debug
        receivedRequest = getIntent().getStringExtra(EXTRA_REQUEST);
        workRequestMode = getIntent().getStringExtra(WORK_REQUEST_MODE);

        initListElements();

        try {
            // memo: receivedRequest can be null for CREATE. (takeshi)
            //if (receivedRequest == null || workRequestMode == null) {
            if (workRequestMode == null) {
                Utility.logError("Unexpected call");
                throw new IllegalArgumentException();
            } else if (workRequestMode.equals(MODE_EDIT)) {
                workRequest = new WorkRequest(new JSONObject(receivedRequest));
                Utility.logDebug(workRequest.toString());
                configureActivityEditMode();
            } else if (workRequestMode.equals(MODE_VIEW)) {
                workRequest = new WorkRequest(new JSONObject(receivedRequest));
                Utility.logDebug(workRequest.toString());
                configureActivityViewMode();
            } else if (workRequestMode.equals(MODE_CREATE)) {
                configureActivityCreateMode();
            } else {
                // unrecognized mode
                Utility.logError("unexpected call");
                throw new IllegalArgumentException();
            }

            changeViewVisibilityByUserPrivilege();
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void changeViewVisibilityByUserPrivilege() {
        if (!User.getInstance().canModifyWorkRequest()) {
            ((Button)findViewById(R.id.buttonDisplayRequestEditRequest)).setEnabled(false);
        }
        if (!User.getInstance().canCreateWorkRequest()) {
            ((Button)findViewById(R.id.buttonDisplayRequestCreateRequest)).setEnabled(false);
        }
        if (!User.getInstance().canCreateMaintenanceLog()) {
            ((Button)findViewById(R.id.buttonDisplayRequestCreateMaintenanceLog)).setEnabled(false);
        }
        if (!User.getInstance().canDisplayMaintenanceLog()) {
            ((ListView)findViewById(R.id.listViewDisplayRequestMaintenanceList)).setVisibility(View.GONE);
        }
    }

    private void configureActivityEditMode() {
        // edit mode
        setTitle("Edit Work-Request");

        // Switch do Edits and Spinner
        SwitchViewsMode(0);

        // Cannot edit request id and date created
        EditText linearLayoutDisplayRequestRequestID = (EditText) findViewById(R.id.editTextRequestID);
        linearLayoutDisplayRequestRequestID.setEnabled(false);

        EditText linearLayoutDisplayDateCreated = (EditText) findViewById(R.id.editTextDateCreated);
        linearLayoutDisplayDateCreated.setEnabled(false);

        EditText editTextDateResolved = (EditText) findViewById(R.id.editTextDateResolved);
        editTextDateResolved.setEnabled(false);

        EditText editTextCompletedBy = (EditText) findViewById(R.id.editTextCompletedBy);
        editTextCompletedBy.setEnabled(true);

        // Configure buttons visibility
        Button buttonDisplayRequestCreateMaintenanceLog = (Button) findViewById(R.id.buttonDisplayRequestCreateMaintenanceLog);
        buttonDisplayRequestCreateMaintenanceLog.setVisibility(View.GONE);
        Button buttonDisplayRequestEditRequest = (Button) findViewById(R.id.buttonDisplayRequestEditRequest);
        buttonDisplayRequestEditRequest.setVisibility(View.GONE);
        Button buttonDisplayRequestCreateRequest = (Button) findViewById(R.id.buttonDisplayRequestCreateRequest);
        buttonDisplayRequestCreateRequest.setVisibility(View.GONE);
        Button buttonDisplayRequestSaveEditedRequest = (Button) findViewById(R.id.buttonDisplayRequestSaveEditedRequest);
        buttonDisplayRequestSaveEditedRequest.setVisibility(View.VISIBLE);
    }

    private void configureActivityViewMode() {
        // view-mode
        setTitle("Work - Request Details");

        // Switch do ViewText
        SwitchViewsMode(1);

        // Cannot edit description
        EditText editTextDescriptionOfRequest = (EditText) findViewById(R.id.editTextDescriptionOfRequest);
        editTextDescriptionOfRequest.setEnabled(false);

        // Configure buttons visibility
        Button buttonDisplayRequestCreateMaintenanceLog = (Button) findViewById(R.id.buttonDisplayRequestCreateMaintenanceLog);
        buttonDisplayRequestCreateMaintenanceLog.setVisibility(View.VISIBLE);
        Button buttonDisplayRequestEditRequest = (Button) findViewById(R.id.buttonDisplayRequestEditRequest);
        buttonDisplayRequestEditRequest.setVisibility(View.VISIBLE);
        Button buttonDisplayRequestCreateRequest = (Button) findViewById(R.id.buttonDisplayRequestCreateRequest);
        buttonDisplayRequestCreateRequest.setVisibility(View.GONE);
        Button buttonDisplayRequestSaveEditedRequest = (Button) findViewById(R.id.buttonDisplayRequestSaveEditedRequest);
        buttonDisplayRequestSaveEditedRequest.setVisibility(View.GONE);
    }

    private void configureActivityCreateMode() {
        setTitle("Create Work-Request");

        // Auto-fill date text
        Calendar calendar = Calendar.getInstance();
        EditText editTextDateCreated = (EditText) findViewById(R.id.editTextDateCreated);
        editTextDateCreated.setEnabled(false);
        editTextDateCreated.setText(Utility.convertDateYYYYMMDDToShow(Utility.convertDateToStringRaw(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))));


        EditText editTextDateResolved = (EditText) findViewById(R.id.editTextDateResolved);
        editTextDateResolved.setEnabled(false);

        // Auto-fill created by text
        EditText editTextCreatedBy = (EditText) findViewById(R.id.editTextCreatedBy);
        editTextCreatedBy.setText(User.getInstance().userID);
        editTextCreatedBy.setEnabled(false);

        // Disable completedBy
        EditText editTextCompletedBy = (EditText) findViewById(R.id.editTextCompletedBy);
        editTextCompletedBy.setEnabled(false);

        // Switch do Edits and Spinner
        SwitchViewsMode(0);

        // Hide RequestID field since it's a new work request
        LinearLayout linearLayoutRequestID = (LinearLayout) findViewById(R.id.linearLayoutDisplayRequestRequestID);
        linearLayoutRequestID.setVisibility(View.GONE);

        // Configure buttons visibility: show button for Save (NEW)
        Button buttonDisplayRequestCreateMaintenanceLog = (Button) findViewById(R.id.buttonDisplayRequestCreateMaintenanceLog);
        buttonDisplayRequestCreateMaintenanceLog.setVisibility(View.GONE);
        Button buttonDisplayRequestEditRequest = (Button) findViewById(R.id.buttonDisplayRequestEditRequest);
        buttonDisplayRequestEditRequest.setVisibility(View.GONE);
        Button buttonDisplayRequestCreateRequest = (Button) findViewById(R.id.buttonDisplayRequestCreateRequest);
        buttonDisplayRequestCreateRequest.setVisibility(View.VISIBLE);
        Button buttonDisplayRequestSaveEditedRequest = (Button) findViewById(R.id.buttonDisplayRequestSaveEditedRequest);
        buttonDisplayRequestSaveEditedRequest.setVisibility(View.GONE);
    }

    private void SwitchViewsMode(int viewIndex) {
        ViewSwitcher viewSwitcherRequestID = (ViewSwitcher) findViewById(R.id.viewSwitcherRequestID);
        ViewSwitcher viewSwitcherDateCreated = (ViewSwitcher) findViewById(R.id.viewSwitcherDateCreated);
        ViewSwitcher viewSwitcherDateResolved = (ViewSwitcher) findViewById(R.id.viewSwitcherDateResolved);
        ViewSwitcher viewSwitcherCreatedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherCreatedBy);
        ViewSwitcher viewSwitcherProgress = (ViewSwitcher) findViewById(R.id.viewSwitcherProgress);
        ViewSwitcher viewSwitcherTitle = (ViewSwitcher) findViewById(R.id.viewSwitcherTitle);
        ViewSwitcher viewSwitcherRequestFor = (ViewSwitcher) findViewById(R.id.viewSwitcherRequestFor);
        ViewSwitcher viewSwitcherStatus = (ViewSwitcher) findViewById(R.id.viewSwitcherStatus);
        ViewSwitcher viewSwitcherPriority = (ViewSwitcher) findViewById(R.id.viewSwitcherPriority);
        ViewSwitcher viewSwitcherMachineIsRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMachineIsRequired);
        ViewSwitcher viewSwitcherCompletedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherCompletedBy);
        viewSwitcherRequestID.setDisplayedChild(viewIndex);
        viewSwitcherDateCreated.setDisplayedChild(viewIndex);
        viewSwitcherDateResolved.setDisplayedChild(viewIndex);
        viewSwitcherCreatedBy.setDisplayedChild(viewIndex);
        viewSwitcherProgress.setDisplayedChild(viewIndex);
        viewSwitcherTitle.setDisplayedChild(viewIndex);
        viewSwitcherRequestFor.setDisplayedChild(viewIndex);
        viewSwitcherStatus.setDisplayedChild(viewIndex);
        viewSwitcherPriority.setDisplayedChild(viewIndex);
        viewSwitcherMachineIsRequired.setDisplayedChild(viewIndex);
        viewSwitcherCompletedBy.setDisplayedChild(viewIndex);
    }

    private void fillWorkRequestFields(WorkRequest workRequest) {
        // Fill information regardless if it is a TextView or EditText
        ViewSwitcher viewSwitcherRequestID = (ViewSwitcher) findViewById(R.id.viewSwitcherRequestID);
        TextView viewRequestID = (TextView) viewSwitcherRequestID.getCurrentView();
        viewRequestID.setText(workRequest.getRequestID());

        ViewSwitcher viewSwitcherDateCreated = (ViewSwitcher) findViewById(R.id.viewSwitcherDateCreated);
        TextView viewDateCreated = (TextView) viewSwitcherDateCreated.getCurrentView();
        viewDateCreated.setText(Utility.convertDateYYYYMMDDToShow(workRequest.getDateRequested()));

        ViewSwitcher viewSwitcherDateResolved = (ViewSwitcher) findViewById(R.id.viewSwitcherDateResolved);
        TextView viewDateResolved = (TextView) viewSwitcherDateResolved.getCurrentView();
        viewDateResolved.setText(Utility.convertDateYYYYMMDDToShow(workRequest.getDateResolved()));

        ViewSwitcher viewSwitcherCreatedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherCreatedBy);
        TextView viewCreatedBy = (TextView) viewSwitcherCreatedBy.getCurrentView();
        viewCreatedBy.setText(workRequest.getCreatedBy());

        ViewSwitcher viewSwitcherCompletedBy = (ViewSwitcher) findViewById(R.id.viewSwitcherCompletedBy);
        TextView viewCompletedBy = (TextView) viewSwitcherCompletedBy.getCurrentView();
        viewCompletedBy.setText(workRequest.getCompletedBy());

//        ViewSwitcher viewSwitcherProgress = (ViewSwitcher) findViewById(R.id.viewSwitcherProgress);
//        TextView viewProgress = (TextView) viewSwitcherProgress.getCurrentView();
//        viewProgress.setText(workRequest.getProgress());

//        ViewSwitcher viewSwitcherStatus = (ViewSwitcher) findViewById(R.id.viewSwitcherStatus);
//        TextView viewStatus = (TextView) viewSwitcherStatus.getCurrentView();
//        viewStatus.setText(workRequest.getStatus());

//        ViewSwitcher viewSwitcherPriority = (ViewSwitcher) findViewById(R.id.viewSwitcherPriority);
//        TextView viewPriority = (TextView) viewSwitcherPriority.getCurrentView();
//        viewPriority.setText(workRequest.getPriority());

        ViewSwitcher viewSwitcherTitle = (ViewSwitcher) findViewById(R.id.viewSwitcherTitle);
        TextView viewTitle = (TextView) viewSwitcherTitle.getCurrentView();
        viewTitle.setText(workRequest.getTitle());

        if (!workRequestMode.equals(MODE_VIEW)) {
            int requestForPosition = mAdapterRequestFor.getPosition(workRequest.getRequestFor());
            if (requestForPosition != -1) {
                Spinner spinnerDisplayRequestRequestFor = (Spinner) findViewById(R.id.spinnerDisplayRequestRequestFor);
                spinnerDisplayRequestRequestFor.setSelection(requestForPosition);
            }

            int progressPosition = mAdapterProgress.getPosition(workRequest.getProgress());
            if (progressPosition != -1) {
                Spinner spinnerDisplayRequestProgress = (Spinner) findViewById(R.id.spinnerDisplayRequestProgress);
                spinnerDisplayRequestProgress.setSelection(progressPosition);
            }

            int statusPosition = mAdapterStatus.getPosition(workRequest.getStatus());
            if (statusPosition != -1) {
                Spinner spinnerDisplayRequestStatus = (Spinner) findViewById(R.id.spinnerDisplayRequestStatus);
                spinnerDisplayRequestStatus.setSelection(statusPosition);
            }

            int priorityPosition = mAdapterPriority.getPosition(workRequest.getPriority());
            if (priorityPosition != -1) {
                Spinner spinnerDisplayRequestPriority = (Spinner) findViewById(R.id.spinnerDisplayRequestPriority);
                spinnerDisplayRequestPriority.setSelection(priorityPosition);
            }
        }
        else {
            TextView textViewRequestFor = (TextView) findViewById(R.id.textViewRequestFor);
            textViewRequestFor.setText(workRequest.getRequestFor());

            TextView textViewProgress = (TextView) findViewById(R.id.textViewProgress);
            textViewProgress.setText(workRequest.getProgress());

            TextView textViewStatus = (TextView) findViewById(R.id.textViewStatus);
            textViewStatus.setText(workRequest.getStatus());

            TextView textViewPriority = (TextView) findViewById(R.id.textViewPriority);
            textViewPriority.setText(workRequest.getPriority());
        }

        ViewSwitcher viewSwitcherMachineIsRequired = (ViewSwitcher) findViewById(R.id.viewSwitcherMachineIsRequired);
        TextView viewMachineIsRequired = (TextView) viewSwitcherMachineIsRequired.getCurrentView();
        viewMachineIsRequired.setText(Utility.convertDateYYYYMMDDToShow(workRequest.getDateDue()));

        EditText editTextDescriptionOfRequest = (EditText) findViewById(R.id.editTextDescriptionOfRequest);
        editTextDescriptionOfRequest.setText(workRequest.getDescription());
    }

    private void getWorkRequestFields() {
        // TODO: validate required fields and formats
//        EditText requestIdTextView = (EditText) findViewById(R.id.editTextRequestID);
//        requestIdTextView.getText();

        EditText editTextDateCreated = (EditText) findViewById(R.id.editTextDateCreated);
        mDateCreated = Utility.convertFormattedDateToRaw(editTextDateCreated.getText().toString());

        EditText editTextDateResolved = (EditText) findViewById(R.id.editTextDateResolved);
        mDateResolved = Utility.convertFormattedDateToRaw(editTextDateResolved.getText().toString());

        EditText editTextCreatedBy = (EditText) findViewById(R.id.editTextCreatedBy);
        mCreatedBy = editTextCreatedBy.getText().toString().trim();

        EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        mTitle = editTextTitle.getText().toString().trim();

        Spinner spinnerDisplayRequestProgress = (Spinner) findViewById(R.id.spinnerDisplayRequestProgress);
        mProgress = spinnerDisplayRequestProgress.getSelectedItem().toString().trim();

        mCompletedBy = "";
        
        // if user is editing work request and progress changed to closed
        if (workRequestMode.equals(MODE_EDIT) && mProgress.equals("Closed") && !workRequest.getProgress().equals("Closed")) { // TODO: do not hardcode
            Calendar calendar = Calendar.getInstance();
            mDateResolved = Utility.convertDateToStringRaw(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            mCompletedBy = User.getInstance().userID;
        }

        Spinner spinnerDisplayRequestRequestFor = (Spinner) findViewById(R.id.spinnerDisplayRequestRequestFor);
        mRequestFor = spinnerDisplayRequestRequestFor.getSelectedItem().toString().trim();

        Spinner spinnerDisplayRequestStatus = (Spinner) findViewById(R.id.spinnerDisplayRequestStatus);
        mStatus = spinnerDisplayRequestStatus.getSelectedItem().toString().trim();

        Spinner spinnerDisplayRequestPriority = (Spinner) findViewById(R.id.spinnerDisplayRequestPriority);
        mPriority = spinnerDisplayRequestPriority.getSelectedItem().toString().trim();

        EditText editTextMachineIsRequired = (EditText) findViewById(R.id.editTextMachineIsRequired);
        mMachineIsRequired = Utility.convertFormattedDateToRaw(editTextMachineIsRequired.getText().toString());

        EditText editTextDescriptionOfRequest = (EditText) findViewById(R.id.editTextDescriptionOfRequest);
        mDescription = editTextDescriptionOfRequest.getText().toString().trim();
    }

    private void initListElements() {
        mAdapterRequestFor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterRequestFor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mAdapterProgress = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterProgress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mAdapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mAdapterPriority = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerRequestFor = (Spinner) findViewById(R.id.spinnerDisplayRequestRequestFor);
        spinnerRequestFor.setAdapter(mAdapterRequestFor);

        Spinner spinnerProgress = (Spinner) findViewById(R.id.spinnerDisplayRequestProgress);
        spinnerProgress.setAdapter(mAdapterProgress);

        Spinner spinnerStatus = (Spinner) findViewById(R.id.spinnerDisplayRequestStatus);
        spinnerStatus.setAdapter(mAdapterStatus);

        Spinner spinnerPriority = (Spinner) findViewById(R.id.spinnerDisplayRequestPriority);
        spinnerPriority.setAdapter(mAdapterPriority);

        callAPIwoParam("GetRequestForList");
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
                // this is the very last callback
                mMachine = jsonString;
                setMachineInformation();
                initMaintenanceLogList();
            }

            // verified 2016-04-09
            if (jsonObject.has("createdRequestID")) {
                String createdRequestID = jsonObject.getString("createdRequestID");
                Utility.showToast(this, "Work request created. ID: " + createdRequestID);

                SearchWorkRequest(createdRequestID);
                return;
            }

            if(jsonObject.has("maintenanceList")) {
                //JSONArray jsonArray = jsonObject.getJSONArray("maintenanceLogList");
                //Intent intent = new Intent(this, MaintenanceLogListActivity.class);
                //intent.putExtra(MaintenanceLogListActivity.EXTRA_MAINTENANCE_LOG_LIST, jsonArray.toString());
                //startActivity(intent);
                setMaintenanceLogList(jsonObject.getJSONArray("maintenanceList").toString());
                return;
            }

            if (jsonObject.has("modifiedRequestID")) {
                String modifiedRequestID = jsonObject.getString("modifiedRequestID");
                Utility.showToast(this, "Work Request modified. ID: " + modifiedRequestID);

                SearchWorkRequest(modifiedRequestID);
                return;
            }

            if (jsonObject.has("requestID")) {
                WorkRequest wr = new WorkRequest(jsonObject);
                Intent intent = new Intent(DisplayRequestActivity.this, DisplayRequestActivity.class);
                intent.putExtra(DisplayRequestActivity.EXTRA_REQUEST, wr.createJson());
                intent.putExtra(DisplayRequestActivity.WORK_REQUEST_MODE, DisplayRequestActivity.MODE_VIEW);
                startActivity(intent);
                return;
            }

            if (jsonObject.has("requestForList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("requestForList");
                fillSpinner(jsonArray, LIST_TYPE.REQUESTFOR);
                callAPIwoParam("GetStatusList");
            }

            if (jsonObject.has("status")) {
                JSONArray jsonArray = jsonObject.getJSONArray("status");
                fillSpinner(jsonArray, LIST_TYPE.STATUS);
                callAPIwoParam("GetPriorityList");
            }

            if (jsonObject.has("priority")) {
                JSONArray jsonArray = jsonObject.getJSONArray("priority");
                fillSpinner(jsonArray, LIST_TYPE.PRIORITY);
                callAPIwoParam("GetProgressList");
            }

            if (jsonObject.has("progress")) {
                JSONArray jsonArray = jsonObject.getJSONArray("progress");
                fillSpinner(jsonArray, LIST_TYPE.PROGRESS);

                // This is the last callback called to fill spinners. At this moment all have been populated to we can populate all fields
                if (!workRequestMode.equals(MODE_CREATE)) {
                    // TODO: populate other fields (edits) independently of spinners and then populate each spinner in its own callback
                    fillWorkRequestFields(workRequest);
                }

                // all callbacks received, now we can populate machine info
                initMachineInformation();
            }

        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
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

    private void fillSpinner(JSONArray jsonArray, LIST_TYPE listType) {
        ArrayList<String> items = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }

        switch(listType){
            case PROGRESS:
                mAdapterProgress.clear();
                mAdapterProgress.add(ValueConstants.ITEM_NOTSELECTED);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterProgress.add(items.get(i));
                }
                // set default balue
                Spinner spinnerProgress = (Spinner) findViewById(R.id.spinnerDisplayRequestProgress);
                spinnerProgress.setSelection(1);
                break;
            case STATUS:
                mAdapterStatus.clear();
                mAdapterStatus.add(ValueConstants.ITEM_NOTSELECTED);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterStatus.add(items.get(i));
                }
                break;
            case PRIORITY:
                mAdapterPriority.clear();
                mAdapterPriority.add(ValueConstants.ITEM_NOTSELECTED);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterPriority.add(items.get(i));
                }
                break;
            case REQUESTFOR:
                mAdapterRequestFor.clear();
                mAdapterRequestFor.add(ValueConstants.ITEM_NOTSELECTED);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterRequestFor.add(items.get(i));
                }
                break;
            default:
                Utility.logError("Implementation error");
        }
    }

    public void onClickCreateMaintenanceLog(View view) {
        Intent intent = new Intent(this, DisplayMaintenanceLogActivity.class);
        intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MACHINE, mMachine);
        intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_MODE, DisplayMaintenanceLogActivity.MODE_CREATE);
        intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_RELATED_REQUEST_ID, workRequest.getRequestID());
        intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_MAINTENANCE_REQUIRED, workRequest.getTitle());
        startActivity(intent);
    }

    public void onClickCreateRequestActivityCreateRequest(View view) {
        getWorkRequestFields();
        JSONObject jsonParam = new JSONObject();

        if (isFormInvalid()) return;

        try {
            FillJsonObject(jsonParam);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("CreateWorkRequest", jsonParam);
    }

    public void onClickCreateRequestActivitySaveEditedRequest(View view) {
        getWorkRequestFields();
        JSONObject jsonParam = new JSONObject();

        if (isFormInvalid()) return;

        try {
            FillJsonObject(jsonParam);

            jsonParam.put("requestID", workRequest.getRequestID());
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }

        callAPI("ModifyWorkRequest", jsonParam);
    }

    private boolean isFormInvalid() {
        if(mTitle.equals("") ||
                mProgress.equals("") ||
                mRequestFor.equals("") ||
                mStatus.equals("") ||
                mPriority.equals("") ||
                mMachineIsRequired.equals("") ||
                mDescription.equals("")) {
            Utility.showToast(this, "Please fill all the information");
            return true;
        }
        return false;
    }

    private void FillJsonObject(JSONObject jsonParam) throws JSONException {
        // verified lowercase for create (2016-04-09)
        jsonParam.put("userID", User.getInstance().userID);

        jsonParam.put("machineID", mMachineObject.getMachineID());
        jsonParam.put("createdBy", mCreatedBy);
        jsonParam.put("dateRequested", mDateCreated);
        jsonParam.put("dateResolved", mDateResolved);
        jsonParam.put("dateDue", mMachineIsRequired);
        jsonParam.put("title", mTitle);
        jsonParam.put("completedBy", mCompletedBy);
        jsonParam.put("description", mDescription);
        jsonParam.put("progress", mProgress.equals(ValueConstants.ITEM_NOTSELECTED) ? "" : mProgress);
        jsonParam.put("priority", mPriority.equals(ValueConstants.ITEM_NOTSELECTED) ? "" : mPriority);
        jsonParam.put("status", mStatus.equals(ValueConstants.ITEM_NOTSELECTED) ? "" : mStatus);
        jsonParam.put("requestFor", mRequestFor.equals(ValueConstants.ITEM_NOTSELECTED) ? "" : mRequestFor);
    }

    public void onClickCreateRequestActivityBeginEditRequest(View view) {
        Intent intent = new Intent(DisplayRequestActivity.this, DisplayRequestActivity.class);
        intent.putExtra(DisplayRequestActivity.EXTRA_REQUEST, receivedRequest);
        intent.putExtra(DisplayRequestActivity.WORK_REQUEST_MODE, DisplayRequestActivity.MODE_EDIT);
        startActivity(intent);
    }

    public void onClickDateMachineIsRequired(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = 0, month = 0, day = 0;

        EditText editText = (EditText)findViewById(R.id.editTextMachineIsRequired);
        String dateInTextView = Utility.convertFormattedDateToRaw(editText.getText().toString());

        if (dateInTextView.equals("")){
            dateInTextView = Utility.convertDateToStringRaw(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        }

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EditText editText = (EditText)findViewById(R.id.editTextMachineIsRequired);
                editText.setText(Utility.convertDateToString(year, monthOfYear + 1, dayOfMonth));
            }
        }, Integer.parseInt(dateInTextView.substring(0, 4)), Integer.parseInt(dateInTextView.substring(4, 6)) - 1, Integer.parseInt(dateInTextView.substring(6, 8)));
        datePickerDialog.show();
    }

    public void onClickWorkRequestShowMaintenanceLogs(View view) {
        /*
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("maintenanceLogID", "");
            jsonParam.put("machineID", "");
            jsonParam.put("requestID", workRequest.getRequestID());
            jsonParam.put("creationDateFrom", "");
            jsonParam.put("creationDateTo","" );
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        callAPI("SearchMaintenanceLogList", jsonParam);
        */
    }

    private void initMachineInformation()
    {
        if (workRequestMode == null) {
            return;
        } else if (workRequestMode.equals(MODE_EDIT) || workRequestMode.equals(MODE_VIEW)) {
            String machineId = workRequest.getMachineID();
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
        } else if (workRequestMode.equals(MODE_CREATE)) {
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

    private void initMaintenanceLogList()
    {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            //jsonParam.put("MaintenanceLogID", "");
            //jsonParam.put("MachineID", "");
            jsonParam.put("requestID", workRequest.getRequestID());
            //jsonParam.put("CreationDateFrom", "");
            //jsonParam.put("CreationDateTo","" );
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        callAPI("SearchMaintenanceLogList", jsonParam);
    }

    private void setMaintenanceLogList(String maintenanceLogList)
    {
        ListView listViewItems = (ListView)findViewById(R.id.listViewDisplayRequestMaintenanceList);
        ArrayList<MaintenanceLog> list = new ArrayList<MaintenanceLog>();
        mMaintenanceAdapter = new MaintenanceAdapter(this, R.layout.item_maintenance_log, list);
        listViewItems.setAdapter(mMaintenanceAdapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMaintenance = mMaintenanceAdapter.getItem(position).createJson();
                Intent intent = new Intent(DisplayRequestActivity.this, DisplayMaintenanceLogActivity.class);
                intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MACHINE, mMachine);
                intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MAINTENANCE_LOG, selectedMaintenance);
                intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_MODE, DisplayMaintenanceLogActivity.MODE_VIEW);
                startActivity(intent);
            }
        });


        try {
            JSONArray jsonArray = new JSONArray(maintenanceLogList);
            for (int i = 0; i < jsonArray.length(); i++) {
                mMaintenanceAdapter.add(new MaintenanceLog(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
        listViewItems.setAdapter(mMaintenanceAdapter);

        setListViewHeightBasedOnChildren(listViewItems);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height *=2;  // todo: workaround
        listView.setLayoutParams(params);
    }
}
