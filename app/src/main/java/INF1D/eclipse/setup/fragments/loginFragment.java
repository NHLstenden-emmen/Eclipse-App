package INF1D.eclipse.setup.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import INF1D.eclipse.R;
import INF1D.eclipse.databinding.FragmentSetupLoginBinding;
import INF1D.eclipse.setup.SetupActivity;

public class loginFragment extends Fragment {

    private FragmentSetupLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_setup_login, container, false);
        binding = FragmentSetupLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireView().findViewById(R.id.nextButton).setOnClickListener(v -> ((SetupActivity) requireActivity()).nextButton(view));

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url ="http://eclipse.serverict.nl/api/mirrors";
                url ="https://api.androidhive.info/contacts/";
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                //create Json request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            binding.response.setText(response.toString());
                            JSONArray contacts = response.getJSONArray("contacts");
                            String jsonResponse = "names equals: ";
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                String id = c.getString("id");
                                String name = c.getString("name");
                                String email = c.getString("email");
                                String address = c.getString("address");
                                String gender = c.getString("gender");

                                // Phone node is JSON Object
                                JSONObject phone = c.getJSONObject("phone");
                                String mobile = phone.getString("mobile");
                                String home = phone.getString("home");
                                String office = phone.getString("office");

                                jsonResponse += name + " ";
                            }
                            binding.response.setText(jsonResponse);
                        } catch (final JSONException e) {
                            binding.response.setText(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.response.setText(error.toString());

                    }
                });
                // Access the RequestQueue through your singleton class.
                queue.add(jsonObjectRequest);
            }
        });

    }

}
