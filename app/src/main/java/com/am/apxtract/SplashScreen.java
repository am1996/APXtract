package com.am.apxtract;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import Utils.*;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_screen);
        getSupportActionBar().hide();
        Task task = new Task();
        task.execute("");
    }
    private class Task extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String path = Environment.getExternalStorageDirectory() +"/APXtract";
            File direct = new File(path);
            if(!direct.exists()) {
                if(direct.mkdir()); //directory is created;
            }
            Utils.saveToShared(getApplicationContext(),"dstDir", path);
            Gson gson = new Gson();
            PackageManager pm = getPackageManager();
            ArrayList<AppInfo> apps = getAppsData(pm);
            String appsInfo = gson.toJson(apps);
            Utils.saveToShared(getApplicationContext(),"appsInfo",appsInfo);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1000);
        }
    }
    public ArrayList<AppInfo> getAppsData(PackageManager packageManager){
        /* getting apps data */
        List<ApplicationInfo> applicationsInfo = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<AppInfo> apps = new ArrayList<>();
        for(ApplicationInfo i : applicationsInfo){
            if(! i.packageName.toLowerCase().contains("com.android") ) {
                AppInfo appInfo = new AppInfo(
                        i.loadLabel(getPackageManager()).toString(), //title
                        i.packageName, //packageName
                        i.sourceDir //srcdir
                );
                apps.add(appInfo);
            }
        }
        Collections.sort(apps, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return o1.title.compareTo(o2.title);
            }
        });
        return apps;
    }
}
