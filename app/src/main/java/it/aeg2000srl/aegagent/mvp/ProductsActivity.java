package it.aeg2000srl.aegagent.mvp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.aeg2000srl.aegagent.R;
//import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.services.ProductService;

public class ProductsActivity extends AppCompatActivity implements IProductsView, SearchView.OnQueryTextListener {

    // UI references
    ListView productsList;
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;

    // presenter
    IProductsPresenter presenter;

    // Adapter
    //ProductsArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        productsList = (ListView)findViewById(R.id.productsList);
        productsList.setAdapter(new ProductsArrayAdapter(this, new ArrayList<ProductViewModel>()));
        productsList.setEmptyView(findViewById(R.id.empty_list));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        try {
            if (getIntent().getAction().equals("CHOOSE_PRODUCT")) {
                presenter = new ChooseProductPresenter(this);
            } else {
                Toast.makeText(this, "Intent not valid", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException ne) {
            presenter = new ProductsPresenter(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        //searchView.setIconifiedByDefault(false);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        return super.onCreateOptionsMenu(menu);
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

        if (id == R.id.action_update) {
            updateFromWs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateFromWs() {
        barProgressDialog = new ProgressDialog(ProductsActivity.this);
        barProgressDialog.setTitle(getString(R.string.title_activity_update_data));
        barProgressDialog.setMessage(getString(R.string.please_wait));
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();
        updateBarHandler = new Handler();
        new DownloadProductsService(updateBarHandler).execute("");
    }

    @Override
    public ProductsArrayAdapter getAdapter() {
        return (ProductsArrayAdapter) productsList.getAdapter();
    }

    @Override
    public void setOnSelectedItem(AdapterView.OnItemClickListener listener) {
        productsList.setOnItemClickListener(listener);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void update() {
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.onSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.onSearch(newText);
        return false;
    }


    /***********************************************************************************************/
    /****************                           ASNYC TASK                          ****************/
    /***********************************************************************************************/
    class DownloadProductsService extends AsyncTask<String, Integer, Integer> {
        private String url = null;
        private final int CONN_TIMEOUT = 10000;
        private Exception exception;
        private List<ContentValues> data;
        private ProductService serv;
        private Handler handler;

        public DownloadProductsService(Handler handler) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
            this.url = SP.getString("api_address", getString(R.string.test_url));
            this.serv = new ProductService(getContext());
            this.handler = handler;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            data = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            int sz = 0;

            try {
                // Send GET data request
                URL url = new URL(this.url + "/products");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONN_TIMEOUT);
                urlConnection.setReadTimeout(20000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String Content = readStream(in);
                urlConnection.disconnect();

                JSONObject jsonMainNode = new JSONObject(Content);
                JSONArray items = jsonMainNode.getJSONArray("json_list");
                sz = items.length();

                barProgressDialog.setMax(sz);

                for (int i=0; i < sz; i++) {
                    publishProgress(i);
                    JSONObject obj = items.getJSONObject(i);

                    ContentValues cv = new ContentValues();
//                    cv.put("id", obj.getLong("id"));
                    cv.put("code", obj.getString("code"));
                    cv.put("name", obj.getString("name"));
                    cv.put("price", obj.getDouble("price"));

                    data.add(cv);
                }

                serv.saveAll(data);
            }
            catch (Exception e) {
                exception = e;
                e.printStackTrace();
                //showMessage(e.toString());
            }

            return sz;
        }

        /** This method runs on the UI thread */
        protected void onProgressUpdate(final Integer... progressValue) {
            handler.post(new Runnable() {
                public void run() {
                    barProgressDialog.setProgress(progressValue[0]);
                }
            });
        }

        protected void onPostExecute(Integer result) {
            if(exception == null) {
                showMessage("ok: " + result);

                getAdapter().addAll((Collection<? extends ProductViewModel>) serv.getAll());
                getAdapter().notifyDataSetChanged();
            } else {
                showError(exception);
            }
            barProgressDialog.dismiss();
        }

        private void showError(Exception e) {
            try {
                throw e;
            }
            catch (IOException ex) {
                showMessage("Errore di connessione");
            }
            catch (JSONException ex) {
                showMessage("Ricevuti dati non validi");
            }
            catch (Exception ex) {
                showMessage("Errore sconosciuto");
            }
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            String ret = "";

            try {
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = "";

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line);
                    sb.append("");
                }

                // Append Server Response To Content String
                ret = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ret;
        }
    }
}
