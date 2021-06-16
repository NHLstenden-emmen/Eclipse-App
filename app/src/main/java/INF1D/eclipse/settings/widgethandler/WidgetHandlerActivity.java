package INF1D.eclipse.settings.widgethandler;

import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridAdapter;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridFragment;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import java.util.stream.Stream;

public class WidgetHandlerActivity extends AppCompatActivity {
    private Mirror selectedMirror;
    private final String userSettingsEndpoint = "http://eclipse.serverict.nl/api/user_settings/1";
    private HashMap<Integer, JSONArray> userSettingsAPI = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widgethandler);
        selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");

        try {
            getUserSettingsFromAPI();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(newInstance((Mirror) getIntent().getSerializableExtra("selectedMirror")), "data provider")
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.widgetContainer, new DraggableGridFragment(), "list view")
                    .commit();
        }
    }

    private void getUserSettingsFromAPI() throws InterruptedException {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();
        dialog.show();

        Thread getUserSettings = getUserSettingsThread(dialog);
        getUserSettings.start();
        getUserSettings.join();

        dialog.dismiss();
    }

    private Thread getUserSettingsThread(AlertDialog dialog) {
        return new Thread(() -> {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userSettingsEndpoint, null, response -> {
                try {
                    parseUserSettings(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(dialog::dismiss);
            }, Throwable::printStackTrace);

            queue.add(userRequest);
        });
    }

    private void parseUserSettings(JSONObject response) throws JSONException {
        String rawJSON = response.getString("widget_settings");

        if(!rawJSON.equals("null")) {
            Toast.makeText(getApplicationContext(), "hoi", Toast.LENGTH_SHORT).show();
            System.out.println(rawJSON);
        } else {
            userSettingsAPI = null;
        }
    }

    @Override
    protected void onStop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();

        new Thread(() -> {
            JSONObject jsonObject = new JSONObject();

            DraggableGridAdapter.getDataProvider().getmData().forEach(w -> {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(w.type);
                try { jsonObject.put(String.valueOf(w.getId()), jsonArray); }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            /* POST settings to API */
                RequestQueue queue = Volley.newRequestQueue(this);
                JSONObject postPayload = new JSONObject();

                try {
                    postPayload.put("user_id", "1");
                    postPayload.put("widget_settings", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.POST, "http://eclipse.serverict.nl/api/user_settings", postPayload, response -> {
                    runOnUiThread(dialog::dismiss);
                }, Throwable::printStackTrace) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("content-type", "application/json");
                        params.put("accept", "application/json");
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };

                queue.add(userRequest);
        }).start();
        super.onStop();
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
