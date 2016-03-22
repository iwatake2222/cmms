package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
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
    }
    private String mCampus;
    private String mShop;
    private String mDisposed;
    private ArrayAdapter<String> mAdapterCampus;
    private ArrayAdapter<String> mAdapterShop;
    private ArrayAdapter<String> mAdapterDisposed;

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
        if(mDisposed.compareTo("No")==0) {
            mDisposed="0";
        } else if(mDisposed.compareTo("Yes")==0) {
            mDisposed="1";
        } else {
            mDisposed="";
        }

        if(editTextMachineID.getText().toString().trim().isEmpty()) {
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("userID", User.getInstance().userID);
                jsonParam.put("campus", mCampus);
                jsonParam.put("shop", mShop);
                jsonParam.put("keywords", editTextKeywords.getText().toString().trim());
                jsonParam.put("MachineID","dummy"); // todo delete
                if(mDisposed.compareTo("") != 0) {
                    jsonParam.put("isDisposed",mDisposed);
                }
                callAPI("SearchMachineList", jsonParam);
            } catch (JSONException e) {
                Utility.logDebug(e.getMessage());
            }
        } else {
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("userID", User.getInstance().userID);
                jsonParam.put("machineID", editTextMachineID.getText().toString().trim());
                callAPI("SearchMachine", jsonParam);
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
            if (result.compareTo(ValueConstants.RET_OK) != 0 ) {
                // do something if needed when error happens
            } else {
                if (jsonObject.has("machineList")) {
                    /* if response contains "machineList", I assume I sent SearchWorkRequestList */
                    JSONArray jsonArray = jsonObject.getJSONArray("machineList");
                    Intent intent = new Intent(this, SearchMachineListActivity.class);
                    intent.putExtra(SearchMachineListActivity.EXTRA_MACHINE_LIST, jsonArray.toString());
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
        mAdapterDisposed = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAdapterDisposed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

        Spinner spinnerDisposed = (Spinner)findViewById(R.id.spinnerSearchMachineDisposed);
        spinnerDisposed.setAdapter(mAdapterDisposed);
        spinnerDisposed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mDisposed = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // spinners with static values
        mAdapterDisposed.clear();
        mAdapterDisposed.add(ValueConstants.ITEM_ANY);
        mAdapterDisposed.add("No");
        mAdapterDisposed.add("Yes");

        // spinners with dynamic values
        callAPIwoParam("GetCampusList");
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
            default:
                Utility.logError("Implementation error");
        }
    }

    private void callAPIforShop() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("campus", mCampus);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        callAPI("GetShopList", jsonParam);
    }
}
