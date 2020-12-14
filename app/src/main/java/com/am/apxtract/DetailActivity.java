package com.am.apxtract;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import Utils.Utils;

public class DetailActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Button extractApkBtn,shareApkBtn;
    TextView appNameView,packNameView,srcDirView;
    String appName,packName,srcDirStr,dstDir;
    ImageView imgView;
    Drawable img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        initConsts();
        setConsts();
        setTitle(appName);
        initOnClickListeners();
    }
    private void initOnClickListeners(){
        extractApkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask copyTask = new CopyTask();
                copyTask.execute(new String[]{null,null,null});
            }
        });
        shareApkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyTask copyTask = new CopyTask();
                copyTask.setShare(true);
                copyTask.execute(new String[]{null,null,null});
            }
        });
    }
    private void initConsts(){
        extractApkBtn = findViewById(R.id.extractBtn);
        shareApkBtn = findViewById(R.id.shareBtn);
        appNameView = findViewById(R.id.appName);
        packNameView= findViewById(R.id.packageName);
        srcDirView = findViewById(R.id.srcDir);
        imgView = findViewById(R.id.appLogo);
        dstDir = Utils.getFromShared(getApplicationContext(),"dstDir");
        appName = getIntent().getStringExtra("appName");
        packName = getIntent().getStringExtra("packageName");
        srcDirStr = getIntent().getStringExtra("srcDir");
        try {
            img = getPackageManager().getApplicationIcon(packName);
        } catch(PackageManager.NameNotFoundException e) {
            img = getResources().getDrawable(R.mipmap.ic_launcher_round);
        }
    }
    private void setConsts(){
        appNameView.setText(appName);
        packNameView.setText(packName);
        srcDirView.setText(srcDirStr);
        imgView.setImageDrawable(img);
    }
    private class CopyTask extends AsyncTask<String,Void,String>{
        private boolean share;
        private File apk;
        public void setShare(boolean a){
            this.share = a;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected String doInBackground(String... strings) {
            String path = dstDir + "/" + appName + ".apk";
            apk = new File(srcDirStr);
            File dest = new File(path);
            Utils.copyFile(apk,dest);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(share == true) {
                Utils.sendVia(getApplicationContext(),apk);
            }else{
                String path = dstDir + "/" + appName + ".apk";
                Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
            }
        }
    }
}
