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
            if(jsonObject.has("RequestID"))
                this.requestID = jsonObject.getString("RequestID");
            if(jsonObject.has("MachineID"))
                this.machineID = jsonObject.getString("MachineID");
            if(jsonObject.has("Campus"))
                this.campus = jsonObject.getString("Campus");
            if(jsonObject.has("Shop"))
                this.shop = jsonObject.getString("Shop");
            if(jsonObject.has("CreatedBy"))
                this.createdBy = jsonObject.getString("CreatedBy").equals("null") ? " " : jsonObject.getString("CreatedBy");
            if(jsonObject.has("CompletedBy"))
                this.completedBy = jsonObject.getString("CompletedBy").equals("null") ? " " : jsonObject.getString("CompletedBy");
            if(jsonObject.has("DateRequested"))
                this.dateRequested = jsonObject.getString("DateRequested").equals("null") ? " " : jsonObject.getString("DateRequested");
            if(jsonObject.has("DateDue"))
                this.dateDue = jsonObject.getString("DateDue").equals("null") ? " " : jsonObject.getString("DateDue");
            if(jsonObject.has("DateResolved"))
                this.dateResolved = jsonObject.getString("DateResolved").equals("null") ? " " : jsonObject.getString("DateResolved");
            if(jsonObject.has("Progress"))
                this.progress = jsonObject.getString("Progress").equals("null") ? " " : jsonObject.getString("Progress");
            if(jsonObject.has("Title"))
                this.title = jsonObject.getString("Title").equals("null") ? " " : jsonObject.getString("Title");
            if(jsonObject.has("RequestFor"))
                this.requestFor = jsonObject.getString("RequestFor").equals("null") ? " " : jsonObject.getString("RequestFor");
            if(jsonObject.has("Status"))
                this.status = jsonObject.getString("Status").equals("null") ? " " : jsonObject.getString("Status");
            if(jsonObject.has("Priority"))
                this.priority = jsonObject.getString("Priority").equals("null") ? " " : jsonObject.getString("Priority");
            if(jsonObject.has("Description"))
                this.description = jsonObject.getString("Description").equals("null") ? " " : jsonObject.getString("Description");
            if(jsonObject.has("RelatedMaintenanceLogList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("RelatedMaintenanceLogList" );
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
            jsonParam.put("RequestID", requestID);
            jsonParam.put("MachineID", machineID);
            jsonParam.put("Campus", campus);
            jsonParam.put("Shop", shop);
            jsonParam.put("CreatedBy", createdBy);
            jsonParam.put("CompletedBy", completedBy);
            jsonParam.put("DateRequested",dateRequested );
            jsonParam.put("DateDue",dateDue );
            jsonParam.put("DateResolved",dateResolved );
            jsonParam.put("Progress", progress);
            jsonParam.put("Title", title);
            jsonParam.put("RequestFor", requestFor);
            jsonParam.put("Status", status);
            jsonParam.put("Priority", priority);
            jsonParam.put("Description", description);
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < relatedMaintenanceLogList.size(); i++){
                jsonArray.put(relatedMaintenanceLogList.get(i));
            }
            jsonParam.put("RelatedMaintenanceLogList", jsonArray);
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
