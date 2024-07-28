package npnlab.smart.algriculture.kiosskdashboard;



import org.json.JSONObject;

public class NPNDockItemModel {
    public String sName = "";
    public String sLogo = "";
    public String sFocus = "";
    public String sPackage = "";

    public  int drawableLogo = 0;
    public  int drawableFocus = 0;

    public NPNDockItemModel() {

    }

    public NPNDockItemModel(String name, String logo, String focus, String pckage) {
        sName = name;
        sLogo = logo;
        sFocus = focus;
        sPackage = pckage;
    }

    public NPNDockItemModel(String name, int logo, int focus, String pckage) {
        sName = name;
        drawableLogo = logo;
        drawableFocus = focus;
        sPackage = pckage;
    }

    public static NPNDockItemModel newModelFromJSONObj(JSONObject object) {
        NPNDockItemModel model = new NPNDockItemModel();
        try {
            model.sName = object.getString("name");
            model.sLogo = object.getString("logo");
            model.sPackage = object.getString("package");
            model.sFocus = object.getString("focus");
            return model;
        } catch (Exception e) {

            return null;
        }

    }

}
