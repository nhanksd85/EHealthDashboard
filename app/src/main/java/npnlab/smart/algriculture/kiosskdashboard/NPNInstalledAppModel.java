package npnlab.smart.algriculture.kiosskdashboard;

import android.graphics.drawable.Drawable;

public class NPNInstalledAppModel {
    public String appName = "";
    public String packageName = "";
    public String versionName = "";
    public int versionCode = -1;
    public Drawable appIcon = null;

    public NPNInstalledAppModel(String appName , String packageName, String versionName, int versionCode, Drawable appIcon) {
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.appIcon     = appIcon;
        this.appName     = appName;
    }
}
