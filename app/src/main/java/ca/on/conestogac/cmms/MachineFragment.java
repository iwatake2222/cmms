package ca.on.conestogac.cmms;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MachineFragment extends Fragment {
    private static final String ARG_MACHINE_JSON_STRING = "machineJsonString";
    private String mMachineJsonString;
    Machine mMachine;


    public static MachineFragment newInstance(String machineJsonString) {
        MachineFragment fragment = new MachineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MACHINE_JSON_STRING, machineJsonString);
        fragment.setArguments(args);
        return fragment;
    }

    public MachineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMachineJsonString = getArguments().getString(ARG_MACHINE_JSON_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        convertMachine(mMachineJsonString);
        View v = inflater.inflate(R.layout.fragment_machine, container, false);
        ((TextView)v.findViewById(R.id.textViewMachineMachineID)).setText(mMachine.getMachineID());
        ((TextView)v.findViewById(R.id.textViewMachineDescription)).setText(mMachine.getDescription());
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
     }

    private void convertMachine(String machineJsonString){
        try {
            JSONObject machineJson = new JSONObject(machineJsonString);
            mMachine = new Machine(machineJson);
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

}
