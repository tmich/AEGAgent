package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aegagent.R;
import it.aeg2000srl.aegagent.services.CustomerService;
import it.aeg2000srl.aegagent.services.ProductService;

public class UpdateDataActivity extends AppCompatActivity implements IUpdateDataView {
    // UI references
    Button btnMakeUpdate;
    TextView lblStatus;
    //UpdateDataPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        btnMakeUpdate = (Button)findViewById(R.id.btnMakeUpdate);
        lblStatus = (TextView)findViewById(R.id.lblStatus);
        btnMakeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lblStatus.setText("Downloading...");
                new DownloadCustomersService().execute("");
                new DownloadCustomersService().execute("");
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, String.format(message), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_data, menu);
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

    /* Download customers */
    class DownloadCustomersService extends AsyncTask<String, Integer, Integer> {
        private String _url = null;
        private final int CONN_TIMEOUT = 10000;
        private Exception exception;
        private List<ContentValues> data;
        private CustomerService serv;

        public DownloadCustomersService() {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(UpdateDataActivity.this);
            _url = SP.getString("api_address", "http://192.168.56.1:5000/api/v1.0");
            serv = new CustomerService(UpdateDataActivity.this);
        }

        public List<ContentValues> getData() {
            return data;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            data = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            int sz = 0;

            try {
                // Send GET data request
                URL url = new URL(_url + "/customers");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONN_TIMEOUT);
                urlConnection.setReadTimeout(20000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String Content = readStream(in);
                urlConnection.disconnect();

                JSONObject jsonMainNode = new JSONObject(Content);
                JSONArray items = jsonMainNode.getJSONArray("json_list");
                sz = items.length();

                for (int i=0; i < sz; i++) {
                    publishProgress(i);
                    JSONObject obj = items.getJSONObject(i);

                    ContentValues cv = new ContentValues();
                    cv.put("id", obj.getLong("id"));
                    cv.put("code", obj.getString("code"));
                    cv.put("name", obj.getString("name"));
                    cv.put("address", obj.getString("address"));
                    cv.put("iva", obj.getString("iva"));
                    cv.put("prov", obj.getString("prov"));
                    cv.put("city", obj.getString("city"));
                    cv.put("tel", obj.getString("tel"));
                    cv.put("cap", obj.getString("cap"));

                    data.add(cv);
                    serv.Save(cv);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                exception = e;
                showMessage(e.toString());
            }
            finally {
                urlConnection = null;
            }

            return sz;
        }

        /** This method runs on the UI thread */
        protected void onProgressUpdate(Integer... progressValue) {
            // TODO Update your ProgressBar here
            lblStatus.setText(String.format("Downloading: %d", progressValue[0]));
        }

        protected void onPostExecute(Integer result) {
            showMessage("ok: " + result);
            lblStatus.setText("Done");
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

    /* Download Products */
    class DownloadProductsService extends AsyncTask<String, Integer, Integer> {
        private String _url = null;
        private final int CONN_TIMEOUT = 10000;
        private Exception exception;
        private List<ContentValues> data;
        private ProductService serv;

        public DownloadProductsService() {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(UpdateDataActivity.this);
            _url = SP.getString("api_address", "http://192.168.56.1:5000/api/v1.0");
            serv = new ProductService(UpdateDataActivity.this);
        }

        public List<ContentValues> getData() {
            return data;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            data = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            int sz = 0;

            try {
                // Send GET data request
                URL url = new URL(_url + "/products");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONN_TIMEOUT);
                urlConnection.setReadTimeout(20000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String Content = readStream(in);
                urlConnection.disconnect();

                JSONObject jsonMainNode = new JSONObject(Content);
                JSONArray items = jsonMainNode.getJSONArray("json_list");
                sz = items.length();

                for (int i=0; i < sz; i++) {
                    publishProgress(i);
                    JSONObject obj = items.getJSONObject(i);

                    ContentValues cv = new ContentValues();
                    cv.put("id", obj.getLong("id"));
                    cv.put("code", obj.getString("code"));
                    cv.put("name", obj.getString("name"));
                    cv.put("price", obj.getDouble("price"));

                    data.add(cv);
                    serv.Save(cv);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                exception = e;
                showMessage(e.toString());
            }
            finally {
                urlConnection = null;
            }

            return sz;
        }

        /** This method runs on the UI thread */
        protected void onProgressUpdate(Integer... progressValue) {
            // TODO Update your ProgressBar here
            lblStatus.setText(String.format("Downloading: %d", progressValue[0]));
        }

        protected void onPostExecute(Integer result) {
            showMessage("ok: " + result);
            lblStatus.setText("Done");
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
