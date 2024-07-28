package npnlab.smart.algriculture.kiosskdashboard;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;


public class NPNConstants {
    public static final String apiHeaderKey = "User-Agent";
    public static final String apiHeaderValue = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36 NPNLab";

    //public static final String mainUrl = "http://174.138.24.109/kodi.php?name=launcher/MSTAR_40/";
    //public static final String mainUrl ="http://ubc.dauthutruyenhinh.com:2080/UBC_Launcher/2019_ALL/TIVI_TOPTECH_338/SANCO/";
    public static final String mainUrl ="http://npnlab.cdn.vccloud.vn/UBC_Launcher/2019_ALL/TIVI_TOPTECH_338/SANCO_B/";
    public static final String rootStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";//"/storage/sdcard0/";
    public static final String apkFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ubc_download.apk";//"/storage/sdcard0/ubc_download.apk";
    public static final String apkStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";//"/storage/sdcard0/Android/data/";
    public static final String folderData = "launcher_data/";
    public static final String agencyLogoUrl = "http://174.138.24.109/agency.php?pass=%s&agency=%s";

    public static final String prefKey = "npn.npnlauncher.pref";
    public static final String pref_ImageAgency = "PrefImageAgency";
    public static final String pref_FirstShowBarcode = "PrefFirstShowBarcode";

    public static final String backdoorPasscode = "069"; // ^_^


    //public static String MegaLauncherRepo = "http://ubc.dauthutruyenhinh.com:2080/MegaLauncherRepo/TOPTECH/SANCO_CHUNHAT/version.txt";


    public static List<NPNChannelModel> getOfflineChannelModels() {

        List<NPNChannelModel> list = new ArrayList<>();

        list.add(new NPNChannelModel("TIVI DVB-T2", R.drawable.open10d, R.drawable.open10d, "com.mstar.tv.tvplayer.ui*28", R.drawable.mega_tvplus, "#009600"));

        list.add(new NPNChannelModel("STV PLAY", R.drawable.open10d, R.drawable.open10d, "com.gviet.stv", R.drawable.stv, "#000096"));

        list.add(new NPNChannelModel("YOUTUBE", R.drawable.open10d, R.drawable.open10d, "com.google.android.youtube.tv", R.drawable.mega_youtube,"#e30613"));
        list.add(new NPNChannelModel("KARAOKE", R.drawable.open10d, R.drawable.open10d, "vn.ubc.ubctube", R.drawable.mega_ubctube_2, "#ffee00"));

        list.add(new NPNChannelModel("VTV GO", R.drawable.open10d, R.drawable.open10d, "vn.vtv.vtvgotv", R.drawable.mega_vtvgo, "#ce1100"));

        list.add(new NPNChannelModel("CỬA HÀNG", R.drawable.open10d, R.drawable.open10d, "vn.ubc.ubcstore", R.drawable.mega_ubcstore,"#009600"));

        return list;

    }



    public static List<NPNChannelModel> getSettingApps() {

        List<NPNChannelModel> list = new ArrayList<>();


        list.add(new NPNChannelModel("CÀI ĐẶT", R.drawable.open10d, R.drawable.open10d, "com.android.settings", R.drawable.gear,"#9fa09f"));

        list.add(new NPNChannelModel("ỨNG DỤNG", R.drawable.open10d, R.drawable.open10d, "com.entertainment.npnlab.npntivi.NPNAppManager", R.drawable.mega_allapp,"#b14021"));

        list.add(new NPNChannelModel("SHARE ANDROID", 0, 0, "com.toptech.mitv.wfd", R.drawable.mega_share_android,"#f2740b"));
        list.add(new NPNChannelModel("SHARE iOS", 0, 0, "com.waxrain.airplaydmr", R.drawable.airpinpro));

        list.add(new NPNChannelModel("THƯ MỤC", R.drawable.open10d, R.drawable.open10d, "com.jrm.localmm", R.drawable.mega_folder,"#505050"));
        list.add(new NPNChannelModel("TỐI ƯU", R.drawable.open10d, R.drawable.open10d, "com.linkin.memory.clear", R.drawable.mega_booter,"#007eff"));

        list.add(new NPNChannelModel("YOUTUBE", R.drawable.open10d, R.drawable.open10d, "com.google.android.youtube.tv", R.drawable.mega_youtube,"#e30613"));
        list.add(new NPNChannelModel("KARAOKE", R.drawable.open10d, R.drawable.open10d, "vn.ubc.ubctube", R.drawable.mega_ubctube_2, "#ffee00"));
        list.add(new NPNChannelModel("CỬA HÀNG", R.drawable.open10d, R.drawable.open10d, "vn.ubc.ubcstore", R.drawable.mega_ubcstore,"#009600"));
        list.add(new NPNChannelModel("VTV GO", R.drawable.open10d, R.drawable.open10d, "vn.vtv.vtvgotv", R.drawable.mega_vtvgo, "#ce1100"));
        return list;

    }


