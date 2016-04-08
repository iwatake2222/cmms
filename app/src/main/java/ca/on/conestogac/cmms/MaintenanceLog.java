package ca.on.conestogac.cmms;

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
            if(jsonObject.has("MaintenanceLogID"))
                this.setMaintenanceLogID(jsonObject.getString("MaintenanceLogID").equals("null") ? " " : jsonObject.getString("MaintenanceLogID"));
            if(jsonObject.has("MachineID"))
                this.setMachineID(jsonObject.getString("MachineID").equals("null") ? " " : jsonObject.getString("MachineID"));
            if(jsonObject.has("Date"))
                this.setDate(jsonObject.getString("Date").equals("null") ? " " : jsonObject.getString("Date"));
            if(jsonObject.has("CompletedBy"))
                this.setCompletedBy(jsonObject.getString("CompletedBy").equals("null") ? " " : jsonObject.getString("CompletedBy"));
            if(jsonObject.has("RequestID"))
                this.setRequestID(jsonObject.getString("RequestID").equals("null") ? " " : jsonObject.getString("RequestID"));
            if(jsonObject.has("MaintenanceRequired"))
                this.setMaintenanceRequired(jsonObject.getString("MaintenanceRequired").equals("null") ? " " : jsonObject.getString("MaintenanceRequired"));
            if(jsonObject.has("ActionTaken"))
                this.setActionTaken(jsonObject.getString("ActionTaken").equals("null") ? " " : jsonObject.getString("ActionTaken"));
            if(jsonObject.has("PartsRequired"))
                this.setPartsRequired(jsonObject.getString("PartsRequired").equals("null") ? " " : jsonObject.getString("PartsRequired"));
            if(jsonObject.has("PartCost"))
                this.setPartCost(jsonObject.getString("PartCost").equals("null") ? " " : jsonObject.getString("PartCost"));
            if(jsonObject.has("PartRequisitionNum"))
                this.setPartRequisitionNum(jsonObject.getString("PartRequisitionNum").equals("null") ? " " : jsonObject.getString("PartRequisitionNum"));
            if(jsonObject.has("Contractor"))
                this.setContractor(jsonObject.getString("Contractor").equals("null") ? " " : jsonObject.getString("Contractor"));
            if(jsonObject.has("ContractorCompany"))
                this.setContractorCompany(jsonObject.getString("ContractorCompany").equals("null") ? " " : jsonObject.getString("ContractorCompany"));
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public String createJson() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("MaintenanceLogID", maintenanceLogID);
            jsonParam.put("MachineID", machineID);
            jsonParam.put("Date", date);
            jsonParam.put("CompletedBy", completedBy);
            jsonParam.put("RequestID", requestID);
            jsonParam.put("MaintenanceRequired", maintenanceRequired);
            jsonParam.put("ActionTaken", actionTaken);
            jsonParam.put("PartsRequired", partsRequired);
            jsonParam.put("PartCost", partCost);
            jsonParam.put("PartRequisitionNum", partRequisitionNum);
            jsonParam.put("Contractor", contractor);
            jsonParam.put("ContractorCompany", contractorCompany);

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
    public String generateReport() {
        // todo: get machine name
        String str;
        str = date + "\n";
        str += "Contractor: " +contractor + "\n";
        str += "Contractor Company: " +contractorCompany + "\n";
        str += "Request ID: " + requestID + "\n";
        str += "Machine ID: " + machineID + "\n";
        str += "Maintenance Log ID: " + maintenanceLogID  + "\n";
        str += "Maintenance Required: " + maintenanceRequired + "\n";
        str += "Action Taken: " + actionTaken + "\n";
        str += "Parts Required: " + partsRequired + "\n";
        str += "Part Cost: " + partCost + "\n";
        str += "Part Requisition Num: " + partRequisitionNum + "\n";
        str += "Completed by: " + completedBy + "\n";
        return str;
    }

    static public String generateReportCSVTitle() {
        // todo: get machine name
        String str;
        str  = "Date,";
        str += "Contractor,";
        str += "Contractor Company,";
        str += "Request ID,";
        str += "Machine ID,";
        str += "Maintenance Log ID,";
        str += "Maintenance Required,";
        str += "Action Taken,";
        str += "Parts Required,";
        str += "Part Cost,";
        str += "Part Requisition Num,";
        str += "Completed by,";
        str += "\n";
        return str;
    }

    public String generateReportCSV() {
        // todo: get machine name
        String str;
        str  = Utility.escapeForCSV(date) + ",";
        str += Utility.escapeForCSV(contractor) + ",";
        str += Utility.escapeForCSV(contractorCompany) + ",";
        str += Utility.escapeForCSV(requestID) + ",";
        str += Utility.escapeForCSV(machineID) + ",";
        str += Utility.escapeForCSV(maintenanceLogID) + ",";
        str += Utility.escapeForCSV(maintenanceRequired) + ",";
        str += Utility.escapeForCSV(actionTaken) + ",";
        str += Utility.escapeForCSV(partsRequired) + ",";
        str += Utility.escapeForCSV(partCost) + ",";
        str += Utility.escapeForCSV(partRequisitionNum) + ",";
        str += Utility.escapeForCSV(completedBy) + ",";
        str += "\n";
        return str;
    }
}
