package INF1D.eclipse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<Widget> widgetsList;
    private RecyclerViewClickListener listener;

    public recyclerAdapter(ArrayList<Widget> widgetsList, RecyclerViewClickListener listener){
        this.widgetsList = widgetsList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView widgetText;

        public MyViewHolder(final View view){
            super(view);
            widgetText = view.findViewById(R.id.textViewWidget);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());

        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View widgetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_widgets, parent, false);
        return new MyViewHolder(widgetView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = widgetsList.get(position).getWidgetName();
        holder.widgetText.setText(name);
    }

    @Override
    public int getItemCount() {
        return widgetsList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
