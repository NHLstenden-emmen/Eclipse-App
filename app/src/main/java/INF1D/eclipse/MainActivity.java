package INF1D.eclipse;

import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.discovery.adapter.DiscoveryAdapter;
import INF1D.eclipse.databinding.*;
import INF1D.eclipse.setup.SetupActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private NsdManager nsdManager;
    private final List<Mirror> mirrors = new ArrayList<>();
    private final List<NsdServiceInfo> resolveQueue = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Eclipse);

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButton.setOnClickListener(this::startSetup);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            mirrors.add(new Mirror());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(new DiscoveryAdapter(mirrors));

    }

    public void scan(Context packageContext) {
        nsdManager = (NsdManager) packageContext.getSystemService(Context.NSD_SERVICE);
        new Thread(() -> {
            nsdManager.discoverServices("_eclipse-mirror._tcp", NsdManager.PROTOCOL_DNS_SD, new NsdManager.DiscoveryListener() {
                @Override
                public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                    System.out.println("Start discovery failed");
                }

                @Override
                public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                    System.out.println("Stop discovery failed");
                }

                @Override
                public void onDiscoveryStarted(String serviceType) {
                    System.out.println("Discovery started");
                }

                @Override
                public void onDiscoveryStopped(String serviceType) {
                    System.out.println("Discovery stopped");
                }

                @Override
                public void onServiceFound(NsdServiceInfo serviceInfo) {
                    System.out.println("found " + serviceInfo.getServiceName());
                    resolveQueue.add(serviceInfo);
                }

                @Override
                public void onServiceLost(NsdServiceInfo serviceInfo) {
                    System.out.println("lost " + serviceInfo.getServiceName());
                    /*
                    Optional<Mirror> optMirror= mirrors.stream()
                            .filter(mirror -> mirror.getIP().equals(serviceInfo.getServiceName()))
                            .findAny();
                     */
                 //   optMirror.ifPresent(Mirror::setOffline);
                }
            });
        }).start();
       parseResolveQueue();
    }

    private void parseResolveQueue() {
        new Thread(() -> {
            final boolean[] finished = {true};
            while(true) {
                if (!resolveQueue.isEmpty() && finished[0]) {

                    finished[0] = false;
                    NsdServiceInfo curServiceInfo = resolveQueue.get(0);
                    nsdManager.resolveService(curServiceInfo, new NsdManager.ResolveListener() {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                            System.out.println("Failed to resolve (" + errorCode + "): " + serviceInfo.getServiceName());
                            finished[0] = true;
                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {

                            Optional<Mirror> optMirror = mirrors.stream()
                                    .filter(mirror -> mirror.getName().equals(serviceInfo.getServiceName()))
                                    .findAny();

                            if (!optMirror.isPresent()) {
                                Mirror mirror = new Mirror(serviceInfo);
                                mirrors.add(mirror);
                            }

                            runOnUiThread(() -> recyclerView.setAdapter(new DiscoveryAdapter(mirrors)));

                            finished[0] = true;
                        }
                    });
                    resolveQueue.remove(curServiceInfo);
                    System.out.println(resolveQueue.size());
                }
            }
        }).start();
    }

    private void startSetup(View view) {
        startActivity(new Intent(this, SetupActivity.class));
    }
}
