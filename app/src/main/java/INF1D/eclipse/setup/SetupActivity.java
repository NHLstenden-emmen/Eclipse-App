package INF1D.eclipse.setup;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.adapter.SetupAdapter;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static android.Manifest.permission.*;

public class SetupActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<ScanResult> foundMirrorHotspots = new ArrayList<>();
    private List<ScanResult> foundWiFiHotspots = new ArrayList<>();

    private ScanResult chosenMirror;
    private ScanResult chosenNetwork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        if(!checkPermission()) requestPermission();
        /*
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            WifiUtils.withContext(getApplicationContext()).scanWifi(this::getScanResults).start();
        }, 0, 10, TimeUnit.SECONDS);
*/
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SetupAdapter(this));
        viewPager.setUserInputEnabled(false);
    }

    public void setChosenMirror(ScanResult chosenMirror) {
        this.chosenMirror = chosenMirror;
    }

    public ScanResult getChosenMirror() {
        return chosenMirror;
    }

    public void setChosenNetwork(ScanResult chosenNetwork) {
        this.chosenNetwork = chosenNetwork;
    }

    public ScanResult getChosenNetwork() {
        return chosenNetwork;
    }

    private void emp() {
                   /* WifiUtils.withContext(context)
                    .connectWith(String.valueOf(holder.mirrorName.getText()), "pizzakaas")
                    .setTimeout(40000)
                    .onConnectionResult(onConnectionResult()).start();*/
    }

    public List<ScanResult> getFoundMirrorHotspots() {
        return foundMirrorHotspots;
    }

    public List<ScanResult> getFoundWiFiHotspots() {
        return foundWiFiHotspots;
    }

    private void getScanResults(@NonNull final List<ScanResult> scanResults) {
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), "New wifi search results are in", Toast.LENGTH_SHORT).show();
        });
        foundMirrorHotspots =  scanResults
                .stream()
           //     .filter(s -> !s.SSID.isEmpty() && s.SSID.startsWith("Eclipse-"))
                .collect(Collectors.toList());

        foundWiFiHotspots = scanResults
                .stream()
                .distinct()
                .filter(s -> !s.SSID.isEmpty())
                .collect(Collectors.toList());
    }

    public ConnectionSuccessListener onConnectionResult() {
        return new ConnectionSuccessListener() {
            @Override
            public void success() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.4.1/wifi", response -> {
                    Log.i("VOLLEY", response);
//                    fragment.getParentActivity().nextButton();
                }, error -> Log.e("VOLLEY", error.toString())) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("ssid", "Android Volley Demo");
                            jsonBody.put("password", "BNK");
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
        };
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
        if(chosenMirror != null && chosenNetwork != null) {

        }
    }
}
