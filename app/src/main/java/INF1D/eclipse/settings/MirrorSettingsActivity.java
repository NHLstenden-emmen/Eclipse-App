package INF1D.eclipse.settings;

import INF1D.eclipse.R;
import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.common.MirrorSocket;
import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MirrorSettingsActivity extends AppCompatActivity {
    private final String userSettingsEndpoint = "http://eclipse.serverict.nl/api/user_settings/search/";
    private HashMap<Integer, DataProvider.TileData> userSettingsAPI = new HashMap<>();
    private PrefManager prefManager;
    private AlertDialog dialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Mirror Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefManager = new PrefManager(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
        dialog.show();

        try {
            getUserSettingsFromAPI(savedInstanceState);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void getUserSettingsFromAPI(Bundle savedInstanceState) throws InterruptedException {
        Thread getUserSettings = getUserSettingsThread(savedInstanceState);
        getUserSettings.start();
        getUserSettings.join();
    }

    private Thread getUserSettingsThread(Bundle savedInstanceState) {
        return new Thread(() -> {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userSettingsEndpoint + prefManager.getUserID(), null, response -> {
                try {
                    parseUserSettings(savedInstanceState, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                try {
                    parseUserSettings(savedInstanceState, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer " + prefManager.getUserToken());

                    return params;
                }
            };

            queue.add(userRequest);
        });
    }

    private void parseUserSettings(Bundle savedInstanceState, JSONObject response) throws JSONException {
        if(response != null) {
            Iterator<String> keys = response.keys();
            while(keys.hasNext()) {
                String dynamicKey = keys.next();
                String type = response.getJSONArray(dynamicKey).getString(0);
                JSONArray test = response.getJSONArray(dynamicKey);
                if(response.getJSONArray(dynamicKey).length() > 1) {
                    userSettingsAPI.put(Integer.parseInt(dynamicKey), new DataProvider.TileData(Integer.parseInt(dynamicKey), type, String.valueOf(response.getJSONArray(dynamicKey))));
                } else {
                    userSettingsAPI.put(Integer.parseInt(dynamicKey), new DataProvider.TileData(Integer.parseInt(dynamicKey), type));
                }
            }
        } else {
            userSettingsAPI = new HashMap<>();
        }

        if (savedInstanceState == null){
            Mirror selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, newInstance(selectedMirror))
                    .commit();

            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        MirrorSocket.closeWebSocket();
        super.onDestroy();
    }

    public MirrorSettingsFragment newInstance(Mirror selected) {
        MirrorSettingsFragment fragment = new MirrorSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedMirror", selected);
        args.putSerializable("userSettings", userSettingsAPI);
        fragment.setArguments(args);

        return fragment;
    }
}
