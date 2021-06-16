package INF1D.eclipse.settings.widgethandler.widget;

import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.widgethandler.WidgetHandlerActivity;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridAdapter;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class WidgetSettings extends PreferenceFragmentCompat {
    private HashMap<String, DataProvider.TileData> availableWidgets;
    private DataProvider.TileData currentTileData;
    private Mirror selectedMirror;
    private widgetSettingsActivity activity;
    private int clickedPositionIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.widget_prefs);

        if(getActivity() != null) {
            activity = ((widgetSettingsActivity) getActivity());

            selectedMirror = activity.getMirror();
            currentTileData = activity.getSelectedTileData();
            availableWidgets = activity.getWidgetList();
            clickedPositionIndex = activity.getCurrentPosition();

            Toast.makeText(getContext(), "WIDGETS LOADED FROM API " + availableWidgets.size(), Toast.LENGTH_SHORT).show();
        }

        final ListPreference listPreference = findPreference("widget");
        if (listPreference != null) {
            setListPreferenceData(listPreference);
            listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                DataProvider.TileData selectedWidget = availableWidgets.get(newValue.toString().toLowerCase());
                if(selectedWidget != null) {

                    DataProvider.TileData newWidget = new DataProvider.TileData(clickedPositionIndex, selectedWidget.type);
                    DraggableGridAdapter.getDataProvider().replaceItem(clickedPositionIndex, newWidget);
                    DraggableGridAdapter.MyViewHolder selectedViewHolder = DraggableGridAdapter.getItemHolders().get(clickedPositionIndex);

                    if(selectedViewHolder != null) {
                        int selectedDrawable = selectedWidget.parseIcon();
                        selectedViewHolder.mIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), selectedDrawable, null));
                    }

                    if(selectedWidget.hasParams()) {
                        System.out.println("THIS WIDGET REQUIRES PARAMETERS, MAKE A NEW PREFERENCE");
                    }
                }
               return true;
            });
        }

//        final PreferenceCategory preferenceCategory = findPreference("prefCat");
        final Preference removeButton = findPreference("remove");
                if(removeButton != null) {
                    removeButton.setOnPreferenceClickListener(preference -> {
                        Objects.requireNonNull(getActivity()).onBackPressed();
                        return true;
                    });
                }
    }

    @Override
    public void onStop() {
        try {
            selectedMirror.sendJSONToMirror(parseJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    private JSONObject parseJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject().put("type", "new_widget");

        DraggableGridAdapter.getDataProvider().getmData().forEach(w -> {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(w.type);
            try { jsonObject.put(String.valueOf(w.getId()), jsonArray); }
             catch (JSONException e) {
                e.printStackTrace();
            }
        });
        //TODO SENT TO MIRROR

        return jsonObject;
    }

    private void setListPreferenceData(ListPreference listPreference) {
        CharSequence[] newEntry = availableWidgets.values().stream().map(tileData -> Character.toUpperCase(tileData.type.charAt(0)) + tileData.type.substring(1)).toArray(CharSequence[]::new);
        availableWidgets.forEach((key, value) -> System.out.println(key));
        listPreference.setEntries(newEntry);
        listPreference.setEntryValues(newEntry);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) { }
}
