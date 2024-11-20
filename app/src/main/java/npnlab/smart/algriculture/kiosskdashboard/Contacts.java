package npnlab.smart.algriculture.kiosskdashboard;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * 所有接口URL
 *
 *
 */
public class Contacts {
    //获取越南时间
    public static final String getDate = "/app/version/getSysDate";

    /**
     * 获取服务器返回的越南时间
     *
     * @return
     */
    public static Map GetDate(){
        Map<String,Object> map = new HashMap<>();
        map.put("mac",getEthMac());
        map.put("sn",getSN());
        return map;
    }
    /**
     * 获取 SN 号
     * @return
     */
    public static String getSN() {
        @SuppressLint({"NewApi", "LocalSuppress", "MissingPermission"}) String serial = Build.getSerial();
        if (serial.length() < 16) {
            return null;
        }
        if (serial.indexOf("NOT") != -1) {
            return null;
        }
        return serial;
    }

    /**
     * 获取有线的MAc
     * @return MAC
     */
    public static String getEthMac() {
        // TODO Auto-generated method stub
        String mac_eth = "";
        String submac_eth = "";
        try {
            String[] args = { "/system/bin/cat", "/sys/class/net/eth0/address" };
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream macInputStream = process.getInputStream();
            byte[] arrayOfByte = new byte[1024];
            while (macInputStream.read(arrayOfByte) != -1) {
                mac_eth = mac_eth + new String(arrayOfByte);
                submac_eth = mac_eth.substring(0, 17);
                submac_eth = submac_eth.replace(":", "");
            }
            macInputStream.close();
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        //return "22584a00f3f5";
        return submac_eth;
    }
}
