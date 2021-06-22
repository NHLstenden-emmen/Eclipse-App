package INF1D.eclipse.setup.adapter;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.fragments.setup_3.wifiselectFragment;
import android.net.wifi.ScanResult;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class WifiSetupAdapter extends RecyclerView.Adapter<WifiSetupAdapter.ViewHolder> {
    private List<ScanResult> wifiList = new ArrayList<>();
    private final wifiselectFragment fragment;

    public WifiSetupAdapter(wifiselectFragment fragment) {
        this.fragment = fragment;
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
            fragment.cancelSearch();
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(fragment.getContext()));
            builder.setTitle("Enter WiFi password");

            final EditText input = new EditText(fragment.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("Connect", (dialog, which) -> {
                if(String.valueOf(input.getText()).length() != 0) {
                    ((SetupActivity) fragment.requireActivity()).setChosenNetwork(wifiList.get(position));
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

    public void updateItems(List<ScanResult> filteredResults) {
        wifiList.clear();
        wifiList.addAll(filteredResults);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView wifiName;

        public ViewHolder(View view) {
            super(view);
            wifiName = view.findViewById(R.id.mirrorSelectLabel);
        }
    }
}
