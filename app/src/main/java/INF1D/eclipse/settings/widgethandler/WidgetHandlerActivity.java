package INF1D.eclipse.settings.widgethandler;

import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGridFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class WidgetHandlerActivity extends AppCompatActivity {
    private Mirror selectedMirror;
    private final String userSettingsEndpoint = "http://eclipse.serverict.nl/api/user_settings/{user_id}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widgethandler);
        selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");
/*
        try {
            getUserSettingsFromAPI();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

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
        getUserSettings.join();

        dialog.dismiss();
    }

    private Thread getUserSettingsThread(AlertDialog dialog) {
        return new Thread(() -> {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userSettingsEndpoint, null, response -> {
                parseUserSettings(response);
                runOnUiThread(dialog::dismiss);
            }, Throwable::printStackTrace);

            queue.add(userRequest);
        });
    }

    private void parseUserSettings(JSONObject response) {

    }

    @Override
    protected void onStop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();
        new Thread(() -> {
            /* POST settings to API */
        }).start();
        super.onStop();
    }

    public DataProvider newInstance(Mirror selected) {
        DataProvider fragment = new DataProvider();
        Bundle args = new Bundle();
        args.putSerializable("selectedMirror", selected);
       // args.putSerializable("userSettings", new HashMap<>());

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
