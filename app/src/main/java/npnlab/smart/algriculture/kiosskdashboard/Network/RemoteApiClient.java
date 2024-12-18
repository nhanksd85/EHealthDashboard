package npnlab.smart.algriculture.kiosskdashboard.Network;

import java.io.File;
import java.util.Map;

public interface RemoteApiClient {
  void get(String requestUrl, ApiResponseListener<String> apiResponseListener);

  void get(String requestUrl, Map<String, String> header, ApiResponseListener<String> apiResponseListener);

  void post(String requestUrl, String params, ApiResponseListener<String> apiResponseListener);

  void multipartRequest(String requestUrl, File file, ApiResponseListener<String> apiResponseListener);

  void put(String requestUrl, ApiResponseListener<String> apiResponseListener);

  void put(String requestUrl, String params, ApiResponseListener<String> apiResponseListener);

  void post(String requestUrl, ApiResponseListener<String> apiResponseListener);

  void delete(String requestUrl, ApiResponseListener<String> apiResponseListener);

  void post(String requestUrl, String params, Map<String, String> header, ApiResponseListener<String> apiResponseListener);

}
