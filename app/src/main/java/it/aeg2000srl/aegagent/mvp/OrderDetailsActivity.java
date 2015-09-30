package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.service.chooser.ChooserTarget;
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
import java.util.List;

import it.aeg2000srl.aegagent.R;

public class OrderDetailsActivity extends AppCompatActivity implements IOrderDetailsView {
    //UI references
    TextView lblCustomerId;
    ListView lstItems;
    FloatingActionButton btnNewItem;

    OrderDetailsPresenter _presenter;

    static final int PICK_PRODUCT_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        lblCustomerId = (TextView) findViewById(R.id.lblCustomerId);
        lstItems = (ListView) findViewById(R.id.lstItems);
        btnNewItem = (FloatingActionButton) findViewById(R.id.btnNewItem);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
            long order_id = intent.getLongExtra("order_id", 0L);
            long customer_id = intent.getLongExtra("customer_id", 0L);
            _presenter = new OrderDetailsPresenter(order_id, customer_id, this);
        }

        btnNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // apro l'activity prodotti per farmi restituire un risultato
                Intent chooseProduct = new Intent(OrderDetailsActivity.this, ProductsActivity.class);
                chooseProduct.setAction("CHOOSE_PRODUCT");
                startActivityForResult(chooseProduct, PICK_PRODUCT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_PRODUCT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a product.
                long id = data.getLongExtra("result", 0);
                showMessage(String.valueOf(id));
            }
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
    public void showMessage(String message) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update() {
        getAdapter().notifyDataSetChanged();
    }

}
