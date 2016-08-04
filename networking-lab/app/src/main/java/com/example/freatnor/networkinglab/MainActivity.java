package com.example.freatnor.networkinglab;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WalmartGetCompletedListener{

    public static final String WALMART_API_KEY = "uqbusdxjepw9dkxsb22wc9ce";

    public static final String COLD_CEREAL_ID = "976759_976783_1231208";
    public static final String HOT_CEREAL_ID = "976759_976783_1231210";

    public static final String CHOCOLATE_ID = "976759_1096070_1224976";

    public static final String TEA_ID = "976759_976782_1001320";
    private static final String TAG = "Main Activity";

    private Button mCereal;
    private Button mChocolate;
    private Button mTea;
    private ListView mListView;

    private ListAdapter mAdapter;

    private ArrayList<WalmartObject> mObjects = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        mCereal = (Button) findViewById(R.id.cereal);
        mChocolate = (Button) findViewById(R.id.chocolate);
        mTea = (Button) findViewById(R.id.tea);

        mListView = (ListView) findViewById(R.id.item_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()) {
                    WalmartAsyncTask task = new WalmartAsyncTask(MainActivity.this);
                    task.execute(COLD_CEREAL_ID, HOT_CEREAL_ID);
                }
            }
        });

        mChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()) {
                    WalmartAsyncTask task = new WalmartAsyncTask(MainActivity.this);
                    task.execute(CHOCOLATE_ID);
                }
            }
        });

        mTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()) {
                    WalmartAsyncTask task = new WalmartAsyncTask(MainActivity.this);
                    task.execute(TEA_ID);
                }
            }
        });

    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onCompleted(ArrayList<WalmartObject> objects) {
        mObjects = objects;
        if(mListView.getAdapter() == null){
            mAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return mObjects.size();
                }

                @Override
                public Object getItem(int i) {
                    return mObjects.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if(view == null){
                        view = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_2, viewGroup, false);
                    }
                    ((TextView)view.findViewById(android.R.id.text1)).setText(mObjects.get(i).getName());
                    ((TextView)view.findViewById(android.R.id.text2)).setText(mObjects.get(i).getPrice());
                    return view;
                }
            };
            mListView.setAdapter(mAdapter);
        }
        else {
//            synchronized (mAdapter){
//                mAdapter.notify();
//            }
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        }
    }
}
