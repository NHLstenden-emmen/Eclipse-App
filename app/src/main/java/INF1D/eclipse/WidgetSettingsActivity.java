package INF1D.eclipse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import INF1D.eclipse.databinding.ActivityMainBinding;
import INF1D.eclipse.databinding.ActivityWidgetSettingsBinding;

import static android.view.View.generateViewId;

public class WidgetSettingsActivity extends AppCompatActivity {

    private ActivityWidgetSettingsBinding binding;

    private String token = "12|Fm7wPnlfNChv2ifrGSgFU7mS3LEMgk2lD3FsVOqR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_settings);

        ActivityWidgetSettingsBinding binding = ActivityWidgetSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.TESTbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://eclipse.serverict.nl/api/widgets";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        binding.textView.setText(response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            binding.textView.setText(binding.textView.getText().toString() + i);
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                LinearLayout WidgetLayout = new LinearLayout(v.getContext());
                                WidgetLayout.setOrientation(LinearLayout.HORIZONTAL);
                                TextView widgetItem = new TextView(v.getContext());
                                Switch widgetSwitch = new Switch(v.getContext());

                                widgetItem.setText(jsonObject.getString("type"));
                                widgetItem.setId(i + 1);
                                widgetItem.setTextSize(25);
                                widgetItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "This is a message displayed in a Toast" + v.getId(),
                                                Toast.LENGTH_SHORT);

                                        toast.show();
                                    }
                                });
                                WidgetLayout.addView(widgetItem);
                                WidgetLayout.addView(widgetSwitch);
                                binding.Parent.addView(WidgetLayout);
                            }catch (JSONException e){
                                binding.textView.setText(e.toString());
                            }

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof AuthFailureError) {
                            binding.textView.setText("Wrong login info");
                        } else {
                            binding.textView.setText("There was an unknown problem while verifying your login info");
                            System.out.println(error.toString());
                        }

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("content-type", "application/json");
                        params.put("accept", "application/json");
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                // Access the RequestQueue through your singleton class.
                queue.add(jsonArrayRequest);
            }
        });
    }

    private void onResponse (View v){

    }

}