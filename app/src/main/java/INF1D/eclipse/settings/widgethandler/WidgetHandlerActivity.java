package INF1D.eclipse.settings.widgethandler;

import INF1D.eclipse.R;
import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.common.MirrorSocket;
import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridFragment;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WidgetHandlerActivity extends AppCompatActivity {
    private Mirror selectedMirror;
    private PrefManager prefManager;
    private HashMap<Integer, DataProvider.TileData> userSettingsAPI;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Widget Placement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_widgethandler);

        selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");
        userSettingsAPI = (HashMap<Integer, DataProvider.TileData>) getIntent().getSerializableExtra("userSettings");
        selectedMirror.openWebSocket();
        prefManager = new PrefManager(getApplicationContext());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(newInstance(selectedMirror), "data provider")
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.widgetContainer, new DraggableGridFragment(), "list view")
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject postPayload = new JSONObject();

        try {
            postPayload.put("id", prefManager.getUserID());
            postPayload.put("widget_settings",   MirrorSocket.parseTileDataToJSON().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.POST, "http://eclipse.serverict.nl/api/user_settings/update", postPayload, response -> runOnUiThread(dialog::dismiss), Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + prefManager.getUserToken());

                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(userRequest);
        super.onDestroy();
    }

    public DataProvider newInstance(Mirror selected) {
        DataProvider fragment = new DataProvider();
        Bundle args = new Bundle();

        args.putSerializable("selectedMirror", selected);
        args.putSerializable("userSettings", userSettingsAPI);
        fragment.setArguments(args);

        return fragment;
    }

    public Fragment getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag("data provider");
        if (fragment != null) {
            return ((DataProvider) fragment).getDataProvider();
        }
        return null;
    }
}