    public static List<NPNDockItemModel> getOfflineDockModels() {

        List<NPNDockItemModel> list = new ArrayList<>();
        list.add(new NPNDockItemModel("THƯ MỤC", 0, 1, "com.cvte.filebrowser.activity"));
        list.add(new NPNDockItemModel("TỐI ƯU", 0, 0, "com.yunos.tv.defensor"));
        list.add(new NPNDockItemModel("TRANG CHỦ", 0, 0, ""));
        //list.add(new NPNDockItemModel("ỨNG DỤNG", R.drawable.bot_bt_04, fc_bot_04, "com.vsoontech.mos.apps"));
        list.add(new NPNDockItemModel("ỨNG DỤNG", 0, 0, "com.entertainment.npnlab.npntivi.NPNAppManager"));
        list.add(new NPNDockItemModel("CÀI ĐẶT", 0, 0, "com.cvte.tv.androidsetting"));

        return list;

    }

    //public static String TVPlusRepo = "http://ubc.dauthutruyenhinh.com:2080/TVPlusRepo/TIVI/MSTAR/version.txt";
    //public static String UBCTubeRepo = "http://ubc.dauthutruyenhinh.com:2080/UBCTubeRepo/TIVI/MSTAR/version.txt";


    public static String KSD_KEY = "959b44cd6e6108eb3559b3f16baa833a";
    public static String lon= "";
    public static String lat="";

    public static String[] restrictedPackage = {
            "com.google.android.youtube",
            "npn.launcher",
            "npn.npnlauncher",
            "com.entertainment.npnlab.npntivi.NPNAppManager",
            "com.google.android.voicesearch"
    };

    public static String[] voice_channels = {
            "Hồ Chí Minh",
            "sctv 123456789",
            "vtv cab cáp 1234567890",
            "VTV 123456789",
            "HTV 123456789",
            "VTC 123456789",
            "Vĩnh Long 12",
            "An Giang",
            "Vũng Tàu",
            "Bắc Giang",
            "Bắc Kạn",
            "Bạc Liêu",
            "Bắc Ninh",
            "Bến Tre",
            "Bình Định",
            "Bình Dương",
            "Bình Phước",
            "Bình Thuận",
            "Cà Mau",
            "Cao Bằng",
            "Đắc Lắc",
            "Đắc Nông",
            "Điện Biên",
            "Đồng Nai",
            "Đồng Tháp",
            "Gia Lai",
            "Hà Giang",
            "Hà Nam",
            "Hà Tĩnh",
            "Hải Dương",
            "Hậu Giang",
            "Hòa Bình",
            "Hưng Yên",
            "Khánh Hòa",
            "Kiên Giang",
            "Kon Tum",
            "Lai Châu",
            "Lâm Đồng",
            "Lạng Sơn",
            "Lào Cai",
            "Long An",
            "Nam Định",
            "Nghệ An",
            "Ninh Bình",
            "Ninh Thuận",
            "Phú Thọ",
            "Quảng Bình",
            "Quảng Nam",
            "Quảng Ngãi",
            "Quảng Ninh",
            "Quảng Trị",
            "Sóc Trăng",
            "Sơn La",
            "Tây Ninh",
            "Thái Bình",
            "Thái Nguyên",
            "Thanh Hóa",
            "Huế",
            "Tiền Giang",
            "Trà Vinh",
            "Tuyên Quang",
            "Vĩnh Long",
            "Vĩnh Phúc",
            "Yên Bái",
            "Phú Yên",
            "Cần Thơ",
            "Đà Nẵng",
            "Hải Phòng",
            "Hà Nội"
    };

    public static String[] VietnameseSigns = new String[]{
            "aAeEoOuUiIdDyY",
            "áàạảãâấầậẩẫăắằặẳẵ",
            "ÁÀẠẢÃÂẤẦẬẨẪĂẮẰẶẲẴ",
            "éèẹẻẽêếềệểễ",
            "ÉÈẸẺẼÊẾỀỆỂỄ",
            "óòọỏõôốồộổỗơớờợởỡ",
            "ÓÒỌỎÕÔỐỒỘỔỖƠỚỜỢỞỠ",
            "úùụủũưứừựửữ",
            "ÚÙỤỦŨƯỨỪỰỬỮ",
            "íìịỉĩ",
            "ÍÌỊỈĨ",
            "đ",
            "Đ",
            "ýỳỵỷỹ",
            "ÝỲỴỶỸ"
    };

}
