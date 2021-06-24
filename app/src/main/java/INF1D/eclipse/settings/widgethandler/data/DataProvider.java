package INF1D.eclipse.settings.widgethandler.data;

import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.R;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DataProvider extends Fragment implements Serializable {
    private Mirror selectedMirror;
    private HashMap<Integer, TileData> userSettingsAPI = new HashMap<>();
    public static final HashMap<Integer, TileData> mData = new HashMap<>();

    private final HashMap<String, DataProvider.TileData> availableWidgets = new HashMap<>();
    private final String widgetsEndpoint = "http://eclipse.serverict.nl/api/widgets";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWidgetsFromAPI();
        mData.clear();
        if (getArguments() != null) {
            selectedMirror = (Mirror) getArguments().getSerializable("selectedMirror");
            userSettingsAPI = (HashMap<Integer, TileData>) getArguments().getSerializable("userSettings");

            if(userSettingsAPI.size() != 0) {
                userSettingsAPI.forEach(mData::put);
            } else defaultValues();

        }
    }

    private void defaultValues() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                final int id = mData.size();
                mData.put(id, new TileData(id, "empty"));
            }
        }
    }

    public HashMap<Integer, TileData> getmData() {
        return mData;
    }

    public int getCount() {
        return mData.size();
    }

    public TileData getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    public void swapItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) return;
        TileData fromPositionTile = mData.get(fromPosition);
        TileData toPositionTile = mData.get(toPosition);
        mData.put(fromPosition, toPositionTile);
        mData.put(toPosition, fromPositionTile);
      //  Collections.swap(mData, toPosition, fromPosition);
    }

    public static void emptyItem(int clickedPosition) {
        mData.replace(clickedPosition, new TileData("empty"));
    }

    public void replaceItem(int clickedPositionIndex, TileData selectedWidgetTile) {
        mData.replace(clickedPositionIndex, selectedWidgetTile);
    }

    public DataProvider getDataProvider() {
        return this;
    }

    private void parseJSONIntoList(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            ArrayList<JSONObject> json = new ArrayList<>();
            json.add(response.getJSONObject(i));
            for (JSONObject jsonObject : json) {
                if(!jsonObject.getString("params").equals("null")) {
                    availableWidgets.put(jsonObject.getString("type").toLowerCase(), new DataProvider.TileData(i, jsonObject.getString("type"), jsonObject.getString("display_name"), jsonObject.getString("params")));
                } else {
                    availableWidgets.put(jsonObject.getString("type").toLowerCase(), new DataProvider.TileData(0.0, jsonObject.getString("type"), jsonObject.getString("display_name")));
                }
            }
        }
    }

    private void getWidgetsFromAPI() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();

        new Thread(() -> {
            Objects.requireNonNull(getActivity()).runOnUiThread(dialog::show);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonArrayRequest widgetRequest = new JsonArrayRequest(Request.Method.GET, widgetsEndpoint, null, response -> {
                try {
                    parseJSONIntoList(response);
                    Objects.requireNonNull(getActivity()).runOnUiThread(dialog::dismiss);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            queue.add(widgetRequest);
        }).start();
    }

    public HashMap<String, TileData> getAvailableWidgets() {
        return availableWidgets;
    }

    public Mirror getSelectedMirror() {
        return this.selectedMirror;
    }

    public static final class TileData implements Serializable  {
        public String type;
        public String displayName;
        public String params = null;
        public int mId;
        public int icon;

        public TileData(int id, String type, String displayName, String params) {
            this.type = type;
            this.displayName = displayName;
            this.params = params;
            this.icon = parseIcon();
        }

        public TileData(int id, String type) {
            this.mId = id;
            this.type = type;
            this.icon = parseIcon();
        }

        public TileData(String type) {
            this.type = type;
            this.icon = parseIcon();
        }

        public TileData(int id, String type, String params) {
            this.mId = id;
            this.type = type;
            this.params = params;
            this.icon = parseIcon();
        }

        public TileData(double id, String type, String displayName) {
            this.type = type;
            this.displayName = displayName;
            this.icon = parseIcon();
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public boolean hasParams() {
            return params != null;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getParams()
        {
            return this.params;
        }

        public long getId() {
            return mId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int parseIcon() {
            switch(getType()) {
                case "meme_of_the_day":
                    return R.drawable.star;
                case "news":
                case "news_business":
                case "news_entertainment":
                case "news_general":
                case "news_health":
                case "news_science":
                case "news_sports":
                case "news_technology":
                    return R.drawable.news;
                case "joke_of_day":
                    return R.drawable.geek_jokes;
                case "cat_fact":
                    return R.drawable.cat_joke;
                case "weather":
                    return R.drawable.weather;
                default:
                    return android.R.color.transparent;
            }
        }
    }
}
