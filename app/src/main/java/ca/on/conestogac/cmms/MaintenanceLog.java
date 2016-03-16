package ca.on.conestogac.cmms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            this.setMaintenanceLogID(jsonObject.getString("maintenanceLogID"));
            this.setMachineID(jsonObject.getString("machineID"));
            this.setDate(jsonObject.getString("date"));
            this.setCompletedBy(jsonObject.getString("completedBy"));
            this.setRequestID(jsonObject.getString("requestID"));
            this.setMaintenanceRequired(jsonObject.getString("maintenanceRequired"));
            this.setActionTaken(jsonObject.getString("actionTaken"));
            this.setPartsRequired(jsonObject.getString("partsRequired"));
            this.setPartCost(jsonObject.getString("partCost"));
            this.setPartRequisitionNum(jsonObject.getString("partRequisitionNum"));
            this.setContractor(jsonObject.getString("contractor"));
            this.setContractorCompany(jsonObject.getString("contractorCompany"));
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public String createJson() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("maintenanceLogID", maintenanceLogID);
            jsonParam.put("machineID", machineID);
            jsonParam.put("date", date);
            jsonParam.put("completedBy", completedBy);
            jsonParam.put("requestID", requestID);
            jsonParam.put("maintenanceRequired", maintenanceRequired);
            jsonParam.put("actionTaken", actionTaken);
            jsonParam.put("partsRequired", partsRequired);
            jsonParam.put("partCost", partCost);
            jsonParam.put("partRequisitionNum", partRequisitionNum);
            jsonParam.put("contractor", contractor);
            jsonParam.put("contractorCompany", contractorCompany);

        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        return jsonParam.toString();
    }

    public String getMaintenanceLogID() {
        return maintenanceLogID;
    }

    public void setMaintenanceLogID(String maintenanceLogID) {
        this.maintenanceLogID = maintenanceLogID;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getMaintenanceRequired() {
        return maintenanceRequired;
    }

    public void setMaintenanceRequired(String maintenanceRequired) {
        this.maintenanceRequired = maintenanceRequired;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getPartsRequired() {
        return partsRequired;
    }

    public void setPartsRequired(String partsRequired) {
        this.partsRequired = partsRequired;
    }

    public String getPartCost() {
        return partCost;
    }

    public void setPartCost(String partCost) {
        this.partCost = partCost;
    }

    public String getPartRequisitionNum() {
        return partRequisitionNum;
    }

    public void setPartRequisitionNum(String partRequisitionNum) {
        this.partRequisitionNum = partRequisitionNum;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getContractorCompany() {
        return contractorCompany;
    }

    public void setContractorCompany(String contractorCompany) {
        this.contractorCompany = contractorCompany;
    }

    @Override
    public String toString() {
        return "MaintenanceLog{" +
                "maintenanceLogID='" + maintenanceLogID + '\'' +
                ", machineID='" + machineID + '\'' +
                ", date='" + date + '\'' +
                ", completedBy='" + completedBy + '\'' +
                ", requestID='" + requestID + '\'' +
                ", maintenanceRequired='" + maintenanceRequired + '\'' +
                ", actionTaken='" + actionTaken + '\'' +
                ", partsRequired='" + partsRequired + '\'' +
                ", partCost='" + partCost + '\'' +
                ", partRequisitionNum='" + partRequisitionNum + '\'' +
                ", contractor='" + contractor + '\'' +
                ", contractorCompany='" + contractorCompany + '\'' +
                '}';
    }
}
