package npnlab.smart.algriculture.kiosskdashboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.core.content.ContextCompat;

public class NPNGlobalMethods {
    public static List<NPNChannelModel> getChannelModelFromLauncherData() {
        String uri = NPNConstants.rootStorageDir + NPNConstants.folderData + "main.txt";
        File file = new File(uri);
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(file);
                String ret = convertStreamToString(fin);
                fin.close();
                List<NPNChannelModel> models = new ArrayList<>();
                JSONArray obj = new JSONArray(ret);
                for (int i=0; i<obj.length(); ++i) {
                    models.add(NPNChannelModel.newModelFromJSONObj(obj.getJSONObject(i)));
                }
                return models;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static List<NPNDockItemModel> getDockItemFromLauncherData() {
        String uri = NPNConstants.rootStorageDir + NPNConstants.folderData + "bottom.txt";
        File file = new File(uri);
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(file);
                String ret = convertStreamToString(fin);
                fin.close();
                List<NPNDockItemModel> models = new ArrayList<>();
                JSONArray obj = new JSONArray(ret);
                for (int i=0; i<obj.length(); ++i) {
                    models.add(NPNDockItemModel.newModelFromJSONObj(obj.getJSONObject(i)));
                }
                return models;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static void updateUserSettingDataFile(int indexHomeScreen, String packageName) {
        String userSettingData = NPNGlobalMethods.readFromInternalFile("user_setting.txt");

        String[] splitUserSettingData = userSettingData.split(Pattern.quote("#"));

        if (userSettingData.equals("-1") || userSettingData.equals("")) {
            splitUserSettingData = null;
        }

        //create a list
        List<NPNUserSettingItem> listUserSetting = new ArrayList<>();
        int i;
        for( i = 0; splitUserSettingData != null && i< splitUserSettingData.length; i++) {
            String[] splitLines = splitUserSettingData[i].split(Pattern.quote("*"));
            NPNUserSettingItem objUserSetting = new NPNUserSettingItem(Integer.parseInt(splitLines[0]),splitLines[1]);
            listUserSetting.add(objUserSetting);
        }

        for( i = 0; i < listUserSetting.size(); i++) {
            if(listUserSetting.get(i).index == indexHomeScreen) {
                listUserSetting.get(i).packageName = packageName;
                break;
            }
        }
        if(i == listUserSetting.size())
        {
            NPNUserSettingItem objUserSetting = new NPNUserSettingItem(indexHomeScreen,packageName);

            listUserSetting.add(objUserSetting);
        }
        //save to user setting data
        userSettingData = "";
        for( i =0; i < listUserSetting.size(); i++)
        {
            userSettingData += Integer.toString(listUserSetting.get(i).index) + "*" + listUserSetting.get(i).packageName;
            if(i != listUserSetting.size() - 1) {
                userSettingData += "#";
            }

        }

        NPNGlobalMethods.writeToInternalFile("user_setting.txt", userSettingData);
    }

    public static void writeToInternalFile(String fileName, String data)
    {
        try {

            File root = new File(NPNConstants.rootStorageDir);
            File gpxfile = new File(root, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static boolean checkWifiConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static boolean checkLanConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

        if(mWifi == null) return false;

        try {
            if (mWifi.isConnected()) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    public static int getWifiLevel(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        return level;
    }

    public static String readFromInternalFile(String fileName) {
        String id = "";
        File file = new File(NPNConstants.rootStorageDir, fileName);
        if (file.exists())   // check if file exist
        {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    id += line;
                }
            } catch (IOException e) {
                //You'll need to add proper error handling here
                id = "0";
            }

        } else {
            id = "-1";
        }
        return id;
    }




    public static List<NPNUserSettingItem> getUserSettingData() {

        String userSettingData = NPNGlobalMethods.readFromInternalFile("user_setting.txt");

        String[] splitUserSettingData = userSettingData.split(Pattern.quote("#"));

        if (userSettingData.equals("-1") || userSettingData.equals("") ) {
            splitUserSettingData = null;
        }

        //create a list
        List<NPNUserSettingItem> listUserSetting = new ArrayList<>();
        int i = 0;

        for( i = 0; splitUserSettingData != null && i< splitUserSettingData.length; i++) {
            String[] splitLines = splitUserSettingData[i].split(Pattern.quote("*"));
            NPNUserSettingItem objUserSetting = new NPNUserSettingItem(Integer.parseInt(splitLines[0]),splitLines[1]);
            listUserSetting.add(objUserSetting);
        }

        return listUserSetting;
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 5, 5, true);
        final int color = newBitmap.getPixel(2, 2);
        newBitmap.recycle();
        return color;
    }

    public static int getDominantColor(Context context, Drawable drawable) {
        return getDominantColor(drawableToBitmap(drawable));
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static void showBarCodeDialog(Context context, Bitmap image, String displaySerial
            , @Nullable NPNDialogBarcodeHandler handler) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        //dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        dialog.setContentView(R.layout.layout_barcode_dialog);

        //dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_dialog_title);
        //TextView title = dialog.findViewById(R.id.text_title);
        //title.setText("THIẾT LẬP ĐẠI LÝ");
        //title.setTextColor(ContextCompat.getColor(context, R.color.white));

        ImageView imgBarCode = dialog.findViewById(R.id.img_barcode);
        imgBarCode.setImageBitmap(image);
        EditText pass = dialog.findViewById(R.id.edt_pass);
        EditText agency = dialog.findViewById(R.id.edt_affiliate);

        agency.setShowSoftInputOnFocus(false);
        pass.setShowSoftInputOnFocus(false);
        pass.requestFocus();

        TextView textSerial = dialog.findViewById(R.id.text_serial);
        textSerial.setText( "IMEI: " + displaySerial);
        Button btnOK = dialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(view -> {
            dialog.cancel();
            if (handler!=null) {
                handler.onOkButtonClicked(pass.getText().toString(), agency.getText().toString());
                //((MainActivity)context).goFullscreen();
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btn_close);
        btnCancel.setOnClickListener(view -> {
            dialog.cancel();
            //((MainActivity)context).goFullscreen();
        });

        Point size = NPNGlobalMethods.getScreenSize((Activity) context);

        dialog.setOnCancelListener(dialogInterface -> {
            //((MainActivity)context).goFullscreen();
        });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bkg);
        dialog.getWindow().setLayout((int)((float)size.x * 0.3), (int) ((float)size.y * 0.8));
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public static Point getScreenSize(Activity activity) {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return screenSize;
    }
    public static Bitmap getBarcode(String data) {
        // barcode image
        Bitmap bitmap = null;
        try {

            bitmap = encodeAsBitmap(data, BarcodeFormat.QR_CODE, 600, 300);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}
