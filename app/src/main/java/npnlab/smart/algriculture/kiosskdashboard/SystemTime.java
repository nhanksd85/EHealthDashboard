package npnlab.smart.algriculture.kiosskdashboard;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.time.Clock;
import java.util.Calendar;
import java.util.Map;


import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class SystemTime {

    public static SystemTime systemtime;
    public static final String baseUrl = "http://2023.zhongtian2020.com/";


    private FullscreenActivity launcher;
    public SystemTime(FullscreenActivity launcher) {
        this.launcher = launcher;
    }

    public static SystemTime getInstance(FullscreenActivity launcher){
        if(systemtime == null){
            systemtime = new SystemTime(launcher);
        }
        return systemtime;
    }

    public void initSystemTime (){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Map systemTimeMap = Contacts.GetDate();
        Call<SystemTimeDate> responseCall = api.getSystemTime(systemTimeMap);
        responseCall.enqueue(new Callback<SystemTimeDate>() {
            @Override
            public void onResponse(Call<SystemTimeDate> call, Response<SystemTimeDate> response) {
                if(null==response.body() || response.body().getDate()==null || response.body().getDate().equals("")){
                    return;
                }
                if(response.body().getReturn_code().equalsIgnoreCase("success")){
                    String time = response.body().getDate();
                    SetTime(time);
                }
            }

            @Override
            public void onFailure(Call<SystemTimeDate> call, Throwable t) {
            }
        });
    }

    private void SetTime(String time){
        Calendar calendar = Calendar.getInstance();
        String date = time.split(" ")[0];
        String hms = time.split(" ")[1];
        calendar.set(Calendar.YEAR,Integer.parseInt(date.split("-")[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(date.split("-")[1]));
        calendar.set(Calendar.DATE,Integer.parseInt(date.split("-")[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hms.split(":")[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(hms.split(":")[2]));

        long selectedTime = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) launcher.getSystemService(Context.ALARM_SERVICE);
        // 设置RTC_WAKEUP触发器，将系统时间设置为指定时间
        alarmManager.setTime(selectedTime);

        // 发送广播通知系统时间已经更改
        Intent intent = new Intent(Intent.ACTION_TIME_CHANGED);
        intent.setFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        launcher.sendBroadcast(intent);
    }

}
