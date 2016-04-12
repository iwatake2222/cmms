package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;

public class SearchRequestListActivity extends BaseActivity {
    public static final String EXTRA_REQUEST_LIST = "ca.on.conestogac.cmms.EXTRA_REQUEST_LIST";
    private static final int MENU_ID_SAVE = 1234;
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
        } else {
            Utility.logDebug(requestList);
            initListView();
            convertRequestList(requestList);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_SAVE, Menu.NONE, "Save");
        MenuItem item = menu.findItem(MENU_ID_SAVE);
        item.setIcon(android.R.drawable.ic_menu_save);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_SAVE:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListView() {
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
                intent.putExtra(DisplayRequestActivity.WORK_REQUEST_MODE, DisplayRequestActivity.MODE_VIEW);
                startActivity(intent);
            }
        });
    }

    private void convertRequestList(String requestList){
        try {
            JSONArray jsonArray = new JSONArray(requestList);
            // set list in the reverse way so that the latest item goes top
            //for (int i = 0; i < jsonArray.length(); i++) {
            for (int i = jsonArray.length() - 1; i >= 0 ; i--) {
                mRequestAdapter.add(new WorkRequest(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public void onClickSortByTitle(View view) {
        mRequestAdapter.sort(new ComparatorTitle());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
        ComparatorTitle.inverse = !ComparatorTitle.inverse;
    }

    public void onClickSortByCreationDate(View view) {
        mRequestAdapter.sort(new ComparatorDate());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
        ComparatorDate.inverse = !ComparatorDate.inverse;
    }

    public void onClickSortByDueDate(View view) {
        mRequestAdapter.sort(new ComparatorDueDate());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
        ComparatorDueDate.inverse = !ComparatorDueDate.inverse;
    }

    public void onClickSortByProgress(View view) {
        mRequestAdapter.sort(new ComparatorProgress());
        ListView listViewItems = (ListView)findViewById(R.id.listViewRequestList);
        listViewItems.setAdapter(mRequestAdapter);
        ComparatorProgress.inverse = !ComparatorProgress.inverse;
    }

    private void save() {
        boolean ret = false;
        String report = "";
        for(int i = 0; i < mRequestAdapter.getCount(); i++) {
            WorkRequest wr = mRequestAdapter.getItem(i);
            report += wr.generateReport() + "\n" + "===========" + "\n";
        }
        ret = Utility.saveTextFile(this, "Request", "txt", report);
        if (ret == true) {
            report = WorkRequest.generateReportCSVTitle();
            for(int i = 0; i < mRequestAdapter.getCount(); i++) {
                WorkRequest wr = mRequestAdapter.getItem(i);
                report += wr.generateReportCSV();
            }
            ret = Utility.saveTextFile(this, "Request", "csv", report);
        }
        if(ret == true) {
            Utility.showToast(this, "Saved report");
        } else{
            Utility.showToast(this, "Failed to save report");
        }
    }

    @Override
    void onAPIResponse(String jsonString) {
        // something else again
    }



}

class ComparatorTitle implements Comparator<WorkRequest> {
    static Boolean inverse = false;
    @Override
    public int compare(WorkRequest lhs, WorkRequest rhs) {
        if(inverse) {
            return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
        } else {
            return rhs.getTitle().compareToIgnoreCase(lhs.getTitle());
        }
    }
}

class ComparatorDate implements Comparator<WorkRequest> {
    static Boolean inverse = false;
    @Override
    public int compare(WorkRequest lhs, WorkRequest rhs) {
        int left;
        try {
            left = Integer.parseInt(lhs.getDateRequested());
        } catch (Exception e){
            left = 0;
        }
        int right;
        try {
            right = Integer.parseInt(rhs.getDateRequested());
        } catch (Exception e){
            right = 0;
        }

        if(inverse) {
            if (left > right) return 1;
            if (left < right) return -1;
        } else {
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
        int left;
        try {
            left = Integer.parseInt(lhs.getDateDue());
        } catch (Exception e){
            left = 0;
        }
        int right;
        try {
            right = Integer.parseInt(rhs.getDateDue());
        } catch (Exception e){
            right = 0;
        }
        if(inverse) {
            if (left > right) return 1;
            if (left < right) return -1;
        } else {
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
            if(left.compareTo("Open")==0) {
                if (right.compareTo("Open") == 0) return 0;
                if (right.compareTo("Working") == 0) return 1;
                if (right.compareTo("Closed") == 0) return 1;
            } else if(left.compareTo("Working")==0) {
                if (right.compareTo("Open") == 0) return -1;
                if (right.compareTo("Working") == 0) return 0;
                if (right.compareTo("Closed") == 0) return 1;
            } else {
                if(left.compareTo("Closed")==0) {
                    if (right.compareTo("Open") == 0) return -1;
                    if (right.compareTo("Working") == 0) return -1;
                    if (right.compareTo("Closed") == 0) return 0;
                }
            }
        } else {
            if(left.compareTo("Open")==0) {
                if (right.compareTo("Open") == 0) return 0;
                if (right.compareTo("Working") == 0) return -1;
                if (right.compareTo("Closed") == 0) return -1;
            } else if(left.compareTo("Working")==0) {
                if (right.compareTo("Open") == 0) return 1;
                if (right.compareTo("Working") == 0) return 0;
                if (right.compareTo("Closed") == 0) return -1;
            } else {
                if(left.compareTo("Closed")==0) {
                    if (right.compareTo("Open") == 0) return 1;
                    if (right.compareTo("Working") == 0) return 1;
                    if (right.compareTo("Closed") == 0) return 0;
                }
            }
        }
        return 0;
    }
}