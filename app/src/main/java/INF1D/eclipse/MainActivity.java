package INF1D.eclipse;

import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.discovery.adapter.DiscoveryAdapter;
import INF1D.eclipse.databinding.*;
import INF1D.eclipse.setup.SetupActivity;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Eclipse);

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButton.setOnClickListener(this::startSetup);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DiscoveryAdapter(generateData()));
    }

    private void startSetup(View view) {
        startActivity(new Intent(this, SetupActivity.class));
    }

    private HashMap<Integer, Mirror> generateData() {
        HashMap<Integer, Mirror> data = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            data.put(i, new Mirror("mirror " + i, "127.0.0.1", "AAA:AAA:AAA"));
        }
        return data;
    }
}
