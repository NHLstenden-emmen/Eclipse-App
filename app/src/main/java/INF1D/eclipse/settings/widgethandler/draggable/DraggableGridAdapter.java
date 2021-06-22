package INF1D.eclipse.settings.widgethandler.draggable;

import INF1D.eclipse.common.MirrorSocket;
import INF1D.eclipse.R;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.widget.widgetSettingsActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class DraggableGridAdapter extends RecyclerView.Adapter<DraggableGridAdapter.MyViewHolder> implements DraggableItemAdapter<DraggableGridAdapter.MyViewHolder>, Serializable {
    private static final String TAG = "MyDraggableItemAdapter";
    public static HashMap<Integer, MyViewHolder> itemHolders = new HashMap<>();
    private static DataProvider mProvider;
    private Context context;

    public DraggableGridAdapter(DataProvider dataProvider) {
        mProvider = dataProvider;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.fragment_widgethandler_grid_item, parent, false);
        context = parent.getContext();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final DataProvider.TileData item = mProvider.getItem(position);

        holder.mContainer.setOnClickListener(onClickListener(position));
        holder.mIcon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), item.parseIcon(), null));
        final DraggableItemState dragState = holder.getDragState();
        itemHolders.put(position, holder);
        if (dragState.isUpdated()) {
            int bgResId;

            if (dragState.isActive()) {
                bgResId = R.drawable.bg_item_dragging_active_state;
                clearState(holder.mContainer.getForeground());
            } else if (dragState.isDragging()) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

           holder.mContainer.setBackgroundResource(bgResId);
        }
    }

    public View.OnClickListener onClickListener(int clickedPosition) {
        return v -> {
            Intent widgetSettingsActivity = new Intent(mProvider.getContext(), widgetSettingsActivity.class);

            widgetSettingsActivity.putExtra("position", clickedPosition);
            widgetSettingsActivity.putExtra("dataProvider", mProvider);

            Objects.requireNonNull(mProvider.getContext()).getApplicationContext().startActivity(widgetSettingsActivity);
        };
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    private JSONObject parseJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject().put("type", "new_widget");
        MirrorSocket.parseTileDataToJSON();
        DraggableGridAdapter.getDataProvider().getmData().forEach((i, w) -> {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(w.type);
            try {
                jsonObject.put(String.valueOf(w.getId()), jsonArray);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        });
        //TODO SENT TO MIRROR

        return jsonObject;
    }


    @Override
    public void onMoveItem(int fromPosition, int toPosition) {

        Toast.makeText(mProvider.getContext(), "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition, Toast.LENGTH_SHORT).show();
        mProvider.swapItem(fromPosition, toPosition);
        try {
            mProvider.getSelectedMirror().sendJSONToMirror(parseJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
        return null;
    }


    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
         notifyDataSetChanged();
    }

    public void clearState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(new int[]{});
        }
    }

    public static HashMap<Integer, MyViewHolder> getItemHolders() {
        return itemHolders;
    }
    public static DataProvider getDataProvider() {
        return mProvider;
    }
    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        public ImageView mIcon;

        public MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mIcon = v.findViewById(R.id.drag_icon);
        }
    }
}
