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

public class SearchMachineListActivity extends BaseActivity {
    public static final String EXTRA_MACHINE_LIST = "ca.on.conestogac.cmms.EXTRA_MACHINE_LIST";
    private static final int MENU_ID_SAVE = 5678;
    private MachineAdapter mMachineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_machine_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String machineList = getIntent().getStringExtra(EXTRA_MACHINE_LIST);
        if (machineList == null) {
            Utility.logError("unexpedted call" );
        } else {
            Utility.logDebug(machineList);
            initListView();
            convertMachineList(machineList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_SAVE, Menu.NONE, "Save" );
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
        ListView listViewItems = (ListView) findViewById(R.id.listViewMachineList);
        ArrayList<Machine> list = new ArrayList<Machine>();
        mMachineAdapter = new MachineAdapter(this, R.layout.item_search_machine, list);
        listViewItems.setAdapter(mMachineAdapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMachine = mMachineAdapter.getItem(position).createJson();
                Intent intent = new Intent(SearchMachineListActivity.this, MachineInformationActivity.class);
                intent.putExtra(MachineInformationActivity.EXTRA_MACHINE, selectedMachine);
                startActivity(intent);
            }
        });
    }

    private void convertMachineList(String requestList) {
        try {
            JSONArray jsonArray = new JSONArray(requestList);
            for (int i = 0; i < jsonArray.length(); i++) {
                mMachineAdapter.add(new Machine(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    private void save() {
        boolean ret = false;
        String report = "";
        for (int i = 0; i < mMachineAdapter.getCount(); i++) {
            Machine machine = mMachineAdapter.getItem(i);
            report += machine.generateReport() + "\n" + "===========" + "\n";
        }
        ret = Utility.saveTextFile(this, "Machine", "txt", report);
        if (ret == true) {
            report = Machine.generateReportCSVTitle();
            for (int i = 0; i < mMachineAdapter.getCount(); i++) {
                Machine machine = mMachineAdapter.getItem(i);
                report += machine.generateReportCSV();
            }
            ret = Utility.saveTextFile(this, "Machine", "csv", report);
        }
        if (ret == true) {
            Utility.showToast(this, "Saved report" );
        } else {
            Utility.showToast(this, "Failed to save report" );
        }
    }

    @Override
    void onAPIResponse(String jsonString) {
    }

    public void onClickSortByID(View view) {
        mMachineAdapter.sort(new ComparatorID());
        ListView listViewItems = (ListView) findViewById(R.id.listViewMachineList);
        listViewItems.setAdapter(mMachineAdapter);
        ComparatorID.inverse = !ComparatorID.inverse;
    }

    public void onClickSortByLocation(View view) {
        mMachineAdapter.sort(new ComparatorLocation());
        ListView listViewItems = (ListView) findViewById(R.id.listViewMachineList);
        listViewItems.setAdapter(mMachineAdapter);
        ComparatorLocation.inverse = !ComparatorLocation.inverse;
    }

    public void onClickSortByManufacturer(View view) {
        mMachineAdapter.sort(new ComparatorManufacturer());
        ListView listViewItems = (ListView) findViewById(R.id.listViewMachineList);
        listViewItems.setAdapter(mMachineAdapter);
        ComparatorManufacturer.inverse = !ComparatorManufacturer.inverse;
    }

    public void onClickSortByMachineStatus(View view) {
        mMachineAdapter.sort(new ComparatorMachineStatus());
        ListView listViewItems = (ListView) findViewById(R.id.listViewMachineList);
        listViewItems.setAdapter(mMachineAdapter);
        ComparatorMachineStatus.inverse = !ComparatorMachineStatus.inverse;
    }

}


class ComparatorID implements Comparator<Machine> {
    static Boolean inverse = false;

    @Override
    public int compare(Machine lhs, Machine rhs) {
        if (inverse) {
            return lhs.getMachineID().compareToIgnoreCase(rhs.getMachineID());
        } else {
            return rhs.getMachineID().compareToIgnoreCase(lhs.getMachineID());
        }
    }
}

class ComparatorLocation implements Comparator<Machine> {
    static Boolean inverse = false;

    @Override
    public int compare(Machine lhs, Machine rhs) {
        String leftLocation = lhs.getCampus() + lhs.getShop();
        String rightLocation = rhs.getCampus() + rhs.getShop();
        if (inverse) {
            return leftLocation.compareToIgnoreCase(rightLocation);
        } else {
            return rightLocation.compareToIgnoreCase(leftLocation);
        }
    }
}

class ComparatorManufacturer implements Comparator<Machine> {
    static Boolean inverse = false;

    @Override
    public int compare(Machine lhs, Machine rhs) {
        if (inverse) {
            return lhs.getManufacturer().compareToIgnoreCase(rhs.getManufacturer());
        } else {
            return rhs.getManufacturer().compareToIgnoreCase(lhs.getManufacturer());
        }
    }
}

class ComparatorMachineStatus implements Comparator<Machine> {
    static Boolean inverse = false;

    @Override
    public int compare(Machine lhs, Machine rhs) {
        if (inverse) {
            return lhs.getMachineStatus().compareToIgnoreCase(rhs.getMachineStatus());
        } else {
            return rhs.getMachineStatus().compareToIgnoreCase(lhs.getMachineStatus());
        }
    }
}