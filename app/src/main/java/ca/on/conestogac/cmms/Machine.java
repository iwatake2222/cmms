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
    private String isDisposed;
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
            if(jsonObject.has("machineID"))
                this.machineID = jsonObject.getString("machineID");
            if(jsonObject.has("campus"))
                this.campus = jsonObject.getString("campus");
            if(jsonObject.has("shop"))
                this.shop = jsonObject.getString("shop");
            if(jsonObject.has("isDisposed"))
                this.isDisposed = jsonObject.getString("isDisposed");
            if(jsonObject.has("manufacturer"))
                this.manufacturer = jsonObject.getString("manufacturer");
            if(jsonObject.has("serialNumber"))
                this.serialNumber = jsonObject.getString("serialNumber");
            if(jsonObject.has("modelNumber"))
                this.modelNumber = jsonObject.getString("modelNumber");
            if(jsonObject.has("description"))
                this.description = jsonObject.getString("description");
            if(jsonObject.has("linkToPicture"))
                this.linkToPicture = jsonObject.getString("linkToPicture");
            if(jsonObject.has("linkToDocument")) {
                JSONArray jsonArray = jsonObject.getJSONArray("linkToDocument");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject eachDocument = jsonArray.getJSONObject(i);
                    this.namesOfDocument.add(eachDocument.getString("documentName"));
                    this.linksToDocument.add(eachDocument.getString("link"));
                }
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }

    public String createJson() {
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("machineID", machineID);
            jsonParam.put("campus", campus);
            jsonParam.put("shop", shop);
            jsonParam.put("isDisposed", isDisposed);
            jsonParam.put("manufacturer", manufacturer);
            jsonParam.put("serialNumber",serialNumber );
            jsonParam.put("modelNumber",modelNumber );
            jsonParam.put("description",description );
            jsonParam.put("linkToPicture",linkToPicture );
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < namesOfDocument.size(); i++){
                JSONObject eachDocument = new JSONObject();
                eachDocument.put("documentName", namesOfDocument.get(i));
                eachDocument.put("link", linksToDocument.get(i));
                jsonArray.put(eachDocument);
            }
            jsonParam.put("linkToDocument", jsonArray);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        return jsonParam.toString();
    }

    public String generateReport() {
        String str;
        str  = "Machine ID: " + machineID + "\n";
        str += "Disposed: " + ((isDisposed.compareTo("0")==0)?"No":"Yes") + "\n";
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
        str += "Disposed,";
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
        str += Utility.escapeForCSV(((isDisposed.compareTo("0")==0)?"No":"Yes")) + ",";
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

    public String getIsDisposed() {
        return isDisposed;
    }

    public void setIsDisposed(String isDisposed) {
        this.isDisposed = isDisposed;
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
