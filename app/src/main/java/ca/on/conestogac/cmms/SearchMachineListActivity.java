package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SearchMachineListActivity extends BaseActivity {
    public static final String EXTRA_MACHINE_LIST = "ca.on.conestogac.cmms.EXTRA_MACHINE_LIST";

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
                "\"machineID\":\"1234\", \"campus\":\"Doon\", \"shop\":\"Wood Working\", \"isDisposed\":\"0\", \"manufacturer\":\"ABC Inc\", \"serialNumber\":\"Serial 123\", \"modelNumber\":\"Model 123\", \"description\":\"Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine Huge machine \", \"linkToPicture\":\"http://www.wmma.org/images/productguide-01.jpg\", \"linkToDocument\":[{\"documentName\":\"Operation Manual\", \"link\":\"http://www.vieltools.com/instruction/D2796_pdf.pdf\"}, {\"documentName\":\"Safety Alert\", \"link\":\"http://iapa.ca/pdf/machine.pdf\"}]\n" +
                "}");
        startActivity(intent);
        startActivity(intent);


    }


    @Override
    void onAPIResponse(String jsonString) {


    }

}
