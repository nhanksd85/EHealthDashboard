package npnlab.smart.algriculture.kiosskdashboard;


import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface Api {

    @POST(Contacts.getDate)
    Call<SystemTimeDate> getSystemTime(@QueryMap Map<String, String> part);
}