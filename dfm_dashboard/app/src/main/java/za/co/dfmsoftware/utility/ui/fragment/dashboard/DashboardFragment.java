package za.co.dfmsoftware.utility.ui.fragment.dashboard;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.flexbox.FlexboxLayout;

import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.ui.base.BaseFragment;
import za.co.dfmsoftware.utility.ui.probe.list.ProbeListActivity;
import za.co.dfmsoftware.utility.ui.view.ProbeCardView;

public class DashboardFragment extends BaseFragment<DashboardContract.Presenter, DashboardContract.View> implements DashboardContract.View {

    private DashboardContract.Presenter presenter;
    private FlexboxLayout flexProbes;
    private SwipeRefreshLayout viewPullRefresh;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void createPresenter() {
        this.presenter = new DashboardPresenter(this.getDfmRetrofit(), this.getDfmRealm());
        this.presenter.attach(this);
    }

    @Nullable
    @Override
    protected DashboardContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    protected DashboardContract.View getBaseView() {
        return this;
    }

    @Override
    protected void setupViews(View view) {
        flexProbes = view.findViewById(R.id.flex_probes_layout);
        viewPullRefresh = view.findViewById(R.id.pull_refresh);

        this.viewPullRefresh.setOnRefreshListener(() -> this.presenter.reloadProbes());

        //use MenuHost and MenuProvider for managing the options menu
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.dashboard_action_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.action_refresh) {
                    presenter.reloadProbes();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        this.presenter.loadProbes();  //load probes after views are set up
    }

    @Override
    public boolean setToolbarTitle(@NonNull TextView title) {
//todo        fix title.setText(R.string.bot_nav_dashboard);
        title.setText(R.string.bot_nav_dashboard);
        return true;
    }

    @Override
    public void addProbeView(@NonNull ProbeStatus probeStatus, int totalProbes) {
        FragmentActivity activity = requireActivity();
        ProbeCardView probeCardView = new ProbeCardView(activity);
        probeCardView.configureView(probeStatus, totalProbes);
        probeCardView.setClickable(true);
        probeCardView.setFocusable(true);
//        probeCardView.setTag(probeStatus);
        probeCardView.setOnClickListener(view -> {
            Intent probeListIntent = new Intent(activity, ProbeListActivity.class);
            probeListIntent.putExtra(ProbeListActivity.ARG_PROBE_STATUS, probeStatus.getStatus());
            activity.startActivity(probeListIntent);
        });

        this.flexProbes.addView(probeCardView);
    }

    @Override
    public void clearProbeView() {
        this.flexProbes.removeAllViews();
    }

    @Override
    public void showLoadProgress() {
        this.viewPullRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoadProgress() {
        viewPullRefresh.postDelayed(() -> viewPullRefresh.setRefreshing(false), 600);
    }
}

