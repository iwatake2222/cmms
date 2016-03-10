package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    Button buttonLoginLogin;
    EditText userID, password;
    boolean canSearchForMachine=false, canSearchForRepairRequest=false, canAccessHome=false, canAccessMachineInformation=false;
    boolean canCreateWorkRequest=false, canDisplayWorkRequest=false, canAccessMaintenanceLogList=false, canSearchForWorkRequest=false, canViewBusinessReport=false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // disable unnecessary menu in Login screen
        MenuItem item = menu.findItem(R.id.action_back);
        item.setVisible(false);
        item = menu.findItem(R.id.action_home);
        item.setVisible(false);
        return true;
    }


    public void onClickLogin(View view) {
        userID= (EditText) findViewById(R.id.userID);
        password= (EditText) findViewById(R.id.password);
        buttonLoginLogin= (Button) findViewById(R.id.buttonLoginLogin);

        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
            jsonParam.put("password", User.getInstance().password);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        callAPI("login", jsonParam);
    }
   /* public void onApiResponse() {
        if (userID = login.userID && password = login.password) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra(HomeActivity.EXTRA_USER_LEVEL, "technician");
            startActivity(intent);
            User.getInstance().userID;
        }
        else{

        }
    }*/
    @Override
    void onAPIResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");
            if (result.compareTo(ValueConstants.RET_OK) != 0 ) {
                // do something if needed when error happens
            }
            if(jsonObject.has("ok") && jsonObject.has("accessLevel")){
               try {
                   int integerAccessLevel = jsonObject.getInt("accessLevel");
                   if(integerAccessLevel==1){
                       canSearchForMachine=true;
                       canSearchForRepairRequest=false;
                       canAccessHome=true;
                       canAccessMachineInformation=true;
                       canCreateWorkRequest=false;
                       canDisplayWorkRequest=false;
                       canAccessMaintenanceLogList=false;
                       canSearchForWorkRequest=false;
                       canViewBusinessReport=false ;
                   }
                   if(integerAccessLevel==2){
                       canSearchForMachine=true;
                       canSearchForRepairRequest=true;
                       canAccessHome=true;
                       canAccessMachineInformation=true;
                       canCreateWorkRequest=true;
                       canDisplayWorkRequest=true;
                       canAccessMaintenanceLogList=false;
                       canSearchForWorkRequest=true;
                       canViewBusinessReport=false ;
                   }
                   if(integerAccessLevel==3){
                       canSearchForMachine=true;
                       canSearchForRepairRequest=true;
                       canAccessHome=true;
                       canAccessMachineInformation=true;
                       canCreateWorkRequest=true;
                       canDisplayWorkRequest=true;
                       canAccessMaintenanceLogList=true;
                       canSearchForWorkRequest=true;
                       canViewBusinessReport=true ;
                   }
                   if(integerAccessLevel==4){
                       canSearchForMachine=true;
                       canSearchForRepairRequest=true;
                       canAccessHome=true;
                       canAccessMachineInformation=true;
                       canCreateWorkRequest=true;
                       canDisplayWorkRequest=true;
                       canAccessMaintenanceLogList=true;
                       canSearchForWorkRequest=true;
                       canViewBusinessReport=true ;
                   }
               }
               catch(JSONException e){
                    Utility.logError(e.getMessage());
                }
            }
        } catch (JSONException e) {
            Utility.logError(e.getMessage());
        }
    }
}
