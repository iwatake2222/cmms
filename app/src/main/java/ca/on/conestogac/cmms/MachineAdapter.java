package ca.on.conestogac.cmms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-22.
 */
public class MachineAdapter extends ArrayAdapter<Machine> {
    private LayoutInflater layoutInflater = null;

    public MachineAdapter(Context context, int resource, ArrayList<Machine> objects) {
        super(context, resource, objects);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_search_machine, parent, false);
        ((TextView)convertView.findViewById(R.id.textViewItemMachineID)).setText(getItem(position).getMachineID());
        ((TextView)convertView.findViewById(R.id.textViewItemMachineCampus)).setText(getItem(position).getCampus());
        ((TextView)convertView.findViewById(R.id.textViewItemMachineShop)).setText(getItem(position).getShop());
        ((TextView)convertView.findViewById(R.id.textViewItemMachineDisposed)).setText("Disposed: " + (getItem(position).getIsDisposed().compareTo("0")==0?"No" : "Yes") );
        ((TextView)convertView.findViewById(R.id.textViewItemMachineManufacturer)).setText(getItem(position).getManufacturer());
        ((TextView)convertView.findViewById(R.id.textViewItemMachineSerialNumber)).setText(getItem(position).getSerialNumber());
        ((TextView)convertView.findViewById(R.id.textViewItemMachineModelNumber)).setText(getItem(position).getModelNumber());
        ((TextView)convertView.findViewById(R.id.textViewItemMachineDescription)).setText(getItem(position).getDescription());
        return convertView;
    }
}
