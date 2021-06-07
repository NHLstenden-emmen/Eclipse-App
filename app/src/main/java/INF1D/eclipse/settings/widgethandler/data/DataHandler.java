package INF1D.eclipse.settings.widgethandler.data;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class DataHandler extends Fragment {
    private AbstractDataHandler mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataProvider = new DataProvider();
    }

    public AbstractDataHandler getDataProvider() {
        return mDataProvider;
    }
}
