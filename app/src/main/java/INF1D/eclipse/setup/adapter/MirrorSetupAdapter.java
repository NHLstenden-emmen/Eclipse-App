package INF1D.eclipse.setup.adapter;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.fragments.setup_2.mirrorselectFragment;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class MirrorSetupAdapter extends RecyclerView.Adapter<MirrorSetupAdapter.ViewHolder> {
    private final List<ScanResult> mirrorList = new ArrayList<>();
    private final mirrorselectFragment fragment;

    public MirrorSetupAdapter(mirrorselectFragment fragment) {
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
        holder.mirrorName.setText(this.mirrorList.get(position).SSID);
        holder.mirrorName.setOnClickListener(v -> {
            fragment.cancelSearch();
            ((SetupActivity) fragment.requireActivity()).setChosenMirror(this.mirrorList.get(position), fragment);
            ((SetupActivity) fragment.requireActivity()).nextButton();
        });
    }

    @Override
    public int getItemCount() {
        return this.mirrorList.size();
    }

    public void updateItems(List<ScanResult> scanResults) {
            mirrorList.clear();
            mirrorList.addAll(scanResults);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mirrorName;

        public ViewHolder(View view) {
            super(view);
            mirrorName = view.findViewById(R.id.mirrorSelectLabel);
        }
    }
}
