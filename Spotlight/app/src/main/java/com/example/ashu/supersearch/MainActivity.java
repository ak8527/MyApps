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

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "MainActivity";
    private static final int MY_STORAGE_REQUEST_CODE = 222;
    private static final int MY_CONTACT_REQUEST_CODE = 333;
    private static final int MY_STORAGE_AND_CONTACT_REQUEST_CODE = 444;
    private StorageAdapter storageAdapter;
    private RecyclerView recyclerView;
    private RecyclerView appRecyclerView;
    private RecyclerView contactRecyclerView;
    private RecyclerView songRecyclerView;
    private RecyclerView videoRecyclerView;
    private AppAdaptor appAdaptor;
    private ContactAdaptor contactAdaptor;
    private SongAdaptor songAdaptor;
    private VideoAdaptor videoAdaptor;
    private final ArrayList<Song> mySongArrayList = new ArrayList<>();
    private final ArrayList<ContactList> myContactList = new ArrayList<>();
    private final ArrayList<App> appArrayList = new ArrayList<>();
    private final ArrayList<Video> videoArrayList = new ArrayList<>();
    private final ArrayList<Storage> myStorageList = new ArrayList<>();

    private InfoList infoList;

    private TextView moreTv;
    private final AppTask appTask = new AppTask();

    private final StorageTask storageTask = new StorageTask();
    private final VideoTask videoTask = new VideoTask();
    private final SongTask songTask = new SongTask();
    private final ContactTask contactTask = new ContactTask();
    private LinearLayout permissionLayout;
    private TextView contactView;
    private TextView appView;
    private TextView songView;
    private TextView fileView;
    private TextView videoView;

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
        Button permissionBtn = findViewById(R.id.permissionBtn);
        permissionLayout = findViewById(R.id.permissionLayout);
        ImageView settingMenu = findViewById(R.id.threeDotMenu);

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


        /*
         * Calling findViewById on recyclerView and searchView.
         */
        SearchView searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.storageRecyclerView);
        appRecyclerView = findViewById(R.id.appRecyclerView);
        contactRecyclerView = findViewById(R.id.contactRecyclerView);
        songRecyclerView = findViewById(R.id.songRecyclerView);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);

        recyclerView.setNestedScrollingEnabled(false);
        contactRecyclerView.setNestedScrollingEnabled(false);
        songRecyclerView.setNestedScrollingEnabled(false);
        videoRecyclerView.setNestedScrollingEnabled(false);


        /*
         * Setting Layout Manger for recyclerView.
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

        /*
         * Calling SearchView widget.
         */

        searchView.setOnQueryTextListener(this);

        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPermissionGranted();
            }
        });

        /*
         * Checking contact and storage permission and executing task according to them.
         */
        if (isContactPermission() && isStoragePermission()) {
            isContactTaskExecute();
            isStorageTaskExecute();
            isPermissionLayoutVisible();
        } else if (isContactPermission() && !isStoragePermission()) {
            isContactTaskExecute();
        } else if (!isContactPermission() && isStoragePermission()) {
            isStorageTaskExecute();
        }


        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageAdapter.moreFiles(true);
                storageAdapter.notifyDataSetChanged();
                moreTv.setVisibility(View.GONE);
            }
        });


        /*
         * Calling methods for setting contact, storage and app adaptor.
         */
        setAppAdaptor();
        setStorageAdapter();
        setContactAdaptor();

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     *
     * @param newText is text which is search on searchView widget.
     * @return false
     */

    @Override
    public boolean onQueryTextChange(String newText) {

        boolean appAns;
        appAns = appAdaptor.filter(newText);
        appAdaptor.notifyDataSetChanged();
        if (appAns) {
            appView.setVisibility(View.VISIBLE);
        } else {
            appView.setVisibility(View.GONE);
        }

        if (isContactPermission()) {
            boolean contactAns = contactAdaptor.filter(newText);
            contactAdaptor.notifyDataSetChanged();
            if (contactAns) {
                contactView.setVisibility(View.VISIBLE);
            } else {
                contactView.setVisibility(View.GONE);
            }
        }


        if (isStoragePermission()) {

            boolean storageAns = storageAdapter.filter(newText);

            storageAdapter.notifyDataSetChanged();
            if (storageAns) {
                boolean moreStorage = storageAdapter.isMoreFiles();
                if (moreStorage) {
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


    /**
     * initializing and setting adaptor for song, video and files.
     */

    private void setStorageAdapter() {
        storageAdapter = new StorageAdapter(this, myStorageList);
        recyclerView.setAdapter(storageAdapter);

        songAdaptor = new SongAdaptor(this, mySongArrayList);
        songRecyclerView.setAdapter(songAdaptor);

        videoAdaptor = new VideoAdaptor(this, videoArrayList);
        videoRecyclerView.setAdapter(videoAdaptor);

    }


    /**
     * initializing and setting adaptor for contact.
     */

    private void setContactAdaptor() {

        contactAdaptor = new ContactAdaptor(this, myContactList);
        contactRecyclerView.setAdapter(contactAdaptor);

    }


    private void setAppAdaptor() {
        /*
           Set recyclerView for App.
           (ArrayList<App>) infoList.GetAllInstalledApkInfo()
         */
        appAdaptor = new AppAdaptor(this, appArrayList);
        appRecyclerView.setNestedScrollingEnabled(false);
        appRecyclerView.setAdapter(appAdaptor);
        appTask.execute();


    }

    private void isPermissionGranted() {
        if (!isContactPermission() && !isStoragePermission()) {

            /*
             * Requesting Permission for both Contact and Storage.
             */

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_AND_CONTACT_REQUEST_CODE);

        } else if (!isContactPermission()) {

            /*
             * Requesting Contact Permission.
             */

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_CONTACT_REQUEST_CODE);

        } else if (!isStoragePermission()) {
            /*
             * Requesting Storage Permission.
             */

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);

        }
    }

    /**
     *
     * @param requestCode for checking which runtime permission is called.
     * @param permissions for requesting the desired permission.
     * @param grantResults for checking if the permission is granted.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case ContactAdaptor.MY_TELEPHONE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactAdaptor.calling();
                }
                return;
            }


            case MY_STORAGE_AND_CONTACT_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        isContactTaskExecute();
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        isStorageTaskExecute();
                }
                isPermissionLayoutVisible();
                return;
            }

            case MY_CONTACT_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    contactTask.execute();
                isPermissionLayoutVisible();
                return;
            }

            case MY_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    isStorageTaskExecute();
                isPermissionLayoutVisible();

            }
        }
    }

    /**
     * @return storagePermission status.
     */

    private boolean isStoragePermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * @return contactPermission status.
     */


    private boolean isContactPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Method for execution of SongTask,VideoTask and StorageTask.
     */

    private void isStorageTaskExecute() {
        songTask.execute();
        videoTask.execute();
        storageTask.execute();
    }

    /**
     * Method for execution of ContactTask.
     */

    private void isContactTaskExecute() {
        contactTask.execute();
    }

    /**
     * Method for setting the permissionLayout invisible.
     */

    private void isPermissionLayoutVisible() {
        if (isStoragePermission() && isContactPermission()) {
            permissionLayout.setVisibility(View.GONE);
        }
    }

    /**
     * define storageTask for getting thee list of files and folders.
     */

    class StorageTask extends AsyncTask<Void, Void, ArrayList<Storage>> {

        @Override
        protected ArrayList<Storage> doInBackground(Void... voids) {
            final String state = Environment.getExternalStorageState();
            ArrayList<Storage> myStorageList1 = new ArrayList<>();
            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  // we can read the External Storage...
                myStorageList1 = infoList.getMyStorageList(new File("/storage/"));
                Log.e("StorageTask", "onCreate: " + Environment.getExternalStorageDirectory().getPath());

            }
            return myStorageList1;

        }
        @Override
        protected void onPostExecute(ArrayList<Storage> storage) {
            super.onPostExecute(storage);
            myStorageList.clear();
            myStorageList.addAll(storage);
            storageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * define videoTask for getting list of videos.
     */

    class VideoTask extends AsyncTask<Void, Void, ArrayList<Video>> {
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

    /**
     * define songTask for getting the list of songs.
     */

    class SongTask extends AsyncTask<Void, Void, ArrayList<Song>> {
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

    /**
     * define contactTask for getting the list of contacts.
     */

    class ContactTask extends AsyncTask<Void, Void, ArrayList<ContactList>> {

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

    /**
     * define appTask for getting the list of installed apps.
     */

    class AppTask extends AsyncTask<Void, Void, ArrayList<App>> {

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
}
