package INF1D.eclipse;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.databinding.ActivityTempUserSettingsBinding;
import INF1D.eclipse.setup.fragments.setup_1.loginFragment;

public class UserSettings extends AppCompatActivity {
    private boolean isRunning;

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

    public void simpleLogin(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.HORIZONTAL); //1 is for vertical orientation
        final EditText input = new EditText(this);
        final EditText input1 = new EditText(this);
        input.setWidth(500);
        input1.setWidth(500);
        lila1.addView(input);
        lila1.addView(input1);
        alert.setView(lila1);

        alert.setTitle("Login");

        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            String UsernameInput = input.getText().toString().trim();
            String passwordInput = input1.getText().toString().trim();

            if (UsernameInput.length() > 0 && passwordInput.length() > 0) {

                if(!isRunning) {
                    new Thread(() -> {
                        try {
                            isRunning = true;
                            loginRequest(loginCallback(), UsernameInput, passwordInput);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "No credentials entered!", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alert.create();

        dialog.show();
    }


    public void loginRequest(final loginFragment.VolleyCallback callback, String username, String password) throws JSONException {
        JSONObject postPayload = new JSONObject().put("email", username).put("password", password);

        String loginEndpointURL = "http://eclipse.serverict.nl/api/login";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginEndpointURL, postPayload, callback::onSuccess, callback::onError);
        Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext())).add(jsonObjectRequest);
    }

    private loginFragment.VolleyCallback loginCallback() {
        return new loginFragment.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                isRunning = false;
                try {
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    prefManager.setUserToken(result.getString("token"));
                    prefManager.setUserID(result.getJSONObject("user").getInt("id"));
                    Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                isRunning = false;
                Toast.makeText(getApplicationContext(), "Login failed, credentials not correct", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onError(VolleyError error);
    }
}
