package com.example.freatnor.networkinglab;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jonathan Taylor on 8/4/16.
 */
public class WalmartAsyncTask extends AsyncTask<String, Void, ArrayList<WalmartObject>> {

    private WalmartGetCompletedListener mListener;

    public WalmartAsyncTask(WalmartGetCompletedListener listener) {
        mListener = listener;
    }

    @Override
    protected ArrayList<WalmartObject> doInBackground(String... strings) {
        ArrayList<WalmartObject> objects = null;
        try {
            objects = downloadUrl(strings);
        } catch (MalformedURLException e) {
            Log.e("URL Connection", "downloadUrl: Bad URL", e);
        } catch (IOException e) {
            Log.e("URL Connection", "downloadUrl: Unable to access URL", e);
        } catch (JSONException e) {
            Log.e("URL Connection", "downloadUrl: Unable to read JSON", e);
        }
        return objects;
    }

    @Override
    protected void onPostExecute(ArrayList<WalmartObject> objects) {
        super.onPostExecute(objects);
        mListener.onCompleted(objects);
    }

    public ArrayList<WalmartObject> downloadUrl(String[] params) throws MalformedURLException, IOException, JSONException {
        InputStream is = null;
        String url = "http://api.walmartlabs.com/v1/paginated/items?format=json&?apiKey="+MainActivity.WALMART_API_KEY;
        for (int i = 0; i < params.length; i++) {
            url+="?category="+params[0];
        }
        String contentAsString = "";
        try {
            URL newUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) newUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();

            contentAsString = readIt(is);

        } finally {
            if (is != null) {
                is.close();
            }
            return parseJson(contentAsString);
        }
    }

    private String readIt(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String read;
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        return sb.toString();
    }

    private ArrayList<WalmartObject> parseJson(String json) throws JSONException {
        ArrayList<WalmartObject> objects = new ArrayList<>();
        JSONObject root = new JSONObject(json);
        JSONArray array = root.getJSONArray("items");
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            objects.add(new WalmartObject(obj.getString("name"), obj.getString("salePrice")));
        }
        return objects;
    }
}
