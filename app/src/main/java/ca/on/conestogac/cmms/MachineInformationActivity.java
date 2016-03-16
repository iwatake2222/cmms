package ca.on.conestogac.cmms;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
        createDocumentLinks();
    }


    private void createDocumentLinks() {
        final int MAX_DOCUMENT_NUM = 8;
        int[] resId = {
                R.id.ids_document_button0,
                R.id.ids_document_button1,
                R.id.ids_document_button2,
                R.id.ids_document_button3,
                R.id.ids_document_button4,
                R.id.ids_document_button5,
                R.id.ids_document_button6,
                R.id.ids_document_button7,
        };
        LinearLayout linearlayout = (LinearLayout)findViewById(R.id.linearLayoutMachineDocuments);
        for(int i = 0; i < mMachine.getLinksToDocument().size(); i++) {
            if(i==MAX_DOCUMENT_NUM)break;
            Button button = new Button(this);
            button.setId(resId[i]);
            button.setText(mMachine.getNamesOfDocument().get(i));
            linearlayout.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()){
                        case R.id.ids_document_button0:
                            gotoDocument(mMachine.getLinksToDocument().get(0));
                            break;
                        case R.id.ids_document_button1:
                            gotoDocument(mMachine.getLinksToDocument().get(1));
                            break;
                        case R.id.ids_document_button2:
                            gotoDocument(mMachine.getLinksToDocument().get(2));
                            break;
                        case R.id.ids_document_button3:
                            gotoDocument(mMachine.getLinksToDocument().get(3));
                            break;
                        case R.id.ids_document_button4:
                            gotoDocument(mMachine.getLinksToDocument().get(4));
                            break;
                        case R.id.ids_document_button5:
                            gotoDocument(mMachine.getLinksToDocument().get(5));
                            break;
                        case R.id.ids_document_button6:
                            gotoDocument(mMachine.getLinksToDocument().get(6));
                            break;
                        case R.id.ids_document_button7:
                            gotoDocument(mMachine.getLinksToDocument().get(7));
                            break;
                    }
                }
            });
        }
    }

    private void gotoDocument(String url) {
        //Utility.showToast(MachineInformationActivity.this, url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("http://docs.google.com/viewer?url=" + url), "text/html");
        startActivity(intent);
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


}
