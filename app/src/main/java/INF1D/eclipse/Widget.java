package INF1D.eclipse;

import org.json.JSONObject;

public class Widget {
    private String widgetName;
    private String param;

    public Widget(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
