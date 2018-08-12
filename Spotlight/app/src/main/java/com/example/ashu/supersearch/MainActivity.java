package com.example.ashu.supersearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ashu.supersearch.App.App;
import com.example.ashu.supersearch.App.AppAdaptor;
import com.example.ashu.supersearch.FileBrowser.Storage;
import com.example.ashu.supersearch.FileBrowser.StorageAdapter;
import com.example.ashu.supersearch.Info.InfoList;
import com.example.ashu.supersearch.Movie.Video;
import com.example.ashu.supersearch.Movie.VideoAdaptor;
import com.example.ashu.supersearch.Music.Song;
import com.example.ashu.supersearch.Music.SongAdaptor;
import com.example.ashu.supersearch.Phone.ContactAdaptor;
import com.example.ashu.supersearch.Phone.ContactList;
import com.example.ashu.supersearch.Settings.SettingActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "MainActivity";
    private static final int MY_STORAGE_REQUEST_CODE = 222;
    SearchView searchView;
    StorageAdapter storageAdapter;
    RecyclerView recyclerView, appRecyclerView, contactRecyclerView, songRecyclerView, videoRecyclerView;
    AppAdaptor appAdaptor;
    ContactAdaptor contactAdaptor;
    SongAdaptor songAdaptor;
    VideoAdaptor videoAdaptor;
    ArrayList<Song> mySongArrayList = new ArrayList<>();
    ArrayList<ContactList> myContactList = new ArrayList<>();
    ArrayList<App> appArrayList = new ArrayList<>();
    ArrayList<Video> videoArrayList = new ArrayList<>();
    ArrayList<Storage> myStorageList = new ArrayList<>();


    InfoList infoList;

    TextView moreTv;
    AppTask appTask = new AppTask();

    StorageTask storageTask = new StorageTask();
    VideoTask videoTask = new VideoTask();
    SongTask songTask = new SongTask();
    ContactTask contactTask = new ContactTask();
    LinearLayout permissionLayout;
    ImageView settingMenu;


    TextView contactView, appView, songView, fileView, videoView;
    Button permissionBtn;
    boolean contactPermission, storagePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoList = new InfoList(this);

        contactView = findViewById(R.id.contactHelper);
        appView = findViewById(R.id.appName);
        songView = findViewById(R.id.songName);
        fileView = findViewById(R.id.filesName);
        videoView = findViewById(R.id.videoName);
        permissionBtn = findViewById(R.id.permissionBtn);
        permissionLayout = findViewById(R.id.permissionLayout);
        settingMenu = findViewById(R.id.threeDotMenu);

        moreTv = findViewById(R.id.moreFileView);


        final PopupMenu popupMenu = new PopupMenu(getBaseContext(), settingMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        settingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.settingOption:
                                Log.e(TAG, "onMenuItemClick: ");
                                Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                                getBaseContext().startActivity(intent);
                                return true;
                            default:
                                return MainActivity.super.onOptionsItemSelected(item);

                        }
                    }
                });
            }
        });


        /**
         * Calling findViewById on recyclerView and searchView.
         */
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.storageRecyclerView);
        appRecyclerView = findViewById(R.id.appRecyclerView);
        contactRecyclerView = findViewById(R.id.contactRecyclerView);
        songRecyclerView = findViewById(R.id.songRecyclerView);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);

        recyclerView.setNestedScrollingEnabled(false);
        contactRecyclerView.setNestedScrollingEnabled(false);
        songRecyclerView.setNestedScrollingEnabled(false);
        videoRecyclerView.setNestedScrollingEnabled(false);


        /**
         * Setting Layout Manger on recyclerView.
         */

        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        appRecyclerView.setLayoutManager(linearLayoutManager);

        searchView.setOnQueryTextListener(this);

        contactPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);

        storagePermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        permission();
        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isPermissionGranted()){
                   Log.e("Button", "onClick: ");
                   permissionLayout.setVisibility(View.GONE);
                   contactTask.execute();

               }
            }
        });

        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageAdapter.moreFiles(true);
                storageAdapter.notifyDataSetChanged();
                moreTv.setVisibility(View.GONE);
            }
        });


        contactSearch();
        storageItemSearch();
        appSearch();

