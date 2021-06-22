package INF1D.eclipse.settings.widgethandler.widget;

import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.common.MirrorSocket;
import INF1D.eclipse.R;
import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridAdapter;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.*;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WidgetSettings extends PreferenceFragmentCompat {
    private HashMap<String, DataProvider.TileData> availableWidgets;
    private DataProvider.TileData currentTileData;
    private Mirror selectedMirror;
    private widgetSettingsActivity activity;
    private int clickedPositionIndex;
    private PrefManager prefManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.widget_prefs);
        prefManager = new PrefManager(getContext());
        if(getActivity() != null) {
            activity                = ((widgetSettingsActivity) getActivity());

            selectedMirror          = activity.getMirror();
            currentTileData         = activity.getSelectedTileData();
            availableWidgets        = activity.getWidgetList();
            clickedPositionIndex    = activity.getCurrentPosition();
        }

        final ListPreference listPreference = findPreference("widget");
        final PreferenceCategory preferenceCategory = findPreference("prefCat");

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

                    if(preferenceCategory != null) {
                        if (selectedWidget.hasParams()) {
                            if(findPreference("City") != null) {
                                preferenceCategory.removePreference(findPreference("City"));
                            }
                            try {
                                JSONArray test = new JSONArray(selectedWidget.getParams());
                                EditTextPreference pref = new EditTextPreference(Objects.requireNonNull(getContext()));
                                String param = test.getString(0);

                                pref.setTitle(param);
                                pref.setDefaultValue(null);
                                pref.setSummary("Weatherwidget requires an selected City");
                                pref.setKey(param);
                                pref.setDialogTitle(param);
                                pref.setOnPreferenceChangeListener(this::paramSave);
                                preferenceCategory.addPreference(pref);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

               return true;
            });
        }

        final Preference removeButton = findPreference("remove");
        if(removeButton != null) {
            removeButton.setOnPreferenceClickListener(preference -> {
                DraggableGridAdapter.MyViewHolder selectedViewHolder = DraggableGridAdapter.getItemHolders().get(clickedPositionIndex);
                if (selectedViewHolder != null) {
                    selectedViewHolder.mIcon.setImageDrawable(null);
                }
                DataProvider.mData.put(clickedPositionIndex, new DataProvider.TileData("empty"));
                requireActivity().onBackPressed();
                return true;
            });
        }
    }

    private boolean paramSave(Preference preference, Object selectedCity) {
        String weatherEndpoint = "http://eclipse.serverict.nl/api/weather?city=" + selectedCity;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherEndpoint, null, response -> {
            try {
                JSONObject jsonObject = new JSONObject()
                                            .put("type", "weather_city")
                                            .put("city_id", response.getInt("cityID"));
                MirrorSocket.sendToMirror(String.valueOf(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(), "This city is not valid!", Toast.LENGTH_SHORT).show())
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + prefManager.getUserToken());
                return headers;
            }};

        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(jsonObjectRequest);
        return true;
    }

    @Override
    public void onStop() {
        selectedMirror.sendJSONToMirror(MirrorSocket.parseTileDataToJSON());
        super.onStop();
    }

    private void setListPreferenceData(ListPreference listPreference) {
        CharSequence[] newEntry = availableWidgets.values().stream().map(tileData -> Character.toUpperCase(tileData.displayName.charAt(0)) + tileData.displayName.substring(1)).toArray(CharSequence[]::new);
        availableWidgets.forEach((key, value) -> System.out.println(key));
        listPreference.setEntries(newEntry);
        listPreference.setEntryValues(newEntry);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) { }
}
