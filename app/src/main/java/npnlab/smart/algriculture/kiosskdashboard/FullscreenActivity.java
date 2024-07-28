package npnlab.smart.algriculture.kiosskdashboard;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import npnlab.smart.algriculture.kiosskdashboard.databinding.ActivityFullscreenBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        //WebView webView = findViewById(R.id.webContent);

        // loading url in the WebView.
        //webView.loadUrl("https://ar-hospital.vercel.app/");
        //webView.loadUrl("https://esnz-reactweather.netlify.app/");
        //webView.loadUrl("https://weather.aniqa.dev/");
        // this will enable the javascript.
        //webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        //webView.setWebViewClient(new WebViewClient());
        horizontalView = findViewById(R.id.horizontal_list);
        listInstalledApps = getInstalledAppList();
        setupHorizontalList();
    }
    public List<NPNInstalledAppModel> getInstalledAppList() {

        PackageManager packageManager  = peekAvailableContext().getPackageManager();
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
        if (mVisible) {
            hide();
        } else {
            show();
        }
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

        int angle = 90;


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

        for(int i=0; i<listInstalledApps.size(); ++i) {
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
//            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, itemW);
//            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
//            valueAnimator.setDuration(200);
//            valueAnimator.addUpdateListener(animation -> {
//                int progress = Math.round((float)animation.getAnimatedValue());
//
//                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(itemW + progress, itemH);
//                params2.setMargins(left  + (int)(20.0f / tanValue) * 2,0,2,0);
//                horizontalView.getChildAt(idx).setLayoutParams(params2);
//
//            });

            Drawable logo;
            if (model.drawableLogo != null) {
                logo = model.drawableLogo;
            } else {
                logo = ContextCompat.getDrawable(this, model.mDrawableLogo);
            }

            ParaItem item = new ParaItem(this, bkgColor, angle, logo, model.mName, (v, b) -> {
                if(b) {
                    //valueAnimator.start();
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams((int)(itemW * 1.5), itemH +  40);
                    params2.setMargins(left + (int)(20.0f / tanValue) * 2 ,10,2,10);
                    horizontalView.getChildAt(idx).setLayoutParams(params2);
                } else {
                    //valueAnimator.cancel();
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(itemW, itemH);
                    params2.setMargins(left ,30,2,0);
                    horizontalView.getChildAt(idx).setLayoutParams(params2);
                }
            });




            horizontalView.addView(item, i, params);

            horizontalView.getChildAt(0).requestFocus();
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

}