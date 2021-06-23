package INF1D.eclipse.settings.widgethandler.widget;

import INF1D.eclipse.R;
import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridAdapter;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;

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

        if (listPreference != null) {
            setListPreferenceData(listPreference);
            if(!currentTileData.type.equals("empty")) {
                listPreference.setValue(currentTileData.type);
                setParamsField(availableWidgets.get(String.valueOf(currentTileData.type).toLowerCase()));
            } else listPreference.setValueIndex(0);

            listPreference.setOnPreferenceChangeListener(this::onPrefChangeListener);
        }

        final Preference removeButton = findPreference("remove");
        if(removeButton != null) {
            removeButton.setOnPreferenceClickListener(preference -> {
                removeCurrentWidget();
                requireActivity().onBackPressed();
                return true;
            });
        }
    }

    private boolean onPrefChangeListener(Preference preference, Object newValue) {
        DataProvider.TileData selectedWidget = availableWidgets.get(String.valueOf(newValue).toLowerCase());

        if(selectedWidget != null) {
                DataProvider.TileData newWidget = new DataProvider.TileData(clickedPositionIndex, selectedWidget.type);
                DraggableGridAdapter.MyViewHolder selectedViewHolder = DraggableGridAdapter.getItemHolders().get(clickedPositionIndex);

                if(selectedViewHolder != null) {
                    int selectedDrawable = selectedWidget.parseIcon();
                    selectedViewHolder.mIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), selectedDrawable, null));
                }

                if(selectedWidget.hasParams()) {
                    setParamsField(selectedWidget);
                } else {
                    DraggableGridAdapter.getDataProvider().replaceItem(clickedPositionIndex, selectedWidget);
                }

            }

            return true;
    }

    private void setParamsField(DataProvider.TileData selectedWidget) {
        final PreferenceCategory preferenceCategory = findPreference("paramsCat");
        if(preferenceCategory != null) {
            preferenceCategory.removeAll();
            if (selectedWidget.hasParams()) {
                try {
                    JSONArray paramsArray = new JSONArray(selectedWidget.getParams());
                    Preference pref = new Preference(Objects.requireNonNull(getContext()));
                    pref.setTitle(paramsArray.getString(0));
                    if(currentTileData.hasParams()) {
                        JSONArray jsonArray = new JSONArray(currentTileData.params);
                        pref.setOnPreferenceClickListener(paramOnclick(selectedWidget, jsonArray.getString(2)));
                    }

                    preferenceCategory.addPreference(pref);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Preference.OnPreferenceClickListener paramOnclick(DataProvider.TileData selectedWidget, String cityName) {
        return preference -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                EditText input = new EditText(getContext());

                if(cityName != null) {
                    input.setText(cityName);
                }

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setMessage("Enter a city");
                builder.setPositiveButton("Save", (dialog, which) -> { });
                builder.setNegativeButton("Cancel", ((dialog, which) -> {
                    removeCurrentWidget();
                    requireActivity().onBackPressed();
                }));
                builder.setView(input);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();

                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String weatherEndpoint = "http://eclipse.serverict.nl/api/weather?city=" + input.getText();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherEndpoint, null, response -> {
                        try {
                            response.getInt("cityID");
                            JSONArray jsonArray = new JSONArray()
                                                    .put(response.getInt("cityID"))
                                                    .put(input.getText());
                            dialog.dismiss();
                            Toast.makeText(getContext(), "City saved!", Toast.LENGTH_SHORT).show();
                            selectedWidget.setParams(String.valueOf(jsonArray));
                            DraggableGridAdapter.getDataProvider().replaceItem(clickedPositionIndex, selectedWidget);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Toast.makeText(getContext(), "This city is not valid, try again!", Toast.LENGTH_SHORT).show();
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization", "Bearer " + prefManager.getUserToken());
                            return headers;
                        }
                    };

                    Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(jsonObjectRequest);
                });
            return true;
        };
    }

    private void removeCurrentWidget() {
        DraggableGridAdapter.MyViewHolder selectedViewHolder = DraggableGridAdapter.getItemHolders().get(clickedPositionIndex);
        if (selectedViewHolder != null) {
            selectedViewHolder.mIcon.setImageDrawable(null);
        }
        DataProvider.mData.put(clickedPositionIndex, new DataProvider.TileData("empty"));
    }

    private void setListPreferenceData(ListPreference listPreference) {
        CharSequence[] entryValues = availableWidgets.values().stream().map(tileData -> tileData.type).toArray(CharSequence[]::new);
        CharSequence[] newEntry = availableWidgets.values().stream().map(tileData -> Character.toUpperCase(tileData.displayName.charAt(0)) + tileData.displayName.substring(1)).toArray(CharSequence[]::new);
        availableWidgets.forEach((key, value) -> System.out.println(key + "\t" + value.type));
        listPreference.setEntries(newEntry);
        listPreference.setEntryValues(entryValues);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) { }
}
