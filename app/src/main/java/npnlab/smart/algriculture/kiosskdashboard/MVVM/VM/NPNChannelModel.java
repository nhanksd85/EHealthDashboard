package npnlab.smart.algriculture.kiosskdashboard.MVVM.VM;

import android.util.Log;
import org.json.JSONObject;

public class NPNChannelModel {
  public String name = "";
  public String id = "";
  public String logo = "";
  public String backup = "";
  public String remote = "";
  public boolean isSelected = false;

  public static NPNChannelModel newModelFromJSONObj(JSONObject object) {
    NPNChannelModel model = new NPNChannelModel();
    try {
      model.name = object.getString("name");
      model.id = object.getString("ID");
      model.logo = object.getString("logo");
      model.backup = object.getString("backup");
      model.remote = object.getString("remote");
      return model;
    } catch (Exception e) {
      Log.d("", e.getMessage());
      return null;
    }

  }

}
