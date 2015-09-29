package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aegagent.R;

public class OrderDetailsActivity extends AppCompatActivity implements IOrderDetailsView {
    //UI references
    TextView lblCustomerId;
    ListView lstItems;

    OrderDetailsPresenter _presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        lblCustomerId = (TextView) findViewById(R.id.lblCustomerId);
        lstItems = (ListView) findViewById(R.id.lstItems);

        Intent intent = getIntent();
        if(intent != null) {
            long order_id = intent.getLongExtra("order_id", 0L);
            long customer_id = intent.getLongExtra("customer_id", 0L);
            _presenter = new OrderDetailsPresenter(order_id, customer_id, this);
        }
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ArrayAdapter getAdapter() {
        return (ArrayAdapter)lstItems.getAdapter();
    }

    @Override
    public void update() {
        getAdapter().notifyDataSetChanged();
    }

//    @Override
//    public void setItems(ArrayList<ContentValues> items) {
//        ArrayAdapter<ContentValues> adapter = new ArrayAdapter<ContentValues>(this, android.R.layout.simple_list_item_1, items);
//        lstItems.setAdapter(adapter);
//    }
}
