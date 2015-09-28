package it.aeg2000srl.aegagent.services;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
import java.util.List;
import it.aeg2000srl.aegagent.mvp.IUpdateDataView;


/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class DownloadProductsService extends AsyncTask<String, Integer, Integer> {
    private String _url = null;
    private final int CONN_TIMEOUT = 10000;
    private Exception exception;
    private List<ContentValues> data;
    private IUpdateDataView _view;

    public DownloadProductsService(IUpdateDataView view) {
        _view = view;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(_view.getContext());
        _url = SP.getString("api_address", "http://192.168.56.1:5000/api/v1.0");
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

                try {
                    cv.put("price", obj.getDouble("price"));
                } catch (JSONException e) {
                    cv.put("price", 0.00);
                }
                //articolo.getUnita_misura(),
                data.add(cv);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            exception = e;
            _view.showMessage(e.toString());
        }
        finally {
            urlConnection = null;
        }

        return sz;
    }

    /** This method runs on the UI thread */
    protected void onProgressUpdate(Integer... progressValue) {
        // TODO Update your ProgressBar here
        //lblStatus.setText(String.format("Downloading: %d", progressValue[0]));
        _view.showMessage(String.format("Downloading: %d", progressValue[0]));
    }

    protected void onPostExecute(Integer result) {
        _view.showMessage("Done: " + result);
        //lblStatus.setText("Done");
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
