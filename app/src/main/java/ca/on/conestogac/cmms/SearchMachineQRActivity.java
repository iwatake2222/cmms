package ca.on.conestogac.cmms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchMachineQRActivity extends BaseActivity {
    private CompoundBarcodeView mBarcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_machine_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utility.getPermissionCamera(this);

        mBarcodeView = (CompoundBarcodeView)findViewById(R.id.barcodeView);
        mBarcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult barcodeResult) {
                Utility.logDebug(barcodeResult.getText());
                final String readData = barcodeResult.getText();
                new AlertDialog.Builder(SearchMachineQRActivity.this)
                        .setTitle("Get information?")
                        .setMessage("Machine ID: " + readData)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject jsonParam = new JSONObject();
                                try{
                                    jsonParam.put("userID", User.getInstance().userID);
                                    jsonParam.put("machineID", readData);
                                } catch (JSONException e) {
                                    Utility.logDebug(e.getMessage());
                                }
                                callAPI("SearchMachine", jsonParam);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> list) {
                // do nothing
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBarcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBarcodeView.pause();
    }

    @Override
    void onAPIResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");
            if (result.compareTo(ValueConstants.RET_OK) != 0 ) {
                // do something if needed when error happens
            } else {
                if (jsonObject.has("machineID")) {
                    Intent intent = new Intent(SearchMachineQRActivity.this, MachineInformationActivity.class);
                    intent.putExtra(MachineInformationActivity.EXTRA_MACHINE, jsonString);
                    startActivity(intent);
                } else {
                    Utility.showToast(this, "No Result");
                }

            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

}
