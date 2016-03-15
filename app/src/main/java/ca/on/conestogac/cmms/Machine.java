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
    private String make;
    private String model;
    private String description;
    private ArrayList<String> namesOfDocument;
    private ArrayList<String> linksToDocument;


    public Machine(JSONObject jsonObject) {
        namesOfDocument = new ArrayList<String>();
        linksToDocument = new ArrayList<String>();
        try {
            this.machineID = jsonObject.getString("machineID");
            this.campus = jsonObject.getString("campus");
            this.shop = jsonObject.getString("shop");
            this.isDisposed = jsonObject.getString("isDisposed");
            this.make = jsonObject.getString("make");
            this.model = jsonObject.getString("model");
            this.description = jsonObject.getString("description");
            JSONArray jsonArray = jsonObject.getJSONArray("linkToDocument");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject eachDocument = jsonArray.getJSONObject(i);
                this.namesOfDocument.add(eachDocument.getString("documentName"));
                this.linksToDocument.add(eachDocument.getString("link"));
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
            jsonParam.put("make", make);
            jsonParam.put("model",model );
            jsonParam.put("description",description );
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
        str += "Description: " + description + "\n";
        str += "Location: " + campus + " (" + shop + ")" + "\n";
        str += "Make: " + make + "\n";
        str += "Model: " + model + "\n";
        str += "Disposed: " + ((isDisposed=="0")?"No":"Yes") + "\n";
        return str;
    }

    static public String generateReportCSVTitle() {
        // todo: get machine name
        String str;
        str  = "Machine ID,";
        str += "Description,";
        str += "Campups,";
        str += "Shop,";
        str += "Make,";
        str += "Model,";
        str += "Disposed,";
        str += "\n";
        return str;
    }

    public String generateReportCSV() {
        // todo: get machine name
        String str;
        str  = Utility.escapeForCSV(machineID) + ",";
        str += Utility.escapeForCSV(description) + ",";
        str += Utility.escapeForCSV(campus) + ",";
        str += Utility.escapeForCSV(shop) + ",";
        str += Utility.escapeForCSV(make) + ",";
        str += Utility.escapeForCSV(model) + ",";
        str += Utility.escapeForCSV(((isDisposed=="0")?"No":"Yes")) + ",";
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

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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
