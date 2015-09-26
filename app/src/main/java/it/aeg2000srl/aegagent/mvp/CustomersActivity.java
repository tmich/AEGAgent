package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aegagent.R;

public class CustomersActivity extends AppCompatActivity implements ICustomersView {

    // UI references
    ListView customersList;

    // Presenter
    CustomersPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        customersList = (ListView)findViewById(R.id.customersList);
        presenter = new CustomersPresenter(this);

        customersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onItemClick(i);
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customers, menu);
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
    public void setItems(ArrayList<ContentValues> items) {
        //ArrayAdapter<ContentValues> adapter = new ArrayAdapter<ContentValues>(this, android.R.layout.simple_list_item_2, items);
        ArrayAdapter<ContentValues> adapter = new ArrayAdapter<ContentValues>(this, android.R.layout.simple_list_item_1, items);
        customersList.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }


    /*
    static class ViewHolder {
        TextView id;
        TextView name;
        ImageView icon;
        int position;

        public setName()
    }

    private class CustomerAdapter extends ArrayAdapter<ContentValues> {
        public CustomerAdapter(List<ContentValues> data) {
            super(CustomersActivity.this, R.layout.cust_row, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater inflater = getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.cust_row, null, false);
                holder = new ViewHolder();
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.id = (TextView) convertView.findViewById(R.id.cid);
            holder.name = (TextView) convertView.findViewById(R.id.cname);
            convertView.setTag(holder);

            return convertView;
        }
    } */

}
