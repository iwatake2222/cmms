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
    private String dateCreated;
    private String createdBy;
    private String progress;
    private String title;
    private String requestFor;
    private String status;
    private String priority;
    private String dueDate;
    private String description;
    private ArrayList<String> relatedMaintenanceLogList;

    public WorkRequest(JSONObject jsonObject) {
        relatedMaintenanceLogList = new ArrayList<String>();
        try {
            this.requestID = jsonObject.getString("requestID");
            this.machineID = jsonObject.getString("machineID");
            this.campus = jsonObject.getString("campus");
            this.shop = jsonObject.getString("shop");
            this.dateCreated = jsonObject.getString("dateCreated");
            this.createdBy = jsonObject.getString("createdBy");
            this.progress = jsonObject.getString("progress");
            this.title = jsonObject.getString("title");
            this.requestFor = jsonObject.getString("requestFor");
            this.status = jsonObject.getString("status");
            this.priority = jsonObject.getString("priority");
            this.dueDate = jsonObject.getString("dueDate");
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
            jsonParam.put("dateCreated",dateCreated );
            jsonParam.put("createdBy", createdBy);
            jsonParam.put("progress", progress);
            jsonParam.put("title", title);
            jsonParam.put("requestFor", requestFor);
            jsonParam.put("status", status);
            jsonParam.put("priority", priority);
            jsonParam.put("dueDate", dueDate);
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

    @Override
    public String toString() {
        return "WorkRequest{" +
                "requestID='" + requestID + '\'' +
                ", machineID='" + machineID + '\'' +
                ", campus='" + campus + '\'' +
                ", shop='" + shop + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", progress='" + progress + '\'' +
                ", title='" + title + '\'' +
                ", requestFor='" + requestFor + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", dueDate='" + dueDate + '\'' +
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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
