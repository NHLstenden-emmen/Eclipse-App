package INF1D.eclipse.setup.fragments;

import android.os.Bundle;
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
import org.json.JSONObject;

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
        requireView().findViewById(R.id.nextButton).setOnClickListener(v -> ((SetupActivity) Objects.requireNonNull(getActivity())).nextButton(view));
        /*
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url ="http://eclipse.serverict.nl/api/widgets";
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                //create Json request
                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        binding.response.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.response.setText(error.toString());

                    }
                });
                // Access the RequestQueue through your singleton class.
                queue.add(jsonArrayRequest);
            }
        });*/

    }

}
