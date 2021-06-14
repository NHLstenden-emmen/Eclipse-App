package INF1D.eclipse.setup.adapter;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.fragments.setup_2.mirrorselectFragment;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private final List<ScanResult> wifiList;
    private final ArrayList<ViewHolder> views = new ArrayList<>();
    private final Context context;

    public WifiAdapter (List<ScanResult> wifiList, Context context) {
        this.wifiList = wifiList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.setup_button, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        views.add(holder);

        holder.mirrorName.setText(this.wifiList.get(position).SSID);
        holder.mirrorName.setOnClickListener(v -> {
            views.forEach(vi -> vi.mirrorName.setBackgroundColor(Color.parseColor("#CFCECE")));

            holder.mirrorName.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            mirrorselectFragment.setSelected();
        });
    }

    @Override
    public int getItemCount() {
        return this.wifiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mirrorName;
        private final Context context;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            mirrorName = view.findViewById(R.id.mirrorSelectLabel);

            //view.findViewById(R.id.mirrorSettings).setOnClickListener(this::openSettings);
        }
    }
}
