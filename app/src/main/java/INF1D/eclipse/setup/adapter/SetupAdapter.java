package INF1D.eclipse.setup.adapter;

import INF1D.eclipse.setup.fragments.setup_1.loginFragment;
import INF1D.eclipse.setup.fragments.setup_2.mirrorselectFragment;
import INF1D.eclipse.setup.fragments.setup_4.succesFragment;
import INF1D.eclipse.setup.fragments.setup_0.welcomeFragment;
import INF1D.eclipse.setup.fragments.setup_3.wifiselectFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SetupAdapter extends FragmentStateAdapter {
    private final int NUM_PAGES;

    public SetupAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        NUM_PAGES = 6;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            default:
            case 0:
                return new welcomeFragment();
            case 1:
                return new loginFragment();
            case 2:
                return new mirrorselectFragment();
            case 3:
                return new wifiselectFragment();
            case 4:
                return new succesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
