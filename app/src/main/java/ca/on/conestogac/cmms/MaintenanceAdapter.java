package ca.on.conestogac.cmms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by susan on 2016-03-19.
 */

public class MaintenanceAdapter extends ArrayAdapter<MaintenanceLog> {
    private LayoutInflater layoutInflater = null;

    public MaintenanceAdapter(Context context, int resource, ArrayList<MaintenanceLog> objects) {
        super(context, resource, objects);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_maintenance_log, parent, false);
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogDate)).setText("Date: " + Utility.convertDateYYYYMMDDToShow(getItem(position).getDate()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogName)).setText("Completed by: " + (getItem(position).getCompletedBy()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMaintenanceRequired)).setText("Maintenance required: " + (getItem(position).getMaintenanceRequired()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogContractor)).setText("Contractor: " + (getItem(position).getContractor()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogContractorCompany)).setText("Contractor company: " + (getItem(position).getContractorCompany()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogPartsRequired)).setText("Parts required: " + (getItem(position).getPartsRequired()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogActionTaken)).setText("Action taken: " + (getItem(position).getActionTaken()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogPartCost)).setText("Part cost: " + (getItem(position).getPartCost()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMaintenanceID)).setText("MaintenanceID: " + (getItem(position).getMaintenanceLogID()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMachineID)).setText("MachineID: " + (getItem(position).getMachineID()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogRequestID)).setText("RequestID: " + (getItem(position).getRequestID()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMaintenanceLogID)).setText("Maintenance Log ID: " + (getItem(position).getMaintenanceLogID()));
        return convertView;
    }
}