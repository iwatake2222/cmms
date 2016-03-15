package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SearchMachineListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_machine_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickSearchMachineList(View view) {
        // todo: delete
        Intent intent = new Intent(SearchMachineListActivity.this, MachineInformationActivity.class);
        intent.putExtra(MachineInformationActivity.EXTRA_MACHINE, "{\"result\":\"ok\", \n" +
                "\"machineID\":\"1234\", \"campus\":\"Doon\", \"shop\":\"Wood Working\", \"isDisposed\":\"0\", \"make\":\"ABC Inc\", \"model\":\"Model 123\", \"description\":\"Huge machine\", \"linkToPicture\":\"http://aaa\", \"linkToDocument\":[{\"documentName\":\"Operation Manual\", \"link\":\"http://aaa\"}, {\"documentName\":\"Safety Alert\", \"link\":\"http://aaa\"}]\n" +
                "}");
        startActivity(intent);

    }


    @Override
    void onAPIResponse(String jsonString) {

    }

}
