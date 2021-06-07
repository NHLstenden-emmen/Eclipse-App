package INF1D.eclipse.discovery.adapter;

import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.MirrorSettingsActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Objects;

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {
    public final HashMap<Integer, Mirror> data;

    public DiscoveryAdapter (HashMap<Integer, Mirror> data){
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
      //  holder.settingsButton.setText(this.data.get(position));
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
            Intent intent = new Intent(context, MirrorSettingsActivity.class);
            intent.putExtra("selectedMirror", selectedMirror);

            context.startActivity(intent);
        }
    }
}
