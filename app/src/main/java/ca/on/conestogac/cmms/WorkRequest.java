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
            if(jsonObject.has("requestID"))
                this.requestID = jsonObject.getString("requestID");
            if(jsonObject.has("machineID"))
                this.machineID = jsonObject.getString("machineID");
            if(jsonObject.has("campus"))
                this.campus = jsonObject.getString("campus");
            if(jsonObject.has("shop"))
                this.shop = jsonObject.getString("shop");
            if(jsonObject.has("createdBy"))
                this.createdBy = jsonObject.getString("createdBy").equals("null") ? " " : jsonObject.getString("createdBy");
            if(jsonObject.has("completedBy"))
                this.completedBy = jsonObject.getString("completedBy").equals("null") ? " " : jsonObject.getString("completedBy");
            if(jsonObject.has("dateRequested"))
                this.dateRequested = jsonObject.getString("dateRequested").equals("null") ? " " : jsonObject.getString("dateRequested");
            if(jsonObject.has("dueDate"))
                this.dateDue = jsonObject.getString("dueDate").equals("null") ? " " : jsonObject.getString("dueDate");
            if(jsonObject.has("dateResolved"))
                this.dateResolved = jsonObject.getString("dateResolved").equals("null") ? " " : jsonObject.getString("dateResolved");
            if(jsonObject.has("progress"))
                this.progress = jsonObject.getString("progress").equals("null") ? " " : jsonObject.getString("progress");
            if(jsonObject.has("title"))
                this.title = jsonObject.getString("title").equals("null") ? " " : jsonObject.getString("title");
            if(jsonObject.has("requestFor"))
                this.requestFor = jsonObject.getString("requestFor").equals("null") ? " " : jsonObject.getString("requestFor");
            if(jsonObject.has("status"))
                this.status = jsonObject.getString("status").equals("null") ? " " : jsonObject.getString("status");
            if(jsonObject.has("priority"))
                this.priority = jsonObject.getString("priority").equals("null") ? " " : jsonObject.getString("priority");
            if(jsonObject.has("description"))
                this.description = jsonObject.getString("description").equals("null") ? " " : jsonObject.getString("description");
            if(jsonObject.has("relatedMaintenanceLogList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("relatedMaintenanceLogList" );
                for (int i = 0; i < jsonArray.length(); i++) {
                    relatedMaintenanceLogList.add(jsonArray.getString(i));
                }
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
            jsonParam.put("dueDate",dateDue );
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
        str  = "Request ID,";
        str += "Title,";
        str += "Machine ID,";
        str += "Campus,";
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
        str  = Utility.escapeForCSV(requestID) + ",";
        str += Utility.escapeForCSV(title) + ",";
        str += Utility.escapeForCSV(machineID) + ",";
        str += Utility.escapeForCSV(campus) + ",";
        str += Utility.escapeForCSV(shop) + ",";
        str += Utility.escapeForCSV(dateRequested) + ",";
        str += Utility.escapeForCSV(dateResolved) + ",";
        str += Utility.escapeForCSV(completedBy) + ",";
        str += Utility.escapeForCSV(description) + ",";
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
