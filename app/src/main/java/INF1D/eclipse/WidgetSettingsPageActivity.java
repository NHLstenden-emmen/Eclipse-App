package INF1D.eclipse;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetSettingsPageActivity extends AppCompatActivity {

    private ArrayList<Widget> Widgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_settings_page);
        TextView widgetType = findViewById(R.id.widgetType);
        TextView widgetparam = findViewById(R.id.params);

        Widgets = new ArrayList<>();
        String type = "Widgettype not set";
        JSONObject params;
        String paramstext = "no params";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("widget") != null){
                type = extras.getString("widget");
            }
            if(extras.getString("params") != null){
                try {
                    params = new JSONObject(extras.getString("params"));
                    if(type.equals("weather")) {
                        paramstext = params.getString("name");
                        findViewById(R.id.editTextTextPostalAddress).setVisibility(View.VISIBLE);
                    } else {
                        paramstext = params.toString();
                    }
                }
                catch (JSONException e){
                    //paramstext = e.toString();
                    paramstext = extras.getString("params").toString();
                }

            }
        }
        widgetType.setText(type);
        widgetparam.setText(paramstext);
    }
}
