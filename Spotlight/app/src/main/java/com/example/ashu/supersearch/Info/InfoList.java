package com.example.ashu.supersearch.Info;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InfoList {

    private final Context context;
    private String name;
    private String path;

    private MediaInfo media;


    public InfoList(Context context) {
        this.context = context;
    }


    public List<MediaInfo> getAllVideoFromDevice(){
        List<MediaInfo> tempVideoList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor c = context.getContentResolver().query(uri,
                projection,
                null,
                null,
                null);

        if (c != null) {
            while (c.moveToNext()) {

                path = c.getString(0);

                name = path.substring(path.lastIndexOf("/") + 1);

                /*
      Method for getting list of video
     */
                media = new MediaInfo(name, path);

                tempVideoList.add(media);
            }
            c.close();
        }

        return tempVideoList;

    }


    public List<MediaInfo> getAllAudioFromDevice() {

        final List<MediaInfo> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri,
                projection,
                null,
                null,
                null);
        if (c != null) {
            while (c.moveToNext()) {

                path = c.getString(0);

                name = path.substring(path.lastIndexOf("/") + 1);

                /*
      Method for getting list of all song.
     */
                 media = new MediaInfo(name, path);

                tempAudioList.add(media);
            }
            c.close();
        }

        return tempAudioList;
    }


    /**
     *  Method for getting app list from device.
     */


    public List<MediaInfo> GetAllInstalledApkInfo(){
        Drawable drawable;
        String appName,packageName;
        List<MediaInfo> apps = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList){

            ActivityInfo activityInfo = resolveInfo.activityInfo;

                packageName = activityInfo.applicationInfo.packageName;
                drawable = getAppIconByPackageName(packageName);
                appName = GetAppName(packageName);
                media = new MediaInfo(appName,packageName,drawable);
                apps.add(media);
        }

        return apps;

    }


    private Drawable getAppIconByPackageName(String ApkTempPackageName){

        Drawable drawable;

        try{
            drawable = context.getPackageManager().getApplicationIcon(ApkTempPackageName);

        }
        catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        }
        return drawable;
    }

    private String GetAppName(String ApkPackageName){

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context.getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if(applicationInfo!=null){

                Name = (String)packageManager.getApplicationLabel(applicationInfo);
            }

        }catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }


    /**
     * Method for getting file and folder name.
     */

    private final ArrayList<MediaInfo> myStorageList = new ArrayList<>();

    public ArrayList<MediaInfo> getMyStorageList(File directory) {
        getAllFilesOfDir(directory);
        return myStorageList;
    }

    private void getAllFilesOfDir(File directory) {
        // Log.d(TAG, "Directory: " + directory.getAbsolutePath() + "\n");
        myStorageList.add(new MediaInfo(directory.getName(),directory.getAbsolutePath()));

        final File[] files = directory.listFiles();

        if ( files != null ) {
            for ( File file : files ) {
                if ( file != null ) {
                    if ( file.isDirectory() ) {  // it is a folder...
                        getAllFilesOfDir(file);
                    }
                    else {  // it is a file...
                        myStorageList.add(new MediaInfo(file.getName(),file.getAbsolutePath()));
                        //  Log.d(TAG, "File: " + file.getName() + "\n");
                    }
                }
            }
        }
    }


    /**
     * get list of contacts.
     */

    public ArrayList<MediaInfo> getAllContact(){
        ArrayList<MediaInfo> contactLists = new ArrayList<>();
//
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        contactLists.add(new MediaInfo(displayName,displayNumber));
                        //haven't seen this number yet: do something with this contact!
                    }  //don't do anything with this contact because we've already found this number

                }
            } finally {
                cursor.close();
            }
        }

        return contactLists;
    }

// --Commented out by Inspection START (15/8/18 9:45 PM):
//    /**
//     * Get list of browser.
//     */
//
    public List<MediaInfo> getBrowserList(){
        List<MediaInfo> browserAppList = new ArrayList<>();
        String appName,packageName;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.google.com"));

        PackageManager pm = context.getPackageManager();


        List<ResolveInfo> browserList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            browserList = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        } else {
            browserList = pm.queryIntentActivities(intent, 0);
        }

        browserAppList.add(new MediaInfo("Google Play Store","com.android.vending"));
        browserAppList.add(new MediaInfo("YouTube","com.google.android.youtube"));


        for (ResolveInfo resolveInfo : browserList){
                packageName = resolveInfo.activityInfo.packageName;
                appName = GetAppName(packageName);
                browserAppList.add(new MediaInfo(appName,packageName));
        }


        return browserAppList;
    }
// --Commented out by Inspection STOP (15/8/18 9:45 PM)


    public ArrayList<MediaInfo> getAllSettingList(){
        ArrayList<MediaInfo> settingList = new ArrayList<>();


        settingList.add(new MediaInfo("Airplane Mode","android.settings.AIRPLANE_MODE_SETTINGS"));
        settingList.add(new MediaInfo("Developer options","android.settings.APPLICATION_DEVELOPMENT_SETTINGS"));
        settingList.add(new MediaInfo("Bluetooth","android.settings.BLUETOOTH_SETTINGS"));
        settingList.add(new MediaInfo("Cast","android.settings.CAST_SETTINGS"));
        settingList.add(new MediaInfo("Data roaming","android.settings.DATA_ROAMING_SETTINGS"));
        settingList.add(new MediaInfo("Device info","android.settings.DEVICE_INFO_SETTINGS"));
        settingList.add(new MediaInfo("Display","android.settings.DISPLAY_SETTINGS"));
        settingList.add(new MediaInfo("Home","android.settings.HOME_SETTINGS"));
        settingList.add(new MediaInfo("Internal storage","android.settings.INTERNAL_STORAGE_SETTINGS"));
        settingList.add(new MediaInfo("Manage applications","android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS"));
        settingList.add(new MediaInfo("NFC","android.settings.NFC_SETTINGS"));
        settingList.add(new MediaInfo("Sound","android.settings.SOUND_SETTINGS"));
        settingList.add(new MediaInfo("Usage access","android.settings.USAGE_ACCESS_SETTINGS"));
        settingList.add(new MediaInfo("Wifi","android.settings.WIFI_SETTINGS"));
        settingList.add(new MediaInfo("Notification access","android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        settingList.add(new MediaInfo("Battery saver","android.settings.BATTERY_SAVER_SETTINGS"));
        settingList.add(new MediaInfo("Ignore battery optimizations","android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS"));
        settingList.add(new MediaInfo("Manage overlay","android.settings.action.MANAGE_OVERLAY_PERMISSION"));
        settingList.add(new MediaInfo("VPN","android.settings.VPN_SETTINGS"));
        settingList.add(new MediaInfo("Manage default apps","android.settings.MANAGE_DEFAULT_APPS_SETTINGS"));
        settingList.add(new MediaInfo("Manage unknown app sources","android.settings.MANAGE_UNKNOWN_APP_SOURCES"));

        return settingList;
    }

}
