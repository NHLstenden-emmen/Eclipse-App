package INF1D.eclipse.setup;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.adapter.SetupAdapter;
import INF1D.eclipse.setup.fragments.setup_2.mirrorselectFragment;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static android.Manifest.permission.*;

public class SetupActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    private ScanResult chosenMirror;
    private ScanResult chosenNetwork;
    private mirrorselectFragment fragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup);

        if(!checkPermission()) requestPermission();

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SetupAdapter(this));
        viewPager.setUserInputEnabled(false);
    }

    public void setChosenMirror(ScanResult chosenMirror, mirrorselectFragment fragment) {
        this.fragment = fragment;
        this.chosenMirror = chosenMirror;
    }

    public void setChosenNetwork(ScanResult chosenNetwork) {
        this.chosenNetwork = chosenNetwork;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CHANGE_WIFI_STATE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_WIFI_STATE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{INTERNET, ACCESS_FINE_LOCATION, CHANGE_WIFI_STATE, ACCESS_WIFI_STATE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {

                boolean INTERNET_ACCEPTED = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean ACCESS_FINE_LOCATION_ACCEPTED = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean CHANGE_WIFI_STATE_ACCEPTED = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean ACCESS_WIFI_STATE_ACCEPTED = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                if (!INTERNET_ACCEPTED || !ACCESS_FINE_LOCATION_ACCEPTED || !CHANGE_WIFI_STATE_ACCEPTED || !ACCESS_WIFI_STATE_ACCEPTED)
                {
                    if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                        new AlertDialog.Builder(this)
                                .setMessage("You need to allow access to both the permissions")
                                .setPositiveButton("OK", (dialog, which) -> requestPermissions(new String[]{INTERNET, ACCESS_FINE_LOCATION, CHANGE_WIFI_STATE, ACCESS_WIFI_STATE}, 1))
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                }
            }
        }
    }

    private int getNextItem() {
        return viewPager.getCurrentItem() + 1;
    }
    private int getPrevItem() { return viewPager.getCurrentItem() - 1; }

    public void nextButton() {
        int current = getNextItem();
        if (current < Objects.requireNonNull(viewPager.getAdapter()).getItemCount() -1) {
            viewPager.setCurrentItem(current);
        } else {
            finish();
        }
    }

    public void prevButton() {
        int current = getPrevItem();
        if (current < Objects.requireNonNull(viewPager.getAdapter()).getItemCount() -1) {
            viewPager.setCurrentItem(current);
        } else {
            finish();
        }
    }

    public void executeSetup(String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();

        if(chosenMirror != null && chosenNetwork != null) {
            new Thread(() -> {
                runOnUiThread(() -> {
                    dialog.show();
                    WifiUtils.withContext(getApplicationContext())
                        .connectWith(chosenNetwork.SSID, password)
                        .setTimeout(40000)
                        .onConnectionResult(new ConnectionSuccessListener() {
                            @Override
                            public void success() {
                                Toast.makeText(getApplicationContext(), "CONNECTED TO HOTSPOT", Toast.LENGTH_SHORT).show();
                                fragment.cancelSearch();
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                WifiUtils.withContext(getApplicationContext())
                                        .connectWith(chosenMirror.SSID, "pizzakaas")
                                        .setTimeout(40000)
                                        .onConnectionResult(new ConnectionSuccessListener() {
                                            @Override
                                            public void success() {
                                                try {
                                                    Thread.sleep(5000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.4.1/wifi", response -> {
                                                    Log.i("VOLLEY", response);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        if(jsonObject.get("status").equals("ok_request")) {
                                                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                            nextButton();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }, error -> Log.e("VOLLEY", error.toString())) {
                                                    @Override
                                                    public String getBodyContentType() {
                                                        return "application/json; charset=utf-8";
                                                    }

                                                    @Override
                                                    public byte[] getBody() {
                                                        JSONObject jsonBody = new JSONObject();
                                                        try {
                                                            jsonBody.put("ssid", chosenNetwork.SSID);
                                                            jsonBody.put("password", password);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                                                    }
                                                };

                                                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                                            }
                                            @Override
                                            public void failed(@NonNull ConnectionErrorCode errorCode) {
                                                Toast.makeText(getApplicationContext(), "EPIC FAIL!" + errorCode, Toast.LENGTH_SHORT).show();
                                            }
                                        }).start();
                            }
                            @Override
                            public void failed(@NonNull ConnectionErrorCode errorCode) {
                                Toast.makeText(getApplicationContext(), "NO COMPLETE setup!", Toast.LENGTH_SHORT).show();
                            }
                        }).start();
                });
            }).start();
        }
    }
}
