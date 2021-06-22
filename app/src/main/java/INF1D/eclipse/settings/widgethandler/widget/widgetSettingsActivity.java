package INF1D.eclipse.settings.widgethandler.widget;

import INF1D.eclipse.R;
import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Objects;

public class widgetSettingsActivity extends AppCompatActivity {

    private Mirror selectedMirror;
    private DataProvider.TileData selectedTileData;
    private int selectedPosition;

    private HashMap<String, DataProvider.TileData> widgetList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_widget_settings);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            selectedPosition            = bundle.getInt("position");
            DataProvider mProvider      = (DataProvider) bundle.getSerializable("dataProvider");
            widgetList                  = mProvider.getAvailableWidgets();
            selectedTileData            = mProvider.getItem(selectedPosition);
            selectedMirror              = mProvider.getSelectedMirror();
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("Widget Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.widgetSettings, new WidgetSettings())
                    .commit();
        }
    }

    public Mirror getMirror() {
        return this.selectedMirror;
    }

    public DataProvider.TileData getSelectedTileData() {
        return this.selectedTileData;
    }

    public int getCurrentPosition() {
        return this.selectedPosition;
    }

    public HashMap<String, DataProvider.TileData> getWidgetList() {
        return widgetList;
    }

}
