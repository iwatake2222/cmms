package ca.on.conestogac.cmms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchMachineActivity extends BaseActivity {
    private enum LIST_TYPE {
        CAMPUS,
        SHOP,
        STATUS,
    }
    private String mCampus;
    private String mShop;
    private String mStatus;
    private ArrayAdapter<String> mAdapterCampus;
    private ArrayAdapter<String> mAdapterShop;
    private ArrayAdapter<String> mAdapterStatus;
    static private JSONObject mApiPrm; // just to pass parameter to dialog. todo: should use bundle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_machine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initListElements();
    }

    public void onClickSearchMachineByQR(View view) {
        Intent intent = new Intent(this, SearchMachineQRActivity.class);
        startActivity(intent);
    }

    public void onClickSearchMachineByKeywords(View view) {
        EditText editTextKeywords = (EditText)findViewById(R.id.editTextSearchMachineKeywords);
        EditText editTextMachineID = (EditText)findViewById(R.id.editTextSearchMachineID);
        if(mCampus.compareTo(ValueConstants.ITEM_ANY)==0)mCampus="";
        if(mShop.compareTo(ValueConstants.ITEM_ANY)==0)mShop="";
        if(mStatus.compareTo(ValueConstants.ITEM_ANY)==0)mStatus="";
        String keywords = editTextKeywords.getText().toString().trim();

        if(editTextMachineID.getText().toString().trim().isEmpty()) {
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("userID", User.getInstance().userID);
                if(mCampus.compareTo("")!=0) jsonParam.put("campus", mCampus);
                if(mShop.compareTo("")!=0) jsonParam.put("shop", mShop);
                if(mStatus.compareTo("")!=0) jsonParam.put("machineStatus", mStatus);
                if(keywords.compareTo("")!=0) jsonParam.put("keyword", keywords);
                //if(mCampus.compareTo("")!=0) jsonParam.put("MachineID","dummy"); // todo delete

                if(mShop.compareTo("")==0) {
                    SearchMachineActivity.mApiPrm = jsonParam;      // todo: should use bundle
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder
                            .setTitle("Are you sure?" )
                            .setMessage("It may take some time to get information without setting Shop name or ID" )
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    callAPI("SearchMachineList", SearchMachineActivity.mApiPrm );
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    callAPI("SearchMachineList", jsonParam);
                }
            } catch (JSONException e) {
                Utility.logDebug(e.getMessage());
            }
        } else {
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("userID", User.getInstance().userID);
                jsonParam.put("machineID", editTextMachineID.getText().toString().trim());
                callAPI("searchMachine", jsonParam);
            } catch (JSONException e) {
                Utility.logDebug(e.getMessage());
            }
        }
    }


    @Override
    void onAPIResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");
            // todo check ret code, after server returns correct return code
            if (result.compareTo(ValueConstants.RET_OK) != 0  && result.compareTo("nok") != 0) {
                // do something if needed when error happens
            } else {
                if (jsonObject.has("machineList")) {
                    /* if response contains "machineList", I assume I sent SearchWorkRequestList */
                    // to avoid memory error, set machine list directry instead of using intent
                    //JSONArray jsonArray = jsonObject.getJSONArray("machineList");
                    Intent intent = new Intent(this, SearchMachineListActivity.class);
                    //intent.putExtra(SearchMachineListActivity.EXTRA_MACHINE_LIST, jsonArray.toString());
                    SearchMachineListActivity.mMachineList = jsonObject.getJSONArray("machineList").toString();
                    startActivity(intent);
                } else if (jsonObject.has("machineID")) {
                    /* if response contains "title", I assume I sent SearchWorkRequest */
                    Intent intent = new Intent(this, MachineInformationActivity.class);
                    intent.putExtra(MachineInformationActivity.EXTRA_MACHINE, jsonObject.toString());
                    startActivity(intent);
                } else if (jsonObject.has("campusName")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("campusName");
                    setList(jsonArray, LIST_TYPE.CAMPUS);
                } else if (jsonObject.has("shopName")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("shopName");
                    setList(jsonArray, LIST_TYPE.SHOP);
                } else if (jsonObject.has("machineStatus")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("machineStatus");
                    setList(jsonArray, LIST_TYPE.STATUS);
                } else {
                    Utility.showToast(this, "No Result");
                }
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void initListElements() {
        mAdapterCampus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterCampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterShop = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerCampus = (Spinner)findViewById(R.id.spinnerSearchMachineCampus);
        spinnerCampus.setAdapter(mAdapterCampus);
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mCampus = item;
                if (mCampus.compareTo(ValueConstants.ITEM_ANY) != 0) {
                    callAPIforShop(); // refresh mShop list
                } else {
                    mAdapterShop.clear();
                    mAdapterShop.add(ValueConstants.ITEM_ANY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerShop = (Spinner)findViewById(R.id.spinnerSearchMachineShop);
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

        Spinner spinnerStatus = (Spinner)findViewById(R.id.spinnerSearchMachineStatus);
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



        callAPIwoParam("GetMachineStatusList");
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
                mAdapterCampus.add(ValueConstants.ITEM_ANY);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterCampus.add(items.get(i));
                }
                break;
            case SHOP:
                mAdapterShop.clear();
                mAdapterShop.add(ValueConstants.ITEM_ANY);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterShop.add(items.get(i));
                }
                break;
            case STATUS:
                mAdapterStatus.clear();
                mAdapterStatus.add(ValueConstants.ITEM_ANY);
                for (int i = 0; i < items.size(); i++) {
                    mAdapterStatus.add(items.get(i));
                }
                callAPIwoParam("GetCampusList");
                break;
            default:
                Utility.logError("Implementation error");
        }
    }

    private void callAPIforShop() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("campusName", mCampus);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        callAPI("GetShopList", jsonParam);
    }
}
