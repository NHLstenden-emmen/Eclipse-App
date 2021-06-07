package INF1D.eclipse.settings.widgethandler;

import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.widgethandler.data.AbstractDataHandler;
import INF1D.eclipse.settings.widgethandler.data.DataHandler;
import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import INF1D.eclipse.settings.widgethandler.draggable.DraggableGrid;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class WidgetHandlerActivity extends AppCompatActivity {
    private Mirror selectedMirror;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");

        setContentView(R.layout.activity_widgethandler);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new DataHandler(), "data provider")
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.widgetContainer, new DraggableGrid(), "list view")
                    .commit();
        }
    }

    public AbstractDataHandler getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag("data provider");
        if (fragment != null) {
            return ((DataHandler) fragment).getDataProvider();
        }
        return null;
    }
}
