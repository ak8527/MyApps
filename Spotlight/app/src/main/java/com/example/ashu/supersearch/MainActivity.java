package com.example.ashu.supersearch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ashu.supersearch.Adaptor.AppAdaptor;
import com.example.ashu.supersearch.Adaptor.ContactAdaptor;
import com.example.ashu.supersearch.Adaptor.SearchAppAdaptor;
import com.example.ashu.supersearch.Adaptor.SettingAdaptor;
import com.example.ashu.supersearch.Adaptor.SongAdaptor;
import com.example.ashu.supersearch.Adaptor.StorageAdapter;
import com.example.ashu.supersearch.Adaptor.VideoAdaptor;
import com.example.ashu.supersearch.AsyncWork.AppTask;
import com.example.ashu.supersearch.AsyncWork.ContactTask;
import com.example.ashu.supersearch.AsyncWork.MediaTask;
import com.example.ashu.supersearch.AsyncWork.StorageTask;
import com.example.ashu.supersearch.Info.InfoList;
import com.example.ashu.supersearch.Interface.MediaResponse;
import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.setting.SettingActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.example.ashu.supersearch.AsyncWork.AppTask;


@SuppressWarnings("WeakerAccess")
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MediaResponse {
    private static final String TAG = "MainActivity";
    private static final int MY_STORAGE_REQUEST_CODE = 222;
    private static final int MY_CONTACT_REQUEST_CODE = 333;
    private static final int MY_STORAGE_AND_CONTACT_REQUEST_CODE = 444;


    @BindView(R.id.filesRecyclerView)
    RecyclerView filesRecyclerView;
    @BindView(R.id.appRecyclerView)
    RecyclerView appRecyclerView;
    @BindView(R.id.contactRecyclerView)
    RecyclerView contactRecyclerView;
    @BindView(R.id.songRecyclerView)
    RecyclerView songRecyclerView;
    @BindView(R.id.videoRecyclerView)
    RecyclerView videoRecyclerView;
    @BindView(R.id.settingRecyclerView)
    RecyclerView settingRecyclerView;
    @BindView(R.id.searchAppRecyclerView)
    RecyclerView searchAppRecyclerView;


    private StorageAdapter storageAdapter;
    private AppAdaptor appAdaptor;
    private SearchAppAdaptor searchAppAdaptor;
    private SettingAdaptor settingAdaptor;
    private ContactAdaptor contactAdaptor;
    private SongAdaptor songAdaptor;
    private VideoAdaptor videoAdaptor;


    private final ArrayList<MediaInfo> mySongArrayList = new ArrayList<>();
    private final ArrayList<MediaInfo> myContactList = new ArrayList<>();
    private final ArrayList<MediaInfo> myAppArrayList = new ArrayList<>();
    private final ArrayList<MediaInfo> myVideoArrayList = new ArrayList<>();
    private final ArrayList<MediaInfo> myStorageList = new ArrayList<>();
    private final ArrayList<MediaInfo> mySettingList = new ArrayList<>();


    @BindView(R.id.moreFileView)
    TextView moreTv;
    @BindView(R.id.permissionLayout)
    ConstraintLayout permissionLayout;
    @BindView(R.id.contactName)
    TextView contactView;
    @BindView(R.id.appName)
    TextView appView;
    @BindView(R.id.songName)
    TextView songView;
    @BindView(R.id.filesName)
    TextView fileView;
    @BindView(R.id.videoName)
    TextView videoView;
    @BindView(R.id.settingName)
    TextView settingView;
    @BindView(R.id.searchName)
    TextView searchName;
    @BindView(R.id.permissionBtn)
    Button permissionBtn;
    @BindView(R.id.threeDotMenu)
    ImageView settingMenu;
    @BindView(R.id.searchView)
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InfoList infoList = new InfoList(this);

        setHelperText();


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


        settingRecyclerView.setNestedScrollingEnabled(false);
        filesRecyclerView.setNestedScrollingEnabled(false);
        contactRecyclerView.setNestedScrollingEnabled(false);
        songRecyclerView.setNestedScrollingEnabled(false);
        videoRecyclerView.setNestedScrollingEnabled(false);


        /*
         * Setting Layout Manager for recyclerView.
         */

        settingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        int browserListSize = infoList.getBrowserList().size();
        if (browserListSize > 6) {
            browserListSize = 6;
        }
        GridLayoutManager searchLayoutManager = new GridLayoutManager(this, browserListSize);
        searchAppRecyclerView.setLayoutManager(searchLayoutManager);
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


        searchAppAdaptor = new SearchAppAdaptor(this, (ArrayList<MediaInfo>) infoList.getBrowserList());
        searchAppRecyclerView.setAdapter(searchAppAdaptor);
        searchAppRecyclerView.setVisibility(View.GONE);


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * @param newText is text which is search on searchView widget.
     * @return false
     */

    @Override
    public boolean onQueryTextChange(String newText) {

        if (!newText.isEmpty()) {
            searchAppRecyclerView.setVisibility(View.VISIBLE);
            searchName.setVisibility(View.VISIBLE);
            searchAppAdaptor.filter(newText);
            setSearchApp(newText);
        } else {
            searchAppRecyclerView.setVisibility(View.GONE);
            searchName.setVisibility(View.GONE);
        }

        boolean appAns, settingAns;

        settingAns = settingAdaptor.filter(newText);
        settingAdaptor.notifyDataSetChanged();
        if (settingAns) {
            settingView.setVisibility(View.VISIBLE);
        } else {
            settingView.setVisibility(View.GONE);
        }
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
        filesRecyclerView.setAdapter(storageAdapter);

        songAdaptor = new SongAdaptor(this, mySongArrayList);
        songRecyclerView.setAdapter(songAdaptor);

        videoAdaptor = new VideoAdaptor(this, myVideoArrayList);
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
        appAdaptor = new AppAdaptor(this, myAppArrayList);
        appRecyclerView.setNestedScrollingEnabled(false);
        appRecyclerView.setAdapter(appAdaptor);

        settingAdaptor = new SettingAdaptor(this, mySettingList);
        settingRecyclerView.setAdapter(settingAdaptor);

        new AppTask(this, this).execute();


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
     * @param requestCode  for checking which runtime permission is called.
     * @param permissions  for requesting the desired permission.
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
                    new ContactTask(this, this).execute();
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
        new MediaTask(this, this).execute();
        new StorageTask(this, this).execute();
    }

    /**
     * Method for execution of ContactTask.
     */

    private void isContactTaskExecute() {
        new ContactTask(this, this).execute();
    }

    /**
     * Method for setting the permissionLayout invisible.
     */

    private void isPermissionLayoutVisible() {
        if (isStoragePermission() && isContactPermission()) {
            permissionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getAppResponse(ArrayList<MediaInfo> appArrayList, ArrayList<MediaInfo> settingArrayList) {
        myAppArrayList.clear();
        myAppArrayList.addAll(appArrayList);
        appAdaptor.notifyDataSetChanged();

        mySettingList.clear();
        mySettingList.addAll(settingArrayList);
        settingAdaptor.notifyDataSetChanged();
    }

    @Override
    public void getContactResponse(ArrayList<MediaInfo> contactArrayList) {
        myContactList.clear();
        myContactList.addAll(contactArrayList);
        contactAdaptor.notifyDataSetChanged();
    }

    @Override
    public void getStorageResponse(ArrayList<MediaInfo> storageArrayList) {
        myStorageList.clear();
        myStorageList.addAll(storageArrayList);
        storageAdapter.notifyDataSetChanged();

    }

    @Override
    public void getMediaResponse(ArrayList<MediaInfo> songArrayList, ArrayList<MediaInfo> videoArrayList) {
        mySongArrayList.clear();
        mySongArrayList.addAll(songArrayList);
        songAdaptor.notifyDataSetChanged();

        myVideoArrayList.clear();
        myVideoArrayList.addAll(videoArrayList);
        videoAdaptor.notifyDataSetChanged();
    }


    private void setSearchApp(String text) {
        String searchStart = "Search ";
        String newText = searchStart + text;
        SpannableString str = new SpannableString(newText);
        int startingIndex = searchStart.length();
        int endingIndex = startingIndex + text.length();
        str.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        searchName.setText(str);
    }

    private void setHelperText() {
        appView.setText(R.string.apps);
        contactView.setText(R.string.contact);
        fileView.setText(R.string.files);
        songView.setText(R.string.song);
        videoView.setText(R.string.videos);
        settingView.setText(R.string.settings);
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


}
