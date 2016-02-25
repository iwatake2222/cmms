package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SearchMachineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_machine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickSearchMachineByQR(View view) {
        Intent intent = new Intent(this, SearchMachineQRActivity.class);
        startActivity(intent);
    }

    public void onClickSearchMachineByKeywords(View view) {
        Intent intent = new Intent(this, SearchMachineListActivity.class);
        startActivity(intent);
    }


    @Override
    void onAPIResponse(String jsonString) {

    }

}
