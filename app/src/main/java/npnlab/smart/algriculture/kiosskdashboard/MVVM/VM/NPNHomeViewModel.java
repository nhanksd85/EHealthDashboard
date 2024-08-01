package npnlab.smart.algriculture.kiosskdashboard.MVVM.VM;

import npnlab.smart.algriculture.kiosskdashboard.MVVM.View.NPNHomeView;
import npnlab.smart.algriculture.kiosskdashboard.Network.ApiResponseListener;

public class NPNHomeViewModel extends BaseViewModel<NPNHomeView>{
  public void requestURL(String url){
    requestGETWithURL(url, new ApiResponseListener<String>() {
      @Override
      public void onSuccess(String response) {
        view.onResponse(response);
      }

      @Override
      public void onError(String error) {
        view.responseError(error);
      }
    });
  }
}
