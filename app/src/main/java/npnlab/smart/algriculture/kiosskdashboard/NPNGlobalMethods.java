package npnlab.smart.algriculture.kiosskdashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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
import java.util.List;
import java.util.regex.Pattern;

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

            File root = new File("/storage/sdcard0/");
            File gpxfile = new File(root, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static String readFromInternalFile(String fileName) {
        String id = "";
        File file = new File("/storage/sdcard0/", fileName);
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


}
