package INF1D.eclipse.setup.adapter;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.fragments.setup_2.mirrorselectFragment;
import INF1D.eclipse.setup.fragments.setup_3.wifiselectFragment;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WifiSetupAdapter extends RecyclerView.Adapter<WifiSetupAdapter.ViewHolder> {
    private List<ScanResult> wifiList;
    private final wifiselectFragment fragment;

    public WifiSetupAdapter(List<ScanResult> wifiList, wifiselectFragment fragment) {
        this.wifiList = wifiList;
        this.fragment = fragment;
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            WifiUtils.withContext(Objects.requireNonNull(fragment.getContext())).scanWifi(this::getScanResults).start();
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void getScanResults(List<ScanResult> scanResults) {
        wifiList = scanResults;
        Toast.makeText(fragment.getContext(), "New wifi search results are in", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.setup_button, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.wifiName.setText(this.wifiList.get(position).SSID);
        holder.wifiName.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(fragment.getContext()));
            builder.setTitle("Enter WiFi password");

            final EditText input = new EditText(fragment.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("Connect", (dialog, which) -> {
                if(String.valueOf(input.getText()).length() != 0) {
                    Toast.makeText(fragment.getContext(), input.getText().toString(), Toast.LENGTH_SHORT).show();
                    ((SetupActivity) fragment.requireActivity()).setChosenMirror(wifiList.get(position));
                    ((SetupActivity) fragment.requireActivity()).executeSetup(String.valueOf(input.getText()));
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return this.wifiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView wifiName;

        public ViewHolder(View view) {
            super(view);
            wifiName = view.findViewById(R.id.mirrorSelectLabel);
        }
    }
}
