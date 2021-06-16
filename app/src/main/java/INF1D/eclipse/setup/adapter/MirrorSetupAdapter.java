package INF1D.eclipse.setup.adapter;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.fragments.setup_2.mirrorselectFragment;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MirrorSetupAdapter extends RecyclerView.Adapter<MirrorSetupAdapter.ViewHolder> {
    private List<ScanResult> mirrorList;
    private final mirrorselectFragment fragment;

    public MirrorSetupAdapter(List<ScanResult> mirrorList, mirrorselectFragment fragment) {
        this.mirrorList = mirrorList;
        this.fragment = fragment;
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            WifiUtils.withContext(Objects.requireNonNull(fragment.getContext())).scanWifi(this::getScanResults).start();
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void getScanResults(List<ScanResult> scanResults) {
        mirrorList = scanResults;
        fragment.getActivity().runOnUiThread(() -> {
            Toast.makeText(fragment.getContext(), "New MIRROR search results are in", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();

        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.setup_button, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mirrorName.setText(this.mirrorList.get(position).SSID);
        holder.mirrorName.setOnClickListener(v -> {
            ((SetupActivity) fragment.requireActivity()).setChosenMirror(this.mirrorList.get(position));
            ((SetupActivity) fragment.requireActivity()).nextButton();
        });
    }

    @Override
    public int getItemCount() {
        return this.mirrorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mirrorName;

        public ViewHolder(View view) {
            super(view);
            mirrorName = view.findViewById(R.id.mirrorSelectLabel);
        }
    }
}
