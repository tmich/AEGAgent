package it.aeg2000srl.aegagent.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import it.aeg2000srl.aegagent.R;

public class CustomersActivity extends AppCompatActivity implements ICustomersView, SearchView.OnQueryTextListener {

    // UI references
    ListView customersList;

    // Presenter
    CustomersPresenter presenter;

    // DataSet
//    CustomersArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        customersList = (ListView)findViewById(R.id.customersList);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        adapter = new CustomersArrayAdapter(this, null);
//        customersList.setAdapter(adapter);
        presenter = new CustomersPresenter(this);

        customersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onItemClick(i);
            }
        });
    }

    @Override
    public ListView getListView() {
        return customersList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customers, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        //searchView.setIconifiedByDefault(false);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setAdapter(CustomersArrayAdapter adapter) {
        customersList.setAdapter(adapter);
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
    public CustomersArrayAdapter getAdapter() {
        return (CustomersArrayAdapter)customersList.getAdapter();
    }

    @Override
    public void update() {
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Toast.makeText(this, query, Toast.LENGTH_LONG).show();
        if(! query.equals("")) {
            presenter.onSearch(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(! newText.equals("")) {
            presenter.onSearch(newText);
        }
        return false;
    }


    /*
    static class ViewHolder {
        TextView id;
        TextView name;
        ImageView icon;
        int position;

        public setName()
    }

    private class CustomersArrayAdapter extends ArrayAdapter<ContentValues> {
        public CustomersArrayAdapter(List<ContentValues> data) {
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
