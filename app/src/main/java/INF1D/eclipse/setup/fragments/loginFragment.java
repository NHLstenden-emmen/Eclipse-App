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

import com.android.volley.AuthFailureError;
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
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import INF1D.eclipse.R;
import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.databinding.FragmentSetupLoginBinding;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.adapter.SetupAdapter;

import android.content.Intent;
import android.net.Uri;

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
        requireView().findViewById(R.id.nextButton).setOnClickListener(v -> ((SetupActivity) requireActivity()).nextButton());
        requireView().findViewById(R.id.prevButton).setOnClickListener(v -> ((SetupActivity) requireActivity()).prevButton());
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.UsernameInput.getText().length() > 0 && binding.passwordInput.getText().length() > 0){
                    String username = binding.UsernameInput.getText().toString();
                    String password = binding.passwordInput.getText().toString();
                    String regex = "[`~!#$%^&*()_+=\\{}\\[\\]|\\\\:;“’<,>?๐฿]";
                    Pattern pattern = Pattern.compile(regex , Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(username);
                    System.out.println(pattern);
                    if(matcher.find()){
                        binding.response.setText("not allowed characters in username " + username);
                    } else {
                        if(Pattern.matches(regex, password)){
                            binding.response.setText("not allowed characters in your password");
                        } else {
                            String url ="http://eclipse.serverict.nl/api/login";
                            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("email", binding.UsernameInput.getText().toString());
                                jsonObj.put("password", binding.passwordInput.getText().toString());
                            } catch (JSONException e) {
                                binding.response.setText(e.toString());
                            }

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.POST, url,jsonObj, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        binding.response.setText(response.getString("token"));
                                        System.out.println(response.getString("token"));
                                        PrefManager prefManager = new PrefManager(getContext());
                                        prefManager.setUserToken(response.getString("token"));
                                    } catch (JSONException e){
                                        binding.response.setText(e.toString());
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if(error instanceof AuthFailureError){
                                        binding.response.setText("Wrong login info");
                                    } else {
                                        binding.response.setText("There was an unknown problem while verifying your login info");
                                        System.out.println(error.toString());
                                    }

                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String>  params = new HashMap< String, String>();
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
                            queue.add(jsonObjectRequest);
                        }
                    }
                } else {
                    binding.response.setText("Please enter your login info");
                }
            }
        });

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eclipse.serverict.nl/registration"));
                        startActivity(browserIntent);
            }
        });
    }
}
