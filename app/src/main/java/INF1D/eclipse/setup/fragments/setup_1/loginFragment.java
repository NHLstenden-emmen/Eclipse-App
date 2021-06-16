package INF1D.eclipse.setup.fragments.setup_1;

import INF1D.eclipse.R;
import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.databinding.FragmentSetup1Binding;
import INF1D.eclipse.setup.SetupActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class loginFragment extends Fragment {
    private FragmentSetup1Binding binding;
    private boolean isRunning;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetup1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.prevButton.setOnClickListener(v -> ((SetupActivity) requireActivity()).prevButton());
        binding.nextButton.setOnClickListener(v -> {
            if (binding.UsernameInput.getText().length() > 0 && binding.passwordInput.getText().length() > 0) {
                String username = String.valueOf(binding.UsernameInput.getText());
                String password = String.valueOf(binding.passwordInput.getText());

                if(!isRunning) {
                    new Thread(() -> {
                        try {
                            isRunning = true;
                            loginRequest(loginCallback(), username, password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            else {
                Toast.makeText(getContext(), "No credentials entered!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.signupButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eclipse.serverict.nl/registration"));
            startActivity(browserIntent);
        });
    }

    public void loginRequest(final VolleyCallback callback, String username, String password) throws JSONException {
        JSONObject postPayload = new JSONObject().put("email", username).put("password", password);

        String loginEndpointURL = "http://eclipse.serverict.nl/api/login";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginEndpointURL, postPayload, callback::onSuccess, callback::onError);
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(jsonObjectRequest);
    }


      private VolleyCallback loginCallback() {
        return new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                isRunning = false;
                ((SetupActivity) requireActivity()).nextButton();
                try {
                    new PrefManager(getContext()).setUserToken(result.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                isRunning = false;
                Toast.makeText(getContext(), "Login failed, credentials not correct", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onError(VolleyError error);
    }

}
