package INF1D.eclipse.discovery.adapter;

import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.R;
import INF1D.eclipse.common.PrefManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import INF1D.eclipse.settings.MirrorSettingsActivity;
import java.util.List;
import java.util.Objects;

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {
    public final List<Mirror> data;

    public DiscoveryAdapter (List<Mirror> data){
        this.data = data;
    }

    @NonNull
    @Override
    public DiscoveryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(INF1D.eclipse.R.layout.mirror_card, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoveryAdapter.ViewHolder holder, int position) {
        holder.mirrorName.setText(Objects.requireNonNull(this.data.get(position)).name);
        holder.selectedMirror = this.data.get(position);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Mirror selectedMirror;
        private final TextView mirrorName;

        private final Context context;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            mirrorName = view.findViewById(R.id.mirrorName);

            view.findViewById(R.id.mirrorSettings).setOnClickListener(this::openSettings);
        }

        private void openSettings(View view) {
            PrefManager prefManager = new PrefManager(context);
            if(prefManager.isUserLoggedIn()) {
                Intent intent = new Intent(context, MirrorSettingsActivity.class);
                intent.putExtra("selectedMirror", selectedMirror);

                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Please log in to change the settings of your mirror!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
