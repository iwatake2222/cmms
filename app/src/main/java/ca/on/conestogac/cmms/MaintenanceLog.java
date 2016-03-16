package ca.on.conestogac.cmms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pca on 16/03/2016.
 */
public class MaintenanceLog {
           private String maintenanceLogID;
           private String machineID;
           private String date;
           private String completedBy;
           private String requestID;
           private String maintenanceRequired;
           private String actionTaken;
           private String partsRequired;
           private String partCost;
           private String partRequisitionNum;
           private String contractor;
           private String contractorCompany;

    public MaintenanceLog(JSONObject jsonObject) {
        try {
             this.maintenanceLogID = jsonObject.getString("maintenanceLogID");
            this.machineID = jsonObject.getString("machineID");
            this.date = jsonObject.getString("date");
            this.completedBy = jsonObject.getString("completedBy");
            this.requestID = jsonObject.getString("requestID");
            this.maintenanceRequired = jsonObject.getString("maintenanceRequired");
            this.actionTaken = jsonObject.getString("actionTaken");
            this.partsRequired = jsonObject.getString("partsRequired");
            this.partCost = jsonObject.getString("partCost");
            this.partRequisitionNum = jsonObject.getString("partRequisitionNum");
            this.contractor = jsonObject.getString("contractor");
            this.contractorCompany = jsonObject.getString("contractorCompany");
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }
}
