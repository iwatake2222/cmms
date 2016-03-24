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
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogName)).setText("Completed By: " + (getItem(position).getContractor()));
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMaintenanceRequired)).setText(getItem(position).getMaintenanceRequired());
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMachineID)).setText(getItem(position).getMachineID());
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogRequestID)).setText(getItem(position).getRequestID());
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogMaintenanceID)).setText(getItem(position).getMaintenanceLogID());
        ((TextView)convertView.findViewById(R.id.textViewMaintenanceLogActionTaken)).setText(getItem(position).getActionTaken());

        return convertView;
    }
}