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
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
//    public List<ResolveInfo> getBrowserList(){
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("google.com"));
//
//        PackageManager pm = context.getPackageManager();
//
//
//        List<ResolveInfo> browserList;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            browserList = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
//        } else {
//            browserList = pm.queryIntentActivities(intent, 0);
//        }
//
//        return browserList;
//    }
// --Commented out by Inspection STOP (15/8/18 9:45 PM)


    public ArrayList<String> getAllSettingList(){
        ArrayList<String> settingList = new ArrayList<>();

        Field[] fields = Settings.class.getDeclaredFields();



        for (Field field : fields){
            if (Modifier.isStatic(field.getModifiers())){
                String s = field.getName();
                settingList.add(s);
            }
        }

        return settingList;
    }

}
