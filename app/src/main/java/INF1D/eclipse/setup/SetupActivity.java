package INF1D.eclipse.setup;

import INF1D.eclipse.R;
import INF1D.eclipse.setup.adapter.SetupAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class SetupActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SetupAdapter(this));
        viewPager.setUserInputEnabled(false);
    }

    private int getNextItem() {
        return viewPager.getCurrentItem() + 1;
    }

    public void nextButton() {
        int current = getNextItem();
        if (current < Objects.requireNonNull(viewPager.getAdapter()).getItemCount() -1) {
            viewPager.setCurrentItem(current);
        } else {
            finish();
        }
    }
}
