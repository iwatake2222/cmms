package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SearchRequestListActivity extends AppCompatActivity {
    public static final String EXTRA_REQUEST_LIST = "ca.on.conestogac.cmms.EXTRA_REQUEST_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_request_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String requestList = getIntent().getStringExtra(EXTRA_REQUEST_LIST);
        if (requestList == null) {
            DebugUtility.logError("unexpedted call");
            throw new IllegalArgumentException();
        } else {
            DebugUtility.logDebug(requestList);
        }
    }

    public void onClickSelect(View view) {
        Intent intent = new Intent(this, DisplayRequestActivity.class);
        startActivity(intent);
    }
}
