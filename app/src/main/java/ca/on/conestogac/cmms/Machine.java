package ca.on.conestogac.cmms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-15.
 */
public class Machine {
    private String machineID;
    private String campus;
    private String shop;
    private String machineStatus;
    private String manufacturer;
    private String serialNumber;
    private String modelNumber;
    private String description;
    private String linkToPicture;
    private ArrayList<String> namesOfDocument;
    private ArrayList<String> linksToDocument;


    public Machine(JSONObject jsonObject) {
        namesOfDocument = new ArrayList<String>();
        linksToDocument = new ArrayList<String>();
        try {
            if(jsonObject.has("MachineID"))
                this.machineID = jsonObject.getString("MachineID");
            if(jsonObject.has("Campus"))
                this.campus = jsonObject.getString("Campus");
            if(jsonObject.has("Shop"))
                this.shop = jsonObject.getString("Shop");
            if(jsonObject.has("MachineStatus"))
                this.machineStatus = jsonObject.getString("MachineStatus");
            if(jsonObject.has("Manufacturer"))
                this.manufacturer = jsonObject.getString("Manufacturer");
            if(jsonObject.has("SerialNumber"))
                this.serialNumber = jsonObject.getString("SerialNumber");
            if(jsonObject.has("ModelNumber"))
                this.modelNumber = jsonObject.getString("ModelNumber");
            if(jsonObject.has("Description"))
                this.description = jsonObject.getString("Description");
            if(jsonObject.has("LinkToPicture"))
                this.linkToPicture = jsonObject.getString("LinkToPicture");
            if(jsonObject.has("LinkToDocument")) {
                JSONArray jsonArray = jsonObject.getJSONArray("LinkToDocument");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject eachDocument = jsonArray.getJSONObject(i);
                    this.namesOfDocument.add(eachDocument.getString("DocumentName"));
                    this.linksToDocument.add(eachDocument.getString("Link"));
                }
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public String createJson() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("MachineID", machineID);
            jsonParam.put("Campus", campus);
            jsonParam.put("Shop", shop);
            jsonParam.put("MachineStatus", machineStatus);
            jsonParam.put("Manufacturer", manufacturer);
            jsonParam.put("SerialNumber",serialNumber );
            jsonParam.put("ModelNumber",modelNumber );
            jsonParam.put("Description",description );
            jsonParam.put("LinkToPicture",linkToPicture );
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < namesOfDocument.size(); i++){
                JSONObject eachDocument = new JSONObject();
                eachDocument.put("DocumentName", namesOfDocument.get(i));
                eachDocument.put("Link", linksToDocument.get(i));
                jsonArray.put(eachDocument);
            }
            jsonParam.put("LinkToDocument", jsonArray);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        return jsonParam.toString();
    }

    public String generateReport() {
        String str;
        str  = "Machine ID: " + machineID + "\n";
        str += "Status: " + machineStatus + "\n";
        str += "Location: " + campus + " (" + shop + ")" + "\n";
        str += "Manufacturer: " + manufacturer + "\n";
        str += "SerialNumber: " + serialNumber + "\n";
        str += "ModelNumber: " + modelNumber + "\n";
        str += "Description: " + description + "\n";
        return str;
    }

    static public String generateReportCSVTitle() {
        // todo: get machine name
        String str;
        str  = "Machine ID,";
        str += "Campus,";
        str += "Shop,";
        str += "Status,";
        str += "Manufacturer,";
        str += "SerialNumber,";
        str += "ModelNumber,";
        str += "Description,";
        str += "\n";
        return str;
    }

    public String generateReportCSV() {
        // todo: get machine name
        String str;
        str  = Utility.escapeForCSV(machineID) + ",";
        str += Utility.escapeForCSV(campus) + ",";
        str += Utility.escapeForCSV(shop) + ",";
        str += Utility.escapeForCSV(machineStatus) + ",";
        str += Utility.escapeForCSV(manufacturer) + ",";
        str += Utility.escapeForCSV(serialNumber) + ",";
        str += Utility.escapeForCSV(modelNumber) + ",";
        str += Utility.escapeForCSV(description) + ",";
        str += "\n";
        return str;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public ArrayList<String> getLinksToDocument() {
        return linksToDocument;
    }

    public void setLinksToDocument(ArrayList<String> linksToDocument) {
        this.linksToDocument = linksToDocument;
    }


    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLinkToPicture() {
        return linkToPicture;
    }

    public void setLinkToPicture(String linkToPicture) {
        this.linkToPicture = linkToPicture;
    }

    public ArrayList<String> getNamesOfDocument() {
        return namesOfDocument;
    }

    public void setNamesOfDocument(ArrayList<String> namesOfDocument) {
        this.namesOfDocument = namesOfDocument;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
}
