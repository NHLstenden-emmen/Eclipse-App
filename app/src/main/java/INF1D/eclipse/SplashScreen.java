package INF1D.eclipse;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_Eclipse);
        super.onCreate(savedInstanceState);

        launchHomeScreen();
    }

    private void launchHomeScreen() {
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
    }
}
