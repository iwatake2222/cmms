package ca.on.conestogac.cmms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-02-24.
 */
public class WorkRequest {
    private String requestID;
    private String machineID;
    private String campus;
    private String shop;
    private String createdBy;
    private String completedBy;
    private String dateRequested;
    private String dateDue;
    private String dateResolved;
    private String progress;
    private String title;
    private String requestFor;
    private String status;
    private String priority;
    private String description;
    private ArrayList<String> relatedMaintenanceLogList;

    public WorkRequest(JSONObject jsonObject) {
        relatedMaintenanceLogList = new ArrayList<String>();
        try {
            this.requestID = jsonObject.getString("requestID");
            this.machineID = jsonObject.getString("machineID");
            this.campus = jsonObject.getString("campus");
            this.shop = jsonObject.getString("shop");
            this.createdBy = jsonObject.getString("createdBy");
            this.completedBy = jsonObject.getString("completedBy");
            this.dateRequested = jsonObject.getString("dateRequested");
            this.dateDue = jsonObject.getString("dateDue");
            this.dateResolved = jsonObject.getString("dateResolved");
            this.progress = jsonObject.getString("progress");
            this.title = jsonObject.getString("title");
            this.requestFor = jsonObject.getString("requestFor");
            this.status = jsonObject.getString("status");
            this.priority = jsonObject.getString("priority");
            this.description = jsonObject.getString("description");
            JSONArray jsonArray = jsonObject.getJSONArray("relatedMaintenanceLogList");
            for (int i = 0; i < jsonArray.length(); i++) {
                relatedMaintenanceLogList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public String createJson() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("requestID", requestID);
            jsonParam.put("machineID", machineID);
            jsonParam.put("campus", campus);
            jsonParam.put("shop", shop);
            jsonParam.put("createdBy", createdBy);
            jsonParam.put("completedBy", completedBy);
            jsonParam.put("dateRequested",dateRequested );
            jsonParam.put("dateDue",dateDue );
            jsonParam.put("dateResolved",dateResolved );
            jsonParam.put("progress", progress);
            jsonParam.put("title", title);
            jsonParam.put("requestFor", requestFor);
            jsonParam.put("status", status);
            jsonParam.put("priority", priority);
            jsonParam.put("description", description);
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < relatedMaintenanceLogList.size(); i++){
                jsonArray.put(relatedMaintenanceLogList.get(i));
            }
            jsonParam.put("relatedMaintenanceLogList", jsonArray);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        return jsonParam.toString();
    }

    public String generateReport() {
        // todo: get machine name
        String str;
        str = title + "\n";
        str += "Request ID: " + requestID + "\n";
        str += "Machine ID: " + machineID + "\n";
        str += "Location: " + campus + " (" + shop + ")" + "\n";
        str += "Date requested: " + dateRequested + "\n";
        str += "Date resolved: " + dateResolved + "\n";
        str += "Completed by: " + completedBy + "\n";
        str += "Description: " + description + "\n";
        return str;
    }

    static public String generateReportCSVTitle() {
        // todo: get machine name
        String str;
        str  = "Request ID, ";
        str += "Title, ";
        str += "Machine ID,";
        str += "Campups,";
        str += "Shop,";
        str += "Date requested,";
        str += "Date resolved,";
        str += "Completed by,";
        str += "Description,";
        str += "\n";
        return str;
    }

    public String generateReportCSV() {
        // todo: get machine name
        String str;
        str  = requestID + ", ";
        str += title + ", ";
        str += machineID + ", ";
        str += campus + ", ";
        str += shop + ", ";
        str += dateRequested + ", ";
        str += dateResolved + ", ";
        str += completedBy + ", ";
        str += description + ", ";
        str += "\n";
        return str;
    }

    @Override
    public String toString() {
        return "WorkRequest{" +
                "campus='" + campus + '\'' +
                ", requestID='" + requestID + '\'' +
                ", machineID='" + machineID + '\'' +
                ", shop='" + shop + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", completedBy='" + completedBy + '\'' +
                ", dateRequested='" + dateRequested + '\'' +
                ", dateDue='" + dateDue + '\'' +
                ", dateResolved='" + dateResolved + '\'' +
                ", progress='" + progress + '\'' +
                ", title='" + title + '\'' +
                ", requestFor='" + requestFor + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", description='" + description + '\'' +
                ", relatedMaintenanceLogList=" + relatedMaintenanceLogList +
                '}';
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getDateDue() {
        return dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(String dateResolved) {
        this.dateResolved = dateResolved;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestFor() {
        return requestFor;
    }

    public void setRequestFor(String requestFor) {
        this.requestFor = requestFor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getRelatedMaintenanceLogList() {
        return relatedMaintenanceLogList;
    }

    public void setRelatedMaintenanceLogList(ArrayList<String> relatedMaintenanceLogList) {
        this.relatedMaintenanceLogList = relatedMaintenanceLogList;
    }
}
