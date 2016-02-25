package ca.on.conestogac.cmms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void onClickLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra(HomeActivity.EXTRA_USER_LEVEL, "technician");
        startActivity(intent);

        User.getInstance().userID = "7292030";
    }


    @Override
    void onAPIResponse(String jsonString) {

    }

}
