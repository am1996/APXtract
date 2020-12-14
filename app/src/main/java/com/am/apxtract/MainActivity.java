package com.am.apxtract;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import Utils.*;

public class MainActivity extends AppCompatActivity {
    RecyclerView appsList;
    PackageManager pm;
    SearchView searchView;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private void initApp(){
        searchView = findViewById(R.id.searchView);
        pm =  getPackageManager();
        gson = new Gson();
        appsList = findViewById(R.id.appsList);
        /* init listview */
        Type type = new TypeToken<ArrayList<AppInfo>>(){}.getType();
        final ArrayList<AppInfo> apps = gson.fromJson(Utils.getFromShared(getApplicationContext(),"appsInfo"),type);
        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getApplicationContext(),apps);
        appsList.setAdapter(recyclerAdapter);
        appsList.setLayoutManager(new LinearLayoutManager(this));
        /* init listview end*/
        /* search view init */
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
        /* search view init end */
    }
}