//
//        for (ResolveInfo r : browserList){
//            Log.e("ResolveInfo", "onCreate: "+r.activityInfo.packageName);
//        }


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        boolean appAns = appAdaptor.filter(newText);
        appAdaptor.notifyDataSetChanged();
        if (appAns) {
            appView.setVisibility(View.VISIBLE);
        } else {
            appView.setVisibility(View.GONE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            boolean contactAns = contactAdaptor.filter(newText);
            contactAdaptor.notifyDataSetChanged();
            if (contactAns) {
                contactView.setVisibility(View.VISIBLE);
            } else {
                contactView.setVisibility(View.GONE);
            }
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            boolean storageAns = storageAdapter.filter(newText);

            storageAdapter.notifyDataSetChanged();
            if (storageAns) {
                boolean moreStorage = storageAdapter.isMoreFiles();
                if (moreStorage){
                    moreTv.setVisibility(View.VISIBLE);
                    storageAdapter.moreFiles(false);
                } else {
                    moreTv.setVisibility(View.GONE);

                    storageAdapter.moreFiles(false);
                }
                fileView.setVisibility(View.VISIBLE);
            } else {
                moreTv.setVisibility(View.GONE);
                fileView.setVisibility(View.GONE);
            }




            boolean songAns = songAdaptor.filter(newText);
            songAdaptor.notifyDataSetChanged();
            if (songAns) {
                songView.setVisibility(View.VISIBLE);
            } else {
                songView.setVisibility(View.GONE);
            }

            boolean videoAns = videoAdaptor.filter(newText);
            videoAdaptor.notifyDataSetChanged();
            if (videoAns) {
                videoView.setVisibility(View.VISIBLE);
            } else {
                videoView.setVisibility(View.GONE);
            }


        }


        return false;
    }

    public void storageItemSearch() {
        /**
         * Checking Storage State and setting storage recyclerView
         */
//        final String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  // we can read the External Storage...
//            myStorageList = infoList.getMyStorageList(Environment.getExternalStorageDirectory());
//            Log.e(TAG, "onCreate: " + Environment.getExternalStorageDirectory().getPath());
//
//        }
        Log.e(TAG, "storageItemSearch: "+ Environment.isExternalStorageEmulated());

        storageAdapter = new StorageAdapter(this, myStorageList);
        recyclerView.setAdapter(storageAdapter);

        /**
         *  Set recyclerView for Song.
         *  (ArrayList<Song>) infoList.getAllAudioFromDevice()
         */

        songAdaptor = new SongAdaptor(this, mySongArrayList);
        songRecyclerView.setAdapter(songAdaptor);


        /**
         *  Set recyclerView for Video.
         *   (ArrayList<Video>) infoList.getAllVideoFromDevice()
         */
        videoAdaptor = new VideoAdaptor(this, videoArrayList);
        videoRecyclerView.setAdapter(videoAdaptor);


    }


    public void contactSearch() {

        /**
         *  Set recyclerView for Contact.
         */
        contactAdaptor = new ContactAdaptor(this, myContactList);
        contactRecyclerView.setAdapter(contactAdaptor);

    }


    public void appSearch() {
        /**
         *  Set recyclerView for App.
         *  (ArrayList<App>) infoList.GetAllInstalledApkInfo()
         */
        appAdaptor = new AppAdaptor(this, appArrayList);
        appRecyclerView.setNestedScrollingEnabled(false);
        appRecyclerView.setAdapter(appAdaptor);


    }

    public void permission() {
        appTask.execute();
        if (contactPermission) {
            contactTask.execute();
            permissionLayout.setVisibility(View.GONE);
    }
    if (storagePermission){
            songTask.execute();
            videoTask.execute();
            storageTask.execute();
    }

    if (contactPermission && storagePermission){
            permissionLayout.setVisibility(View.GONE);
    }
    }



    class StorageTask extends AsyncTask<Void, Void, ArrayList<Storage>> {

        @Override
        protected ArrayList<Storage> doInBackground(Void... voids) {
            final String state = Environment.getExternalStorageState();
            ArrayList<Storage> myStorageList1 = new ArrayList<>();
            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  // we can read the External Storage...
                myStorageList1 = infoList.getMyStorageList(Environment.getExternalStorageDirectory());
                Log.e("StorageTask", "onCreate: " + Environment.getExternalStorageDirectory().getPath());

            }
            return myStorageList1;

        }


        @Override
        protected void onPostExecute(ArrayList<Storage> storages) {
            super.onPostExecute(storages);
            Log.e("OnPost", "onPostExecute: "+ storages.get(6).getFileName() );
            myStorageList.clear();
            myStorageList.addAll(storages);
            storageAdapter.notifyDataSetChanged();
        }
    }


    public class VideoTask extends AsyncTask<Void,Void,ArrayList<Video>>{
        @Override
        protected ArrayList<Video> doInBackground(Void... voids) {
            return (ArrayList<Video>) infoList.getAllVideoFromDevice();
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videos) {
                super.onPostExecute(videos);
                videoArrayList.clear();
                videoArrayList.addAll(videos);
                videoAdaptor.notifyDataSetChanged();

        }
    }


    class SongTask extends AsyncTask<Void,Void,ArrayList<Song>>{
        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {
            return (ArrayList<Song>) infoList.getAllAudioFromDevice();
        }


        @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            super.onPostExecute(songs);
            mySongArrayList.clear();
            mySongArrayList.addAll(songs);
            songAdaptor.notifyDataSetChanged();
        }
    }

    public class ContactTask extends AsyncTask<Void, Void, ArrayList<ContactList>> {

        @Override
        protected ArrayList<ContactList> doInBackground(Void... voids) {
            return infoList.getAllContact();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactList> contactLists) {
            super.onPostExecute(contactLists);
            myContactList.clear();
            myContactList.addAll(contactLists);
            contactAdaptor.notifyDataSetChanged();

        }
    }

    public  class AppTask extends AsyncTask<Void, Void, ArrayList<App>> {

        @Override
        protected ArrayList<App> doInBackground(Void... voids) {
            return (ArrayList<App>) infoList.GetAllInstalledApkInfo();
        }

        @Override
        protected void onPostExecute(ArrayList<App> apps) {
            super.onPostExecute(apps);
            appArrayList.clear();
            appArrayList.addAll(apps);
            appAdaptor.notifyDataSetChanged();

        }
    }

    public boolean isPermissionGranted(){
        if ((contactPermission && !storagePermission) || (!contactPermission && storagePermission)) {
            if (contactPermission) {
                Log.e("MainActivity1", "onClick: ");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MainActivity2", "onClick: ");
                    storagePermission = true;
                    return true;
                }
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_STORAGE_REQUEST_CODE);
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MainActivity3", "onClick: ");
                    contactPermission = true;
                    return true;

                }
            }
        } else  {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
            return true;
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case ContactAdaptor.MY_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    contactAdaptor.calling();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

//            case MY_STORAGE_REQUEST_CODE: {
//                if (grantResults.length>0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Log.e(TAG, "onRequestPermissionsResult: "+"Contact" );
//                    contactTask.execute();
//
//
//                }
//
//                if (grantResults.length >0
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                    Log.e(TAG, "onRequestPermissionsResult: "+"Storage");
//                    songTask.execute();
//                    videoTask.execute();
//                    storageTask.execute();
//
//
//                }
//            }
//


            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
