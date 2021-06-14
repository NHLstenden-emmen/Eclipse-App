package INF1D.eclipse;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import INF1D.eclipse.databinding.ActivityWidgetSettingsBinding;

public class WidgetSettingsActivity extends AppCompatActivity {

    private ActivityWidgetSettingsBinding binding;

    private String token = "12|Fm7wPnlfNChv2ifrGSgFU7mS3LEMgk2lD3FsVOqR";

    private ArrayList<Widget>  Widgets;
    private RecyclerView recyclerView;
    private recyclerAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_settings);

        ActivityWidgetSettingsBinding binding = ActivityWidgetSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //recyclerView = binding.Parent;

        Widgets = new ArrayList<>();
        /*for (int j = 0; j < 10; j++) {
            Widgets.add(new Widget("naam " + (j + 1)));
        }*/
        String url = "http://eclipse.serverict.nl/api/widgets";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int j = 0; j < 5; j++) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Widget widget = new Widget(jsonObject.getString("type"));
                            widget.setParam(jsonObject.getString("params"));
                            Widgets.add(widget);
                        } catch (JSONException e) {
                       //     binding.textView.setText(e.toString());
                        }
                    }
                }

                System.out.println(Widgets);
                setAdapter();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /*
                if (error instanceof AuthFailureError) {
                    binding.textView.setText("Wrong login info");
                } else {
                    binding.textView.setText("There was an unknown problem while verifying your login info");
                    System.out.println(error.toString());
                }
                 */
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
        queue.add(jsonArrayRequest);
    }

    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(Widgets, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new recyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), WidgetSettingsPageActivity.class);
                intent.putExtra("widget", Widgets.get(position).getWidgetName());
                intent.putExtra("params", Widgets.get(position).getParam());
                startActivity(intent);
            }
        };
    }

}
