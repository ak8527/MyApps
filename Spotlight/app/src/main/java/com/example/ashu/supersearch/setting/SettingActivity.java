package com.example.ashu.supersearch.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashu.supersearch.BuildConfig;
import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private SharedPreferences sharedPreferences;


    public static final String CHANNEL_ID = "420";

    public static final String MY_SETTING_PREF = "setting_pref";

    @BindView(R.id.searchAnyWhereSwitch)
     Switch searchAnyWhereSwitch;
    @BindView(R.id.searchBarPositionTv)
    TextView searchBarPositionTv;

//    @BindView(R.id.faq)
//    TextView faqTv;

    @BindView(R.id.versionCode)
    TextView versionCode;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        versionCode.setText(BuildConfig.VERSION_NAME);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(MY_SETTING_PREF,MODE_PRIVATE);
        String switchValue = sharedPreferences.getString("background_switch",null);
        if (switchValue != null && switchValue.equals("true")) {
            searchAnyWhereSwitch.setChecked(true);
        }

        if (R.id.topBar == sharedPreferences.getInt("radio_id",0)){
            searchBarPositionTv.setText(R.string.top);
        }




    }

    @OnClick(R.id.searchAnyWhereSwitch)
    public void switchCheck(){
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_SETTING_PREF,MODE_PRIVATE).edit();
            if (searchAnyWhereSwitch.isChecked() ){
                editor.putString("background_switch", String.valueOf(searchAnyWhereSwitch.isChecked()));
                drawOverAppPermission();
            } else {
                editor.putString("background_switch", String.valueOf(searchAnyWhereSwitch.isChecked()));
                stopService(new Intent(SettingActivity.this,MyWidgetService.class));
            }
            editor.apply();

    }

    private void drawOverAppPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getBaseContext())) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();

        }

    }
    private void initializeView() {
                startService(new Intent(SettingActivity.this, MyWidgetService.class));
//                finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    initializeView();
                } else { //Permission is not available
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_SETTING_PREF,MODE_PRIVATE).edit();
                    editor.putString("background_switch", "false");
                    editor.apply();

                    Toast.makeText(this,
                            "Draw over other app permission not available. Closing the application",
                            Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.settingAssistView)
     public void setAssistApp(){
        startActivityForResult(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS), 0);
    }

    @OnClick(R.id.searchBarPosition)
    public void searchBarPosition(){
        final RadioButton topBar;
        final RadioButton bottomBar;
        final RadioGroup radioGroup;

//        View view = LayoutInflater.from(this).inflate(R.layout.layout_selection,null);
        View view = View.inflate(this,R.layout.layout_selection,null);
       final AlertDialog builder =  new AlertDialog.Builder(this)
                .setTitle(R.string.search_bar_position)
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        radioGroup = view.findViewById(R.id.radioLayoutGroup);
        topBar = view.findViewById(R.id.topBar);
        bottomBar = view.findViewById(R.id.bottomBar);
       sharedPreferences = getSharedPreferences(MY_SETTING_PREF,MODE_PRIVATE);
       int radioId = sharedPreferences.getInt("radio_id",0);
       if (radioId == R.id.topBar){
           topBar.setChecked(true);
       } else {
           bottomBar.setChecked(true);
       }


        topBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBarPositionTv.setText(R.string.top);
                setRadioButton(radioGroup.getCheckedRadioButtonId());
                builder.dismiss();

            }
        });
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBarPositionTv.setText(R.string.bottom);
                setRadioButton(radioGroup.getCheckedRadioButtonId());
                builder.dismiss();
            }
        });

        }

        private void setRadioButton(int id){
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_SETTING_PREF, MODE_PRIVATE).edit();
            editor.putInt("radio_id",id);
            editor.apply();
        }



    @Override
    protected void onResume() {
        super.onResume();
        int radioId = sharedPreferences.getInt("radio_Id",0);
        if (radioId == R.id.topBar){
            searchBarPositionTv.setText(R.string.top);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private AlertDialog builder;

    @OnClick(R.id.faq)
    public void showFaqDialog(){
        TextView textView;
//        View view = LayoutInflater.from(this).inflate(R.layout.faq_dialog_layout,null);
        View view = View.inflate(this,R.layout.faq_dialog_layout,null);
        builder = new AlertDialog.Builder(this)
                .setTitle(R.string.faq)
                .setView(view)
                .show();
        textView = view.findViewById(R.id.spanClickText);

        String solidExplorer = "Solid Explorer";
        String esExplorer = "ES File Explorer";

        String fullText = getResources().getString(R.string.ans3);


        int startingIndex = fullText.indexOf(solidExplorer);
        int endingIndex = startingIndex + solidExplorer.length();

        int eStartingIndex = fullText.indexOf(esExplorer);
        int eEndingIndex = eStartingIndex + esExplorer.length();
        Log.e("Setting", "showFaqDialog: " + eStartingIndex + " " + eEndingIndex);

        SpannableStringBuilder  str = new SpannableStringBuilder(fullText);

        textView.setHighlightColor(Color.TRANSPARENT);
        str.setSpan(new MyClickableSpan(0),startingIndex,endingIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new MyClickableSpan(1),eStartingIndex,eEndingIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(str,TextView.BufferType.SPANNABLE);

    }

    @SuppressWarnings("WeakerAccess")
    class MyClickableSpan extends ClickableSpan{
        final int position;
        public MyClickableSpan(int position){
            this.position = position;
        }

        @Override
        public void onClick(@NonNull View widget) {
            builder.dismiss();
                if (position == 0){
                    openPlayStorePage(getResources().getString(R.string.solidExplorer));
                } else {
                    openPlayStorePage(getResources().getString(R.string.esExplorer));
                }
        }
    }

    private void openPlayStorePage(String packageName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName );
        intent.setData(uri);
        startActivity(intent);
    }


}



