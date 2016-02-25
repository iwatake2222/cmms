package ca.on.conestogac.cmms;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchRequestActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String>  {
    private enum LIST_TYPE {
        CAMPUS,
        SHOP,
        PROGRESS,
        STATUS,
        PRIORITY,
    }

    private String mCampus;
    private String mShop;
    private String mProgress;
    private String mStatus;
    private String mPriority;
    private String mFrom;
    private String mTo;
    private ArrayAdapter<String> mAdapterCampus;
    private ArrayAdapter<String> mAdapterShop;
    private ArrayAdapter<String> mAdapterProgress;
    private ArrayAdapter<String> mAdapterStatus;
    private ArrayAdapter<String> mAdapterPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDateElements();
        initListElements();

        // todo: delete sample
        /*
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://cmmsmock.apphb.com/Service1.svc/machinepost");
        bundle.putString("method", "POST");
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("MachineID", "3432");
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        String param = jsonParam.toString();

        bundle.putString("param", param);
        getSupportLoaderManager().restartLoader(0, bundle, this);
        */
    }

    public void onClickSearch(View view) {
        EditText editTextKeywords = (EditText)findViewById(R.id.editTextSearchRequestKeywords);
        if(mCampus.compareTo("Any")==0)mCampus="";
        if(mShop.compareTo("Any")==0)mShop="";
        if(mProgress.compareTo("Any")==0)mProgress="";
        if(mStatus.compareTo("Any")==0)mStatus="";
        if(mPriority.compareTo("Any")==0)mPriority="";
        // call Web API
        Bundle bundle = new Bundle();
        bundle.putString("url", ValueConstants.SERVER_URL + "SearchWorkRequest");
        bundle.putString("method", "POST");
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("requestID", "");
            jsonParam.put("machineID", "");
            jsonParam.put("campus", mCampus);
            jsonParam.put("shop", mShop);
            jsonParam.put("progress",mProgress );
            jsonParam.put("status", mStatus);
            jsonParam.put("creationDateFrom", mFrom);
            jsonParam.put("creationDateTo", mTo);
            jsonParam.put("keywords", editTextKeywords.getText().toString().trim());
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        String param = jsonParam.toString();
        /*
        bundle.putString("method", "GET");
        String param = "userID=" + User.getInstance().userID;
        param += "&requestID=null";
        param += "&machineID=null";
        param += "&campus=" + mCampus;
        param += "&shop=" + mShop;
        param += "&progress=" + mProgress;
        param += "&status=" + mStatus;
        param += "&creationDateFrom=" + mFrom;
        param += "&creationDateTo=" + mTo;
        try {
            param += "&keywords=" + URLEncoder.encode(editTextKeywords.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Utility.logError(e.getMessage());
        }
        */
        bundle.putString("param", param);
        getSupportLoaderManager().restartLoader(0, bundle, this);

        //todo: for test
        Intent intent = new Intent(this, SearchRequestListActivity.class);
        intent.putExtra(SearchRequestListActivity.EXTRA_REQUEST_LIST,"[{\"requestID\":\"req123\",\"machineID\":\"1234\", \"campus\":\"Doon\",\"shop\":\"WoodWorking\",\"dateCreated\":\"20160201\", \"createdBy\":\"Alice\", \"progress\":\"Open\", \"title\":\"Shaft broken\", \"requestFor\":\"Maintenance\", \"status\":\"Safety issue\", \"priority\":\"High\", \"dueDate\":\"20160401\", \"description\":\"hoge hoge hoge\", \"relatedMaintenanceLogList\":[\"log1234\", \"log5677\"]},{\"requestID\":\"req456\",\"machineID\":\"1234\", \"campus\":\"Doon\",\"shop\":\"WoodWorking\",\"dateCreated\":\"20160101\", \"createdBy\":\"Alice\", \"progress\":\"Closed\", \"title\":\"motor broken\", \"requestFor\":\"Maintenance\", \"status\":\"Safety issue\", \"priority\":\"High\", \"dueDate\":\"20160401\", \"description\":\"fuga fuga fuga\", \"relatedMaintenanceLogList\":[\"log1234\", \"log5677\"]}]");
        startActivity(intent);
    }

    private void retrieveRequestList(JSONArray jsonArray) {
        Utility.logDebug(jsonArray.toString());
        // Start SearchRequestListActivity
        Intent intent = new Intent(this, SearchRequestListActivity.class);
        intent.putExtra(SearchRequestListActivity.EXTRA_REQUEST_LIST, jsonArray.toString());
        startActivity(intent);
    }

    private void initDateElements() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);    // 0 - 11
        month++;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mFrom = Utility.convertDateToStringRaw(year - 1, month, day);
        EditText editTextFrom = (EditText)findViewById(R.id.editTextSearchRequestFrom);
        editTextFrom.setText(Utility.convertDateToString(year - 1, month, day));
        mTo = Utility.convertDateToStringRaw(year, month, day);
        EditText editTextTo = (EditText)findViewById(R.id.editTextSearchRequestTo);
        editTextTo.setText(Utility.convertDateToString(year, month, day));
    }

    public void onClickFrom(View view) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mFrom = Utility.convertDateToStringRaw(year, monthOfYear, dayOfMonth);
                EditText editText = (EditText)findViewById(R.id.editTextSearchRequestFrom);
                editText.setText(Utility.convertDateToString(year, monthOfYear, dayOfMonth));
            }
        }, Integer.parseInt(mFrom.substring(0, 4)), Integer.parseInt(mFrom.substring(4, 6)), Integer.parseInt(mFrom.substring(6, 8)));
        datePickerDialog.show();
    }

    public void onClickTo(View view) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTo = Utility.convertDateToStringRaw(year, monthOfYear, dayOfMonth);
                EditText editText = (EditText)findViewById(R.id.editTextSearchRequestTo);
                editText.setText(Utility.convertDateToString(year, monthOfYear, dayOfMonth));
            }
        }, Integer.parseInt(mTo.substring(0, 4)), Integer.parseInt(mTo.substring(4, 6)), Integer.parseInt(mTo.substring(6, 8)));
        datePickerDialog.show();
    }

    private void initListElements() {
        mAdapterCampus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterCampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterShop = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterProgress = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterProgress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterPriority = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerCampus = (Spinner)findViewById(R.id.spinnerSearchRequestCampus);
        spinnerCampus.setAdapter(mAdapterCampus);
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mCampus = item;
                if(mCampus.compareTo("Any") != 0){
                    callAPIforShop("GetShopList", mCampus); // refresh mShop list
                } else {
                    mAdapterShop.clear();
                    mAdapterShop.add("Any");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerShop = (Spinner)findViewById(R.id.spinnerSearchRequestShop);
        spinnerShop.setAdapter(mAdapterShop);
        spinnerShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mShop = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Spinner spinnerProgress = (Spinner)findViewById(R.id.spinnerSearchRequestProgress);
        spinnerProgress.setAdapter(mAdapterProgress);
        spinnerProgress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mProgress = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerStatus = (Spinner)findViewById(R.id.spinnerSearchRequestStatus);
        spinnerStatus.setAdapter(mAdapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mStatus = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerPriority = (Spinner)findViewById(R.id.spinnerSearchRequestPriority);
        spinnerPriority.setAdapter(mAdapterPriority);
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mPriority = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // todo: delete
        initCampusList();
        initShopList();
        initProgressList();
        initStatusList();
        initPriorityList();

        // need to call by serial
        // call GetCampusList at the last to avoid double call
        // GetProgressList -> GetStatusList -> GetPriorityList -> GetCampusList -> GetShopList
        callAPI("GetProgressList");
    }

    private void callAPI(String API){
        Bundle bundle = new Bundle();
        bundle.putString("url", ValueConstants.SERVER_URL + API);
        bundle.putString("method", "POST");
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        String param = jsonParam.toString();
        /*
        bundle.putString("method", "GET");
        String param = "userID=" + User.getInstance().userID;
        */
        bundle.putString("param", param);
        getSupportLoaderManager().restartLoader(0, bundle, this);
    }

    private void callAPIforShop(String API, String campustName){
        Bundle bundle = new Bundle();
        bundle.putString("url", ValueConstants.SERVER_URL + API);
        bundle.putString("method", "POST");
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("campustName", campustName);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        String param = jsonParam.toString();
        /*
        bundle.putString("method", "GET");
        String param = "userID=" + User.getInstance().userID + "&campustName=" + campustName;
        */
        bundle.putString("param", param);
        getSupportLoaderManager().restartLoader(0, bundle, this);
    }

    private void setList(JSONArray jsonArray, LIST_TYPE listType) {
        ArrayList<String> items = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }

        switch(listType){
            case CAMPUS:
                mAdapterCampus.clear();
                mAdapterCampus.add("Any");
                for (int i = 0; i < items.size(); i++) {
                    mAdapterCampus.add(items.get(i));
                }
                // GetShopList is called when CAMPUS list set
                //callAPIforShop("GetShopList", aaa);
                break;
            case SHOP:
                mAdapterShop.clear();
                mAdapterShop.add("Any");
                for (int i = 0; i < items.size(); i++) {
                    mAdapterShop.add(items.get(i));
                }
                break;
            case PROGRESS:
                mAdapterProgress.clear();
                mAdapterProgress.add("Any");
                for (int i = 0; i < items.size(); i++) {
                    mAdapterProgress.add(items.get(i));
                }
                callAPI("GetStatusList");
                break;
            case STATUS:
                mAdapterStatus.clear();
                mAdapterStatus.add("Any");
                for (int i = 0; i < items.size(); i++) {
                    mAdapterStatus.add(items.get(i));
                }
                callAPI("GetPriorityList");
                break;
            case PRIORITY:
                mAdapterPriority.clear();
                mAdapterPriority.add("Any");
                for (int i = 0; i < items.size(); i++) {
                    mAdapterPriority.add(items.get(i));
                }
                callAPI("GetCampusList");
                break;
            default:
                Utility.logError("Implementation error");
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        HttpAsyncLoader loader = new HttpAsyncLoader(this, args.getString("url"), args.getString("method"), args.getString("param"));
        //Utility.logDebug("onCreateLoader: " + args.getString("url"));
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if ( loader.getId() == 0 ) {
            if (data != null) {
                Utility.logDebug(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String result = jsonObject.getString("result");
                    if (result.compareTo(ValueConstants.RET_OK) != 0 ) {
                        Utility.logError(result);
                        // todo: error message
                    }
                    if(jsonObject.has("campusName")){
                        JSONArray jsonArray = jsonObject.getJSONArray("campusName");
                        setList(jsonArray, LIST_TYPE.CAMPUS);
                    }
                    if(jsonObject.has("shopName")){
                        JSONArray jsonArray = jsonObject.getJSONArray("shopName");
                        setList(jsonArray, LIST_TYPE.SHOP);
                    }
                    if(jsonObject.has("progress")){
                        JSONArray jsonArray = jsonObject.getJSONArray("progress");
                        setList(jsonArray, LIST_TYPE.PROGRESS);
                    }
                    if(jsonObject.has("status")){
                        JSONArray jsonArray = jsonObject.getJSONArray("status");
                        setList(jsonArray, LIST_TYPE.STATUS);
                    }
                    if(jsonObject.has("priority")){
                        JSONArray jsonArray = jsonObject.getJSONArray("priority");
                        setList(jsonArray, LIST_TYPE.PRIORITY);
                    }
                    if(jsonObject.has("requestList")){
                        JSONArray jsonArray = jsonObject.getJSONArray("requestList");
                        retrieveRequestList(jsonArray);
                    }
                } catch (JSONException e) {
                    Utility.logError(e.getMessage());
                }
            } else {
                Utility.logError("");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // do nothing
    }



    // todo: delete
    private void initCampusList() {
        mAdapterCampus.clear();
        mAdapterCampus.add("Any");
        mAdapterCampus.add("Doon");
        mAdapterCampus.add("Cambridge");
        mAdapterCampus.add("Waterloo");
    }

    private void initShopList() {
        mAdapterShop.clear();
        mAdapterShop.add("Any");
        mAdapterShop.add("Wood Working");
        mAdapterShop.add("ShopB");
        mAdapterShop.add("ShopC");
    }

    private void initProgressList() {
        mAdapterProgress.clear();
        mAdapterProgress.add("Any");
        mAdapterProgress.add("Open");
        mAdapterProgress.add("Working");
        mAdapterProgress.add("Closed");
    }

    private void initStatusList() {
        mAdapterStatus.clear();
        mAdapterStatus.add("Any");
        mAdapterStatus.add("Safety issue");
        mAdapterStatus.add("Safety level lock-out");
        mAdapterStatus.add("Appears normal repair");
    }

    private void initPriorityList() {
        mAdapterPriority.clear();
        mAdapterPriority.add("Any");
        mAdapterPriority.add("High");
        mAdapterPriority.add("Medium");
        mAdapterPriority.add("Low");
    }
}
