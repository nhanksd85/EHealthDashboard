package npnlab.smart.algriculture.kiosskdashboard;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONObject;

public class NPNChannelModel {
    public String mName = "";
    public String mLogo = "";
    public String mPackage = "";
    public String mBackground = "";
    public String mFocus = "";
    public String mHexaCode = "";

    public boolean isSelected = false;

    public int mDrawableLogo = 0;
    public int mDrawableBkg = 0;
    public int mDrawableFocus = 0;
    public Drawable drawableLogo;

    public NPNChannelModel() {

    }


    public NPNChannelModel(String name, int bkg, int focus, String pckage, int logo, String hexaCode) {
        mName = name;
        mDrawableLogo = logo;
        mPackage = pckage;
        mDrawableBkg = bkg;
        mDrawableFocus = focus;
        mHexaCode = hexaCode;
    }

    public NPNChannelModel(String name, String bkg, String focus, String pckage, String logo) {
        mName = name;
        mLogo = logo;
        mPackage = pckage;
        mBackground = bkg;
        mFocus = focus;
    }

    public NPNChannelModel(String name, int bkg, int focus, String pckage, int logo) {
        mName = name;
        mDrawableLogo = logo;
        mPackage = pckage;
        mDrawableBkg = bkg;
        mDrawableFocus = focus;
    }

    public static NPNChannelModel newModelFromJSONObj(JSONObject object) {
        NPNChannelModel model = new NPNChannelModel();
        try {
            model.mName = object.getString("name");
            model.mLogo = object.getString("logo");
            model.mPackage = object.getString("package");
            model.mBackground = object.getString("background");
            model.mFocus = object.getString("focus");
            return model;
        } catch (Exception e) {
            Log.d("", e.getMessage());
            return null;
        }

    }

}
