package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aegagent.R;
import it.aeg2000srl.aegagent.core.Order;

public class CustomerDetailsActivity extends AppCompatActivity implements ICustomerDetailsView {

    // UI references
    TextView lblId;
    TextView lblName;
    TextView lblAddress;
    TextView lblCity;
//    TextView lblWaitingOrders;
    Button btnNewOrder;
    ListView lstWaitingOrders;

    // presenter
    CustomerDetailsPresenter presenter;
//    List<OrderViewModel> waitingOrders;
    CustomerViewModel customerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        lblName = (TextView)findViewById(R.id.lblName);
        lblAddress = (TextView)findViewById(R.id.lblAddress);
        lblCity = (TextView)findViewById(R.id.lblCity);
        btnNewOrder = (Button)findViewById(R.id.btnNewOrder);
        lstWaitingOrders = (ListView)findViewById(R.id.lstWaitingOrders);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        lstWaitingOrders.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<OrderViewModel>()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent() != null) {
            Intent intent = getIntent();
            final long customer_id = intent.getLongExtra("id", -1);
            if (customer_id > 0) {
                presenter = new CustomerDetailsPresenter(this, customer_id);

                btnNewOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.createNewOrder(customer_id);
                    }
                });
            } else {
                Toast.makeText(this, "Errore: cliente non selezionato", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
//        update();
    }

    @Override
    public void setCustomer(CustomerViewModel customerViewModel) {
        this.customerViewModel = customerViewModel;
    }

    @Override
    public ArrayAdapter<OrderViewModel> getWaitingOrdersAdapter() {
        return (ArrayAdapter<OrderViewModel>)lstWaitingOrders.getAdapter();
    }

//    @Override
//    public void setWaitingOrders(List<Order> waitingOrders) {
//        this.waitingOrders = waitingOrders;
//    }

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

    @Override
    public void update() {
        if(customerViewModel != null) {
            lblName.setText(customerViewModel.Name);
            lblAddress.setText(customerViewModel.Address);
            lblCity.setText(customerViewModel.City);
        }

        getWaitingOrdersAdapter().notifyDataSetChanged();

//        if(waitingOrders > 0) {
//            lblWaitingOrders.setText(String.valueOf(waitingOrders));
//        }
    }
}
