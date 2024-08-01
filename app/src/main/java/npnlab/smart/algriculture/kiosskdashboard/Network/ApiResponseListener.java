package npnlab.smart.algriculture.kiosskdashboard.Network;

public interface ApiResponseListener<T> {
  void onSuccess(T response);

  void onError(String error);
}
