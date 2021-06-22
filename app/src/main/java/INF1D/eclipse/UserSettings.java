package INF1D.eclipse;

import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.databinding.ActivityTempUserSettingsBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserSettings extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("User Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActivityTempUserSettingsBinding binding = ActivityTempUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PrefManager prefManager1 = new PrefManager(getApplicationContext());
        if(prefManager1.isUserLoggedIn()) {
            binding.LogoutButton.setOnClickListener(v -> {
                String loginEndpointURL = "http://eclipse.serverict.nl/api/logout";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginEndpointURL, null, response -> {
                    try {
                        if(response.getString("message").equals("logged out")) {
                            prefManager1.setUserToken(null);
                            prefManager1.setUserID(-1);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace)
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + prefManager1.getUserToken());
                        return headers;
                    }};

                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
            });
        } else {
            binding.LogoutButton.setClickable(false);
            binding.LogoutButton.setTextColor(Color.parseColor("#6a7398"));
        }
    }
}
