package ca.on.conestogac.cmms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016-02-24.
 */
public class RequestAdapter extends ArrayAdapter<WorkRequest> {
    private LayoutInflater layoutInflater = null;

    public RequestAdapter(Context context, int resource, ArrayList<WorkRequest> objects) {
        super(context, resource, objects);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_search_request, parent, false);
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestTitle)).setText(getItem(position).getTitle());
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestCreationDate)).setText("Date requested: " + Utility.convertDateYYYYMMDDToShow(getItem(position).getDateRequested()));
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestDueDate)).setText("Due date: " + Utility.convertDateYYYYMMDDToShow(getItem(position).getDateDue()));
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestProgress)).setText(getItem(position).getProgress());
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestCampus)).setText(getItem(position).getCampus());
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestShop)).setText(getItem(position).getShop());
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestRequestFor)).setText(getItem(position).getRequestFor());
        ((TextView)convertView.findViewById(R.id.textViewItemSearchRequestStatus)).setText(getItem(position).getStatus());

        return convertView;
    }
}
