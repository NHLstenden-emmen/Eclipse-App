package INF1D.eclipse.settings;

import INF1D.eclipse.R;
import INF1D.eclipse.common.Mirror;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ColorWheel extends AppCompatActivity {

    private Mirror selectedMirror;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setTitle("RGB Ledstrip Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_colorwheel);
        ColorPickerView colorPickerView = findViewById(R.id.colorPickerView);
        selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");
        selectedMirror.openWebSocket();

        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            int[] rgb = envelope.getArgb();
            try {
                JSONObject rgbJSON = new JSONObject()
                        .put("type", "color")
                        .put("r", rgb[1])
                        .put("g", rgb[2])
                        .put("b", rgb[3]);
                selectedMirror.sendJSONToMirror(rgbJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void onDestroy() {
        selectedMirror.closeWebsocket();
        super.onDestroy();
    }
}
