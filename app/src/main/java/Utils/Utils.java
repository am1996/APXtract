package Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;

public class Utils {
    public static String[] getStringArrayFromAppsInfo(ArrayList<AppInfo> appList){
        String str[] = new String[appList.size()];
        for(int i=0;i<appList.size();i++){
            str[i] = appList.get(i).title;
        }
        return str;
    }
    public static void saveToShared(Context c,String key,String value){
        SharedPreferences mPrefs = c.getSharedPreferences("com.am.APXtract",c.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = mPrefs.edit();
        spEditor.putString(key,value);
        spEditor.commit();
    }
    public static String getFromShared(Context c,String key){
        SharedPreferences mPrefs = c.getSharedPreferences("com.am.APXtract",c.MODE_PRIVATE);
        return mPrefs.getString(key,"None");
    }
    /*
     * Copy file from one place to another
     * @param inFile
     * @param outFile
     * @return returns void
     */
    public static void copyFile(File inFile,File outFile){
        try {
            FileInputStream fis = new FileInputStream(inFile);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer_stream = new byte[1024];
            int len;
            while((len = fis.read(buffer_stream)) > 0)
                fos.write(buffer_stream,0,len);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     *share file
     * @param File file
     * returns void
     */
    public static void sendVia(Context context,File file) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));
        Intent chooser = Intent.createChooser(intentShareFile,"Share Application");
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooser);
    }
    public static boolean checkPermission(Context c,String permission ) {
        int result = ContextCompat.checkSelfPermission(c, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public static void requestPermissions(Activity a, String permissions[]){
        ActivityCompat.requestPermissions(a, permissions, 101);
    }
}
