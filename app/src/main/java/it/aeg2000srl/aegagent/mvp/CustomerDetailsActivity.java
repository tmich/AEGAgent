package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import it.aeg2000srl.aegagent.R;

public class CustomerDetailsActivity extends AppCompatActivity implements ICustomerDetailsView {

    // UI references
    TextView lblId;
    TextView lblName;
    TextView lblAddress;
    TextView lblCity;

    // presenter
    CustomerDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        lblId = (TextView)findViewById(R.id.lblId);
        lblName = (TextView)findViewById(R.id.lblName);
        lblAddress = (TextView)findViewById(R.id.lblAddress);
        lblCity = (TextView)findViewById(R.id.lblCity);

        if (getIntent() != null) {
            Intent intent = getIntent();
            long id = intent.getLongExtra("id", -1);
            presenter = new CustomerDetailsPresenter(this, id);
        }
    }

    @Override
    public void setItem(ContentValues item) {
        lblId.setText(String.valueOf(item.getAsLong("id")));
        lblName.setText(String.valueOf(item.getAsString("name")));
        lblAddress.setText(String.valueOf(item.getAsString("address")));
        lblCity.setText(String.valueOf(item.getAsString("city")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
