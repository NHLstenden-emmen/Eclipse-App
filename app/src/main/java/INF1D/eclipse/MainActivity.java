package INF1D.eclipse;

import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.discovery.adapter.DiscoveryAdapter;
import INF1D.eclipse.databinding.*;
import INF1D.eclipse.setup.SetupActivity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private NsdManager nsdManager;
    private final List<Mirror> mirrors = new ArrayList<>();
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
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(80);
        serviceInfo.setServiceName("test_mirror");
        try {
            serviceInfo.setHost(InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mirrors.add(new Mirror(serviceInfo));
        recyclerView.setAdapter(new DiscoveryAdapter(mirrors));

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scan(getApplicationContext());
            }
        }, 0, 5000);
    }

    public void scan(Context packageContext) {
        nsdManager = (NsdManager) packageContext.getSystemService(Context.NSD_SERVICE);
        new Thread(() -> nsdManager.discoverServices("_eclipse-mirror._tcp", NsdManager.PROTOCOL_DNS_SD, new Discovery())).start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, UserSettings.class));
        return true;
    }

    private void startSetup(View view) {
        startActivity(new Intent(this, SetupActivity.class));
    }

    class Discovery implements NsdManager.DiscoveryListener {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {}

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {}

        @Override
        public void onDiscoveryStarted(String serviceType) {}

        @Override
        public void onDiscoveryStopped(String serviceType) {}

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            System.out.println("found " + serviceInfo.getServiceName());
            nsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    System.out.println("Failed to resolve (" + errorCode + "): " + serviceInfo.getServiceName());
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
                }
            });
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
    }
}
