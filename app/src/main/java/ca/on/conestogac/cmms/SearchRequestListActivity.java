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

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class SearchRequestListActivity extends BaseActivity {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // todo
        menu.add(Menu.NONE, 123, Menu.NONE, "Save");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // todo
            case 123:
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

    // todo
    private void save() {
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date(System.currentTimeMillis());
        String fileName = "WorkRequest_" + df.format(date) + ".txt";
        Utility.logDebug(fileName);
        String str="abc";
        try {
            OutputStream out = openFileOutput(fileName, MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.append(str);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    void onAPIResponse(String jsonString) {
    }


}

class ComparatorDate implements Comparator<WorkRequest> {
    static Boolean inverse = false;
    @Override
    public int compare(WorkRequest lhs, WorkRequest rhs) {
        int left = Integer.parseInt(lhs.getDateCreated());
        int right = Integer.parseInt(rhs.getDateCreated());
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
        int left = Integer.parseInt(lhs.getDueDate());
        int right = Integer.parseInt(rhs.getDueDate());
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