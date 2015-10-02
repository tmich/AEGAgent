package it.aeg2000srl.aegagent.mvp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.R;
import it.aeg2000srl.aegagent.core.OrderItem;

public class OrderDetailsActivity extends AppCompatActivity implements IOrderDetailsView {
    //UI references
    TextView lblCustomer;
    ListView lstItems;
    FloatingActionButton btnNewItem;

//    long order_id;
//    long customer_id;

    OrderDetailsPresenter presenter;
    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        lblCustomer = (TextView) findViewById(R.id.lblCustomer);
        lstItems = (ListView) findViewById(R.id.lstItems);
        btnNewItem = (FloatingActionButton) findViewById(R.id.btnNewItem);

        lstItems.setAdapter(new OrderItemArrayAdapter(this, new ArrayList<OrderItemViewModel>()));
    }

    @Override
    public void setActionOnNewButton(View.OnClickListener listener)
    {
        btnNewItem.setOnClickListener(listener);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
            long order_id = intent.getLongExtra("order_id", 0L);
            long customer_id = intent.getLongExtra("customer_id", 0L);
            presenter = new OrderDetailsPresenter(order_id, customer_id, this);
        } else {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_details, menu);
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

        if(id == R.id.action_save) {
            try {
                presenter.save();
                showMessage("Ordine salvato");
                finish();
            } catch (Exception exc) {
                showMessage(exc.toString());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ArrayAdapter<OrderItem> getAdapter() {
        return (ArrayAdapter<OrderItem>) lstItems.getAdapter();
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update() {
        lblCustomer.setText(orderViewModel.Customer.Name);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public OrderViewModel getOrderViewModel() {
        return orderViewModel;
    }

    @Override
    public void setOrderViewModel(OrderViewModel orderViewModel) {
        this.orderViewModel = orderViewModel;
        update();
    }
}
