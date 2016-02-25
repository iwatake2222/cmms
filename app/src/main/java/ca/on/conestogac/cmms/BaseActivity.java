package ca.on.conestogac.cmms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String>  {

    abstract void onAPIResponse(String jsonString);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                // todo: call logout -> no. do this in login activity
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                return true;
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.text_about)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void callAPI(String API, JSONObject jsonParam){
        Bundle bundle = new Bundle();
        bundle.putString("url", ValueConstants.SERVER_URL + API);
        bundle.putString("method", "POST");
        bundle.putString("param", jsonParam.toString());
        getSupportLoaderManager().restartLoader(0, bundle, this);
    }

    protected void callAPIwoParam(String API){
        Bundle bundle = new Bundle();
        bundle.putString("url", ValueConstants.SERVER_URL + API);
        bundle.putString("method", "POST");
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("userID", User.getInstance().userID);
        } catch (JSONException e) {
            Utility.logDebug(e.getMessage());
        }
        bundle.putString("param", jsonParam.toString());
        getSupportLoaderManager().restartLoader(0, bundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        HttpAsyncLoader loader = new HttpAsyncLoader(this, args.getString("url"), args.getString("method"), args.getString("param"));
        //Utility.logDebug("onCreateLoader: " + args.getString("url"));
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if ( loader.getId() == 0 ) {
            if (data != null) {
                Utility.logDebug(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String result = "response has no return code";
                    if(jsonObject.has("result")) {
                        result = jsonObject.getString("result");
                    }
                    if (result.compareTo(ValueConstants.RET_OK) != 0) {
                        Utility.logError(result);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Server Error");
                        builder.setMessage("Server returns: " + result);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder.show();
                    }
                } catch (JSONException e) {
                    Utility.logError(e.getMessage());
                }
                onAPIResponse(data);
            } else {
                    Utility.logError("");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // do nothing
    }

}
