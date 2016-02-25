package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;

public class SearchRequestListActivity extends AppCompatActivity {
    public static final String EXTRA_REQUEST_LIST = "ca.on.conestogac.cmms.EXTRA_REQUEST_LIST";
    private RequestAdapter mRequestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_request_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String requestList = getIntent().getStringExtra(EXTRA_REQUEST_LIST);
        if (requestList == null) {
            Utility.logError("unexpedted call");
            throw new IllegalArgumentException();
        } else {
            Utility.logDebug(requestList);
        }
        initListView();
        convertRequestList(requestList);
    }

    private void initListView() {
        // set adapter fo
        // r list view
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        ArrayList<WorkRequest> list = new ArrayList<WorkRequest>();
        mRequestAdapter = new RequestAdapter(this, R.layout.item_search_request, list);
        listViewItems.setAdapter(mRequestAdapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRequest = mRequestAdapter.getItem(position).createJson();
                Intent intent = new Intent(SearchRequestListActivity.this, DisplayRequestActivity.class);
                intent.putExtra(DisplayRequestActivity.EXTRA_REQUEST, selectedRequest);
                startActivity(intent);
            }
        });
    }

    private void convertRequestList(String requestList){
        try {
            JSONArray jsonArray = new JSONArray(requestList);
            for (int i = 0; i < jsonArray.length(); i++) {
                mRequestAdapter.add(new WorkRequest(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public void onClickSortByCreationDate(View view) {
        mRequestAdapter.sort(new ComparatorDate());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
    }

    public void onClickSortByDueDate(View view) {
        mRequestAdapter.sort(new ComparatorDueDate());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
    }

    public void onClickSortByProgress(View view) {
        mRequestAdapter.sort(new ComparatorProgress());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
    }
}

class ComparatorDate implements Comparator<WorkRequest> {
    static Boolean inverse = false;
    @Override
    public int compare(WorkRequest lhs, WorkRequest rhs) {
        int left = Integer.parseInt(lhs.getDateCreated());
        int right = Integer.parseInt(rhs.getDateCreated());
        if(inverse) {
            inverse = false;
            if (left > right) return 1;
            if (left < right) return -1;
        } else {
            inverse = true;
            if (left > right) return -1;
            if (left < right) return 1;
        }
        return 0;
    }
}

class ComparatorDueDate implements Comparator<WorkRequest> {
    static Boolean inverse = false;
    @Override
    public int compare(WorkRequest lhs, WorkRequest rhs) {
        int left = Integer.parseInt(lhs.getDueDate());
        int right = Integer.parseInt(rhs.getDueDate());
        if(inverse) {
            inverse = false;
            if (left > right) return 1;
            if (left < right) return -1;
        } else {
            inverse = true;
            if (left > right) return -1;
            if (left < right) return 1;
        }
        return 0;
    }
}

class ComparatorProgress implements Comparator<WorkRequest> {
    static Boolean inverse = false;
    @Override
    public int compare(WorkRequest lhs, WorkRequest rhs) {
        String left = lhs.getProgress();
        String right = rhs.getProgress();
        if(inverse) {
            inverse = false;
            if(left.compareTo("Closed")==0)return 1;
            return -1;
        } else {
            inverse = true;
            if(left.compareTo("Open")==0)return 1;
            return -1;
        }
        //return 0;
    }
}