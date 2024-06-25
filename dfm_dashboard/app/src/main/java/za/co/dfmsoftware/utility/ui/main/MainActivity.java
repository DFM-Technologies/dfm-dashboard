package za.co.dfmsoftware.utility.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationBarView;

import za.co.dfmsoftware.utility.Logger;
import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.model.model.User;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.ui.base.BaseActivity;
import za.co.dfmsoftware.utility.ui.fragment.dashboard.DashboardFragment;
import za.co.dfmsoftware.utility.ui.fragment.profile.ProfileFragment;
import za.co.dfmsoftware.utility.ui.login.LoginActivity;

/**
 * MAIN ACTIVITY
 */
public class MainActivity extends BaseActivity<MainContract.Presenter, MainContract.View> implements MainContract.View{

    private  static final String TAG = MainActivity.class.getSimpleName(); //used in bot nav
    private MainContract.Presenter presenter;
    private NavigationBarView navigationBarView;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        DfmRealm dfmRealm = this.getDfmRealm();
        User currentUser = dfmRealm.getCurrentUser();

        if(currentUser == null){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            this.startActivity(loginIntent);
            this.finish();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainContract.View getBaseView() {
        return this;
    }

    @Override
    protected void createPresenter() {
        this.presenter = new MainPresenter();
    }

    @Nullable
    @Override
    protected MainContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    protected void setupViews() {
        toolbar = findViewById(R.id.app_toolbar);
        navigationBarView = findViewById(R.id.bottom_navigation_view);

        //BOTTOM NAV
        navigationBarView.setOnItemSelectedListener(onItemSelectedListener);
        navigationBarView.setSelectedItemId(R.id.nav_dashboard_bot);
    }

    //BOTTOM NAV
    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        try {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_dashboard_bot) {
                this.showFragment(DashboardFragment.class, false);
                this.setupFragmentToolbarTitle(this.toolbar);
            }else if(itemId == R.id.nav_profile_bot) {
                this.showFragment(ProfileFragment.class, false);
                this.setupFragmentToolbarTitle(this.toolbar);
            }
        }catch (Exception e) {
            Logger.e(TAG, "Error on menu item: ", e);
        }

        return false;
    };
}