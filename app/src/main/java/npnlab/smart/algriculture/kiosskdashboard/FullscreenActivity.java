package npnlab.smart.algriculture.kiosskdashboard;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import androidx.core.content.ContextCompat;
import npnlab.smart.algriculture.kiosskdashboard.MVVM.VM.NPNHomeViewModel;
import npnlab.smart.algriculture.kiosskdashboard.MVVM.View.NPNHomeView;
import npnlab.smart.algriculture.kiosskdashboard.databinding.ActivityFullscreenBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements NPNHomeView {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityFullscreenBinding binding;
    TextView txtTemperature, txtHumidity, txtDescription;
    ImageView imgMainIcon;
    ImageView imgTopLogo;
    ImageButton btnAllApp, btnFolder, btnSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtTemperature = findViewById(R.id.txtTemperature);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtDescription = findViewById(R.id.txtDescription);
        imgMainIcon = findViewById(R.id.imgIconMain);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);

        imgWifiStatus = findViewById(R.id.imgWifiStatus);


        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.dummyButton.setOnTouchListener(mDelayHideTouchListener);

        // Find the WebView by its unique ID
//        WebView webView = findViewById(R.id.webContent);
//
//        webView.loadUrl("http://lpnserver.net:51091/page");
//
//        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        //webView.setWebViewClient(new WebViewClient());
        horizontalView = findViewById(R.id.horizontal_list);
        listInstalledApps = getInstalledAppList();
        setupHorizontalList();
        mViewModel = new NPNHomeViewModel();
        mViewModel.attach(this, this);


        setTimer(0, 1);
        setTimer(1, 2);
        usingCountDownTimer();
        imgTopLogo = findViewById(R.id.top_logo);
        if(NPNGlobalMethods.readFromInternalFile("logo.txt").equals("1")){
            imgTopLogo.setImageDrawable(getResources().getDrawable(R.drawable.android_tv_crop));
        }else{
            imgTopLogo.setImageDrawable(getResources().getDrawable(R.drawable.dcar_icon));
        }

        txtT3 = findViewById(R.id.T3);
        txtT4 = findViewById(R.id.T4);
        txtT5 = findViewById(R.id.T5);
        txtT6 = findViewById(R.id.T6);


        btnAllApp = findViewById(R.id.btnn_allapp);
        btnAllApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAppFromPackageName("com.entertainment.npnlab.npntivi.NPNAppManager");
            }
        });

        btnFolder = findViewById(R.id.btnn_folder);
        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAppFromPackageName("com.droidlogic.FileBrower");
            }
        });

        btnSetting = findViewById(R.id.btnn_setting);
        btnSetting.setOnClickListener(v -> {
            launchAppFromPackageName("com.android.tv.settings");
        });

    }


    public void stateChange(String action, String data) {
        Log.d("NPN", "Something changed here: " + action);
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    CountDownTimer countDownTimer;

    public void usingCountDownTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 10000) {

            // This is called after every 10 sec interval.
            public void onTick(long millisUntilFinished) {
                Log.d("ACLAB","Using count down timer");
                if(timerFlag[0] == 1) {
                    checkNetworkConnection();
                    setTimer(0, 30);
                }
                if(timerFlag[1] == 1){
                    requestWeatherData();
                    setTimer(1, 10 * 6);
                }
                timerRun();
            }

            public void onFinish() {
                start();
            }
        }.start();
    }
    public List<NPNInstalledAppModel> getInstalledAppList() {

        PackageManager packageManager  = this.peekAvailableContext().getPackageManager();
        List<ApplicationInfo> applist = packageManager.getInstalledApplications(0);
        List<NPNInstalledAppModel> mInstalledApps = new ArrayList<>();

        for (ApplicationInfo app : applist) {
            //checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                //installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {

                try {

                    // Get the application's resources
                    Resources res = packageManager.getResourcesForApplication(app);
                    // Get a copy of the configuration, and set it to the desired resolution
                    Configuration config = res.getConfiguration();
                    Configuration originalConfig = new Configuration(config);
                    config.densityDpi = DisplayMetrics.DENSITY_XHIGH;

                    // Update the configuration with the desired resolution
                    DisplayMetrics dm = res.getDisplayMetrics();
                    res.updateConfiguration(config, dm);

                    // Grab the app icon
                    Drawable appIcon = res.getDrawable(app.icon);

                    //Get the application name, displayed as a label
                    String appName = (String) packageManager.getApplicationLabel(app);

                    //Get packageInfo: packageName (com.android.youtube), version code and version name
                    PackageInfo packageInfo = packageManager.getPackageInfo(app.packageName, 0);
                    mInstalledApps.add(new NPNInstalledAppModel(appName, app.packageName,
                            packageInfo.versionName, packageInfo.versionCode, appIcon));

                    res.updateConfiguration(originalConfig, dm);
                }catch(Exception t){}

            }
        }

        return mInstalledApps;

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    private void toggle() {
//        if (mVisible) {
//            hide();
//
//        } else {
//            show();
//        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    LinearLayout horizontalView;
    List<NPNChannelModel> listChannels;
    List<NPNInstalledAppModel> listInstalledApps;

    void setupHorizontalList() {

        int angle = 80;

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        //int itemH = (int)((float)displayMetrics.heightPixels * 0.3f);
        //int itemW = (int)((float)itemH * 0.7f);

        int itemH = (int)((float)displayMetrics.heightPixels * 0.15f);
        int itemW = (int)(itemH * 1.5);

        float tanValue = (float)Math.tan(Math.toRadians(angle));
        int marginLeft = (int) ((float)itemH / tanValue);

        int shiftRight = (int)((float)itemH * 1.3f);

        int itemH_IMG = (int)((float)displayMetrics.heightPixels * 0.3f);
        int itemW_IMG = (int)((float)itemH * 1.3f);

//        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(itemW_IMG, itemH_IMG);
//        layout.setMargins(0,20, 0,0);
//        imgADs.setLayoutParams(layout);
//        imgADs.setVisibility(View.VISIBLE);


        LinearLayout.LayoutParams abc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        abc.setMargins(2,0,0,20);
        horizontalView.setLayoutParams(abc);

        initChannelItemsOffline(false);
        List<NPNChannelModel> list = listChannels;

        //listInstalledApps.size()
        for(int i=0; i<0; ++i) {
            NPNInstalledAppModel appModel = listInstalledApps.get(i);
            // Check if the installed app already be in the list or not
            boolean isExisted = false;
            for(int j=0; j < list.size(); ++j) {
                isExisted = list.get(j).mPackage.equals(appModel.packageName);
                if (isExisted) {
                    break;
                }
            }
            if(!isExisted)
            {
                for(int j=0; j < NPNConstants.restrictedPackage.length; ++j) {
                    isExisted =  appModel.packageName.indexOf(NPNConstants.restrictedPackage[j]) >=0;
                    if (isExisted) {
                        break;
                    }
                }
            }
            if (!isExisted) {
                NPNChannelModel model = new NPNChannelModel(appModel.appName, 0, 0, appModel.packageName, 0);
                model.drawableLogo = appModel.appIcon;
                list.add(model);
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemW, itemH);
        //params.setMargins(-marginLeft,20,2,0);

        params.setMargins(10,30,2,30);


        for (int i=0; i < list.size(); ++i) {
            NPNChannelModel model = list.get(i);
            final int idx = i;
            int left = idx == 0 ? 10  : 10;


            int bkgColor;
            if(model.mHexaCode.length() > 0){
                bkgColor = Color.parseColor(model.mHexaCode);
            }
            else if (model.mDrawableLogo != 0) {
                Drawable drawable = ContextCompat.getDrawable(this, model.mDrawableLogo);
                bkgColor = NPNGlobalMethods.getDominantColor(this, drawable);
            }
            else if (model.drawableLogo != null){
                bkgColor = NPNGlobalMethods.getDominantColor(this, model.drawableLogo);
            } else {
                bkgColor = Color.GRAY;
            }


            // Animation
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, itemW);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(animation -> {
                int progress = Math.round((float)animation.getAnimatedValue());


                //Animation HEIGHT + 20
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(itemW + progress, itemH + 10);
                params2.setMargins(left  + (int)(20.0f / tanValue) * 2,10,2,10);
                horizontalView.getChildAt(idx).setLayoutParams(params2);

            });

            Drawable logo;
            if (model.drawableLogo != null) {
                logo = model.drawableLogo;
            } else {
                logo = ContextCompat.getDrawable(this, model.mDrawableLogo);
            }

            ParaItem item = new ParaItem(this, bkgColor, angle, logo, model.mName, (v, b) -> {
                if(b) {
                    valueAnimator.start();
                    //This part can be removed
//                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams((int)(itemW * 1.5), itemH +  40);
//                    params2.setMargins(left + (int)(20.0f / tanValue) * 2 ,10,2,10);
//                    horizontalView.getChildAt(idx).setLayoutParams(params2);
                } else {
                    valueAnimator.cancel();
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(itemW, itemH);
                    params2.setMargins(left ,30,2,0);
                    horizontalView.getChildAt(idx).setLayoutParams(params2);
                }
            });

            item.setOnClickListener(v -> {
                launchAppFromPackageName(model.mPackage);
            });


            horizontalView.addView(item, i, params);

            horizontalView.getChildAt(0).requestFocus();
        }

    }

    String activeAdminPackage = "";
    public void launchAppFromPackageName(String packageName) {
        //Launch an application from package name
        activeAdminPackage = packageName;
        if(packageName.contains("NPNAppManager") ||
                packageName.contains("tv.settings") ||
                packageName.contains("droidlogic.FileBrower") ||
                packageName.contains("vn.ubc.ubcstore")) {

            String serial = "abcdcadfewfewafew";
            Bitmap data = NPNGlobalMethods.getBarcode("abcdcadfewfewafew");
            NPNGlobalMethods.showBarCodeDialog(this, data, serial, new NPNDialogBarcodeHandler() {
                @Override
                public void onOkButtonClicked(String pass, String agency) {
                    if(pass.contains("1147") || agency.contains("1147")){
                        Intent launchIntent = peekAvailableContext().getPackageManager().getLaunchIntentForPackage(packageName);
                        if (launchIntent != null) {
                            peekAvailableContext().startActivity(launchIntent);
                        }
                    }
                }
            });
        }else {
            Intent launchIntent = peekAvailableContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                peekAvailableContext().startActivity(launchIntent);
            }
        }

    }

    void initChannelItemsOffline(boolean isLauncherDataExist) {
        // Load from launcher data
        List<NPNChannelModel> list = isLauncherDataExist ?
                NPNGlobalMethods.getChannelModelFromLauncherData() :
                NPNConstants.getOfflineChannelModels();

        listChannels = list;

        //listInstalledApps = ((MainActivity) getActivity()).getInstalledAppList();
        boolean isUserSetting = false;

        File file = new File(NPNConstants.rootStorageDir + "user_setting.txt");
        if(file.exists() == true) isUserSetting = true;


        if(isUserSetting == true){

            PackageManager packageManager  =  this.peekAvailableContext().getPackageManager();
            // Load from user setting
            List<NPNUserSettingItem> items = NPNGlobalMethods.getUserSettingData();
            for (int i = 0; i < items.size(); ++i) {
                String pkg = items.get(i).packageName;
                int updateIdx = items.get(i).index;
                try {

                    ApplicationInfo app = packageManager.getApplicationInfo(pkg, 0);

                    // Get the application's resources
                    Resources res = packageManager.getResourcesForApplication(app);
                    // Get a copy of the configuration, and set it to the desired resolution
                    Configuration config = res.getConfiguration();
                    Configuration originalConfig = new Configuration(config);
                    config.densityDpi = DisplayMetrics.DENSITY_XHIGH;

                    // Update the configuration with the desired resolution
                    DisplayMetrics dm = res.getDisplayMetrics();
                    res.updateConfiguration(config, dm);

                    // Grab the app icon
                    Drawable appIcon = res.getDrawable(app.icon);

                    if(updateIdx < listChannels.size()) {
                        listChannels.get(updateIdx).mName = (String) packageManager.getApplicationLabel(app);
                        listChannels.get(updateIdx).mPackage = pkg;
                        listChannels.get(updateIdx).drawableLogo = appIcon;
                    }

                } catch (PackageManager.NameNotFoundException e) {

                }
            }
        }


    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String RemoveSign4VietnameseString(String str)
    {
        for (int i = 1; i < NPNConstants.VietnameseSigns.length; i++)
        {
            for (int j = 0; j < NPNConstants.VietnameseSigns[i].length(); j++)
                str = str.replace(NPNConstants.VietnameseSigns[i].charAt(j), NPNConstants.VietnameseSigns[0].charAt(i-1));
        }
        return str;
    }

    NPNHomeViewModel mViewModel;
    String currentCity = "Hồ Chí Minh";
    TextView txtCurrentDate;
    public void requestWeatherData(){
        String url = "https://api.openweathermap.org/data/2.5/weather?q=xxxxxx&appid=yyyyyy&lang=vi&units=metric";
        url = url.replaceAll("yyyyyy", NPNConstants.KSD_KEY);
        int random_city_index = getRandomNumber(0, NPNConstants.city_names.length - 1);
        //random_city_index = 0;

        currentCity = NPNConstants.city_names[random_city_index];

        String currentDate = new SimpleDateFormat("dd-MM", Locale.getDefault()).format(new Date());
        txtCurrentDate.setText(currentCity + " - " + currentDate.replaceAll(Pattern.quote("-"), " tháng "));

        url = url.replaceAll("xxxxxx", RemoveSign4VietnameseString(NPNConstants.city_names[random_city_index].toLowerCase()));
        mViewModel.requestURL(url);
        Log.d("ACLAB","Request Weather: " + url);
        //var iconurl = "http://openweathermap.org/img/w/" + iconcode + ".png";

    }

    @Override
    public void responseError(String message) {
        Log.d("ACLAB","Request Weather: " + message);
        currentCity = "Hồ Chí Minh";
        //requestWeatherData();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        countDownTimer.start();
    }


    public enum NETWORK_STATE{
        NO_CONNECT,LAN, WIFI_0, WIFI_1, WIFI_2, WIFI_3, WIFI_4, WIFI_5
    }
    NETWORK_STATE network_state = NETWORK_STATE.WIFI_5;
    ImageView imgWifiStatus;
    private void checkNetworkConnection(){
        if (NPNGlobalMethods.checkWifiConnected(this) == true) {
            switch (NPNGlobalMethods.getWifiLevel(this)) {
                case 1:
                    if(network_state != NETWORK_STATE.WIFI_0) {
                        imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_wifi_0));
                        network_state = NETWORK_STATE.WIFI_0;
                    }
                    break;
                case 2:
                    if(network_state != NETWORK_STATE.WIFI_1) {
                        imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_wifi_1));
                        network_state = NETWORK_STATE.WIFI_1;
                    }
                    break;
                case 3:
                    if(network_state != NETWORK_STATE.WIFI_2) {
                        imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_wifi_2));
                        network_state = NETWORK_STATE.WIFI_2;
                    }
                    break;
                case 4:
                    if(network_state != NETWORK_STATE.WIFI_3) {
                        imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_wifi_3));
                        network_state = NETWORK_STATE.WIFI_3;
                    }
                    break;
                default:
                    if(network_state != NETWORK_STATE.WIFI_4) {
                        imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_wifi_3));
                        network_state = NETWORK_STATE.WIFI_4;
                    }
                    break;
            }
        } else if (NPNGlobalMethods.checkLanConnected(this) == true) {
            if(network_state != NETWORK_STATE.LAN) {
                imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_lan));
                network_state = NETWORK_STATE.LAN;
            }

        } else {
            if(network_state != NETWORK_STATE.NO_CONNECT) {
                imgWifiStatus.setImageDrawable(getResources().getDrawable(R.drawable.icon_disconnect));
                network_state = NETWORK_STATE.NO_CONNECT;
            }
        }
    }



    @Override
    public void onResponse(String message) {
        Log.d("ACLAB","Request Weather: " + message);
        try{
            JSONObject currentWeather = new JSONObject(message);
            JSONObject currentDate = currentWeather.getJSONArray("weather").getJSONObject(0);

            String description = currentDate.getString("description");

            description = description.substring(0, 1).toUpperCase() + description.substring(1).toLowerCase();
            String icon = "ico" + currentDate.getString("icon");

            JSONObject currentMain = currentWeather.getJSONObject("main");


            txtDescription.setText(description);
            String[] splitTemp = currentMain.getString("feels_like").split(Pattern.quote("."));
            txtTemperature.setText(splitTemp[0]);
            txtHumidity.setText(currentMain.getString("humidity") +"%");


            int id = getResources().getIdentifier(icon, "drawable", peekAvailableContext().getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            imgMainIcon.setImageDrawable(drawable);

            String urlHistory = "https://api.openweathermap.org/data/2.5/forecast/daily?q=xxxxxx&appid=6721dd6cc81ac8c2460a5c1260aa064a&lang=vi&units=metric&cnt=4";
            urlHistory = urlHistory.replaceAll("xxxxxx", RemoveSign4VietnameseString(currentCity.toLowerCase()));
            mViewModel.requestHistory(urlHistory);

        }catch (Exception e){}
    }
    TextView txtTempMin, txtTempMax, txtT3, txtT4, txtT5, txtT6;
    ImageView imgForecast;
    @Override
    public void onResponseForecast(String message) {
        Log.d("ACLAB","Request Weather: " + message);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        try{
            JSONObject mainObject = new JSONObject(message);
            JSONArray arrJson = mainObject.getJSONArray("list");
            for(int i = 0; i < arrJson.length(); i++){
                switch(i){
                    case 0:
                        txtTempMin = findViewById(R.id.txtT3Min);
                        txtTempMax= findViewById(R.id.txtT3Max);
                        imgForecast = findViewById(R.id.imgT3);

                        break;
                    case 1:
                        txtTempMin = findViewById(R.id.txtT4Min);
                        txtTempMax= findViewById(R.id.txtT4Max);
                        imgForecast = findViewById(R.id.imgT4);

                        break;
                    case 2:
                        txtTempMin = findViewById(R.id.txtT5Min);
                        txtTempMax= findViewById(R.id.txtT5Max);
                        imgForecast = findViewById(R.id.imgT5);

                        break;
                    case 3:
                        txtTempMin = findViewById(R.id.txtT6Min);
                        txtTempMax= findViewById(R.id.txtT6Max);
                        imgForecast = findViewById(R.id.imgT6);

                        break;
                }
                String[] splitDay = arrJson.getJSONObject(i).getJSONObject("temp").getString("day").split(Pattern.quote("."));
                String[] splitNight = arrJson.getJSONObject(i).getJSONObject("temp").getString("night").split(Pattern.quote("."));

                String icon = "ico"  +  arrJson.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                int id = getResources().getIdentifier(icon, "drawable", peekAvailableContext().getPackageName());
                Drawable drawable = getResources().getDrawable(id);



                txtTempMin.setText( splitNight[0] + "°");
                txtTempMax.setText( splitDay[0] + "°");

                imgForecast.setImageDrawable(drawable);

            }

            switch (day) {
                case Calendar.SUNDAY:
                    txtT3.setText("T2");txtT4.setText("T3");txtT5.setText("T4");txtT6.setText("T5");
                    break;
                case Calendar.MONDAY:
                    txtT3.setText("T3");txtT4.setText("T4");txtT5.setText("T5");txtT6.setText("T6");
                    break;
                case Calendar.TUESDAY:
                    txtT3.setText("T4");txtT4.setText("T5");txtT5.setText("T6");txtT6.setText("T7");
                    break;
                case Calendar.WEDNESDAY:
                    txtT3.setText("T5");txtT4.setText("T6");txtT5.setText("T7");txtT6.setText("CN");
                    break;
                case Calendar.THURSDAY:
                    txtT3.setText("T6");txtT4.setText("T7");txtT5.setText("CN");txtT6.setText("T2");
                    break;
                case Calendar.FRIDAY:
                    txtT3.setText("T7");txtT4.setText("CN");txtT5.setText("T2");txtT6.setText("T3");
                    break;
                case Calendar.SATURDAY:
                    txtT3.setText("CN");txtT4.setText("T2");txtT5.setText("T3");txtT6.setText("T4");
                    break;
            }

        }catch (Exception e){
            Log.d("ACLAB", "There is an error");
        }

    }


    private int[] timerCounter = new int[10];
    private int[] timerFlag = new int[10];
    private void setTimer(int index, int counter){
        timerCounter[index] = counter;
        timerFlag[index] = 0;
    }
    private void timerRun(){
        for(int i = 0; i < 10; i++){
            if(timerCounter[i] > 0){
                timerCounter[i]--;
                if(timerCounter[i] <= 0) timerFlag[i] = 1;
            }
        }
    }

    int counterBackPress = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_1) {
            counterBackPress++;
            if (counterBackPress > 5) {
                counterBackPress = 0;
                generateID();
            }
        }
        return true;
    }

    private  void generateID()
    {
        if(NPNGlobalMethods.readFromInternalFile("logo.txt").equals("1")){
            NPNGlobalMethods.writeToInternalFile("logo.txt", "0");
            imgTopLogo.setImageDrawable(getResources().getDrawable(R.drawable.dcar_icon));
        }else{
            NPNGlobalMethods.writeToInternalFile("logo.txt", "1");
            imgTopLogo.setImageDrawable(getResources().getDrawable(R.drawable.android_tv_crop));
        }
        displayUpdateDialog();
    }

    public void displayUpdateDialog(){
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.bcd);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setPositiveButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setView(image);
        builder.create().show();
    }
}