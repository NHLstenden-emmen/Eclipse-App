package INF1D.eclipse.settings.widgethandler.data;


import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridAdapter;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridFragment;
import INF1D.eclipse.settings.widgethandler.widget.widgetSettingsActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class DataProvider extends Fragment implements Serializable {
    private Mirror selectedMirror;
    public final List<TileData> mData = new LinkedList<>();

    private final HashMap<String, DataProvider.TileData> availableWidgets = new HashMap<>();
    private final String widgetsEndpoint = "http://eclipse.serverict.nl/api/widgets";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWidgetsFromAPI();


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                final int id = mData.size();
                mData.add(id, new TileData(id, "empty"));
            }
        }

        if (getArguments() != null) {
            selectedMirror = (Mirror) getArguments().getSerializable("selectedMirror");
         //   mAdapter = ((DraggableGridFragment) getArguments().getSerializable("draggableGridFragment")).getmAdapter();
            //  findPreference("serialnmbr").setTitle(selectedMirror.serialNo);
            // findPreference("ip").setTitle(selectedMirror.IP.getHostAddress());
        }
    }

    public List<TileData> getmData() {
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

        Collections.swap(mData, toPosition, fromPosition);
    }

    public void replaceItem(int clickedPositionIndex, TileData selectedWidgetTile) {
        mData.remove(clickedPositionIndex);
        mData.add(clickedPositionIndex, selectedWidgetTile);

        System.out.println("SET " + mData.get(clickedPositionIndex).getType());
    }

    public DataProvider getDataProvider()
    {
        return this;
    }

    private void parseJSONIntoList(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            ArrayList<JSONObject> json = new ArrayList<>();
            json.add(response.getJSONObject(i));
            for (JSONObject jsonObject : json) {
                if(!jsonObject.getString("params").equals("null")) {
                    availableWidgets.put(jsonObject.getString("type").toLowerCase(), new DataProvider.TileData(jsonObject.getString("type"), new JSONArray(jsonObject.getString("params"))));
                } else {
                    availableWidgets.put(jsonObject.getString("type").toLowerCase(), new DataProvider.TileData(jsonObject.getString("type")));
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

    private void parseUserSettings(JSONArray response) {

    }


    public HashMap<String, TileData> getAvailableWidgets() {
        return availableWidgets;
    }

    public Mirror getSelectedMirror() {
        return this.selectedMirror;
    }

    public static final class TileData implements Serializable  {
        public String type;
        public JSONArray params = null;
        public int mId;
        public int icon;

        public TileData(String type, JSONArray params) {
            this.type = type;
            this.params = params;
            this.icon = parseIcon();
        }

        public TileData(String type) {
            this.type = type;
            this.icon = parseIcon();
        }

        public TileData(int id, String type) {
            this.mId = id;
            this.type = type;
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

        public void setParams(JSONArray params) {
            this.params = params;
        }

        public JSONArray getParams()
        {
            return this.params;
        }

        public long getId() {
            return mId;
        }

        @NonNull
        public String toString() {
            return null;
        }

        public String getText() {
            return null;
        }

        public int parseIcon() {
            switch(getType()) {
                default:
                    return R.drawable.weather;
            }
        }

        public int getIcon() {
            return this.icon;
        }

    }
}
