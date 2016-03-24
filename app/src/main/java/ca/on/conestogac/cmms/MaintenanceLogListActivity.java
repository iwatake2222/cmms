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

public class MaintenanceLogListActivity extends BaseActivity {
    public static final String EXTRA_MAINTENANCE_LOG_LIST = "ca.on.conestogac.cmms.EXTRA_MAINTENANCELOG_LIST";
    private static final int MENU_ID_SAVE = 1234;
    private MaintenanceAdapter mMaintenanceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_log_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String maintenanceList = getIntent().getStringExtra(EXTRA_MAINTENANCE_LOG_LIST);
        if (maintenanceList == null) {
            Utility.logError("unexpected call");
        } else {
            Utility.logDebug(maintenanceList);
            initListView();
            convertMaintenanceLogList(maintenanceList);
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
        ListView listViewItems = (ListView)findViewById(R.id.listViewMaintenanceList);
        ArrayList<MaintenanceLog> list = new ArrayList<MaintenanceLog>();
        mMaintenanceAdapter = new MaintenanceAdapter(this, R.layout.item_maintenance_log, list);
        listViewItems.setAdapter(mMaintenanceAdapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMaintenance = mMaintenanceAdapter.getItem(position).createJson();
                Intent intent = new Intent(MaintenanceLogListActivity.this, DisplayMaintenanceLogActivity.class);
                intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MACHINE, selectedMaintenance);
                intent.putExtra(DisplayMaintenanceLogActivity.EXTRA_MAINTENANCE_LOG, selectedMaintenance);
                intent.putExtra(DisplayMaintenanceLogActivity.MAINTENANCE_LOG_MODE, DisplayMaintenanceLogActivity.MODE_VIEW);
                startActivity(intent);
            }
        });
    }

    private void convertMaintenanceLogList(String maintenanceLogList){
        try {
            JSONArray jsonArray = new JSONArray(maintenanceLogList);
            for (int i = 0; i < jsonArray.length(); i++) {
                mMaintenanceAdapter.add(new MaintenanceLog(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public void onClickSortByContractor(View view) {
        mMaintenanceAdapter.sort(new ComparatorContractorMaintenanceLog());
        ListView listViewItems = (ListView)findViewById(R.id.listViewMaintenanceList);
        listViewItems.setAdapter(mMaintenanceAdapter);
        ComparatorTitle.inverse = !ComparatorTitle.inverse;
    }

    public void onClickSortByDate(View view) {
        mMaintenanceAdapter.sort(new ComparatorDateMaintenanceLog());
        ListView listViewItems = (ListView)findViewById(R.id.listViewMaintenanceList);
        listViewItems.setAdapter(mMaintenanceAdapter);
        ComparatorDate.inverse = !ComparatorDate.inverse;
    }

    private void save() {
        boolean ret = false;
        String report = "";
        for(int i = 0; i < mMaintenanceAdapter.getCount(); i++) {
            MaintenanceLog ml = mMaintenanceAdapter.getItem(i);
            report += ml.generateReport() + "\n" + "===========" + "\n";
        }
        ret = Utility.saveTextFile(this, "Maintenance", "txt", report);
        if (ret == true) {
            report = MaintenanceLog.generateReportCSVTitle();
            for(int i = 0; i < mMaintenanceAdapter.getCount(); i++) {
                MaintenanceLog ml = mMaintenanceAdapter.getItem(i);
                report += ml.generateReportCSV();
            }
            ret = Utility.saveTextFile(this, "Maintenance", "csv", report);
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

class ComparatorContractorMaintenanceLog implements Comparator<MaintenanceLog> {
    static Boolean inverse = false;
    @Override
    public int compare(MaintenanceLog lhs, MaintenanceLog rhs) {
        if(inverse) {
            return lhs.getContractor().compareToIgnoreCase(rhs.getContractor());
        } else {
            return rhs.getContractor().compareToIgnoreCase(lhs.getContractor());
        }
    }
}

class ComparatorDateMaintenanceLog implements Comparator<MaintenanceLog> {
    static Boolean inverse = false;
    @Override
    public int compare(MaintenanceLog lhs, MaintenanceLog rhs) {
        int left = Integer.parseInt(lhs.getDate());
        int right = Integer.parseInt(rhs.getDate());
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
