package za.co.dfmsoftware.utility.ui.probe.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.ui.base.BaseActivity;
import za.co.dfmsoftware.utility.ui.probe.details.ProbeDetailsActivity;
import za.co.dfmsoftware.utility.utils.ProbeListAdapter;

/**
 * 20/06/2024 - DONE BY NEHEMIAH PIETERSEN
 * PROBE LIST ACTIVITY
 */
public class ProbeListActivity extends BaseActivity<ProbeListContract.Presenter, ProbeListContract.View> implements ProbeListContract.View {

    public static String ARG_PROBE_STATUS = "probe.status";
    private ProbeListContract.Presenter probeListPresenter;
    private ProbeListAdapter probeListAdapter;
    private CompositeDisposable compositeDisposable;
    private Toolbar toolbar;
    private RecyclerView probeListView;

    @Override
    protected int getLayoutResId() { return R.layout.activity_probe_list; }

    @Override
    protected ProbeListContract.View getBaseView() { return this; }

    @Override
    protected void createPresenter() {
        this.probeListPresenter = new ProbeListPresenter(this.getDfmRealm());
    }

    @Nullable
    @Override
    protected ProbeListContract.Presenter getPresenter() {
        return this.probeListPresenter;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void setupViews() {
        toolbar = findViewById(R.id.app_toolbar);
        probeListView = findViewById(R.id.probes_list_view);

        Intent intent = this.getIntent();
        if (!intent.hasExtra(ARG_PROBE_STATUS)) {
            throw new IllegalArgumentException("No probe Status was set on the activity intent.");
        }

        this.compositeDisposable = new CompositeDisposable();
        ProbeStatus probeStatus = ProbeStatus.getStatus(intent.getIntExtra(ARG_PROBE_STATUS, ProbeStatus.ALL_PROBES.getStatus()));

        this.setupActionBar(this.toolbar, this.getProbeStatusTitle(probeStatus));
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme()));
        }

        this.probeListAdapter = new ProbeListAdapter(this);
        this.probeListAdapter.getOnItemClickedSubject()
                .doOnSubscribe(this.compositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.probeListPresenter::onProbeItemClicked);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.probeListView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        this.probeListView.setLayoutManager(linearLayoutManager);
        this.probeListView.setAdapter(this.probeListAdapter);

        this.probeListPresenter.loadProbesForStatus(probeStatus);
    }

    @Override
    public void showProbesList(@NonNull List<Probe> probeList) {
        this.probeListAdapter.clearAndApply(probeList);
    }

    @Override
    public void showProbeDetailsScreen(@NonNull Probe probe) {
        Intent graphDetailsIntent = new Intent(this, ProbeDetailsActivity.class);
        graphDetailsIntent.putExtra(ProbeDetailsActivity.ARG_PROBE_ID, probe.getProbeId());
        this.startActivity(graphDetailsIntent);
    }

    private String getProbeStatusTitle(@NonNull ProbeStatus probeStatus) {
        Resources resources = this.getResources();

        if (probeStatus == ProbeStatus.ALL_PROBES) {
            return resources.getString(R.string.all_label);
        }

        String[] titles = resources.getStringArray(R.array.probeStatusColor);

        if (probeStatus.getStatus() >= titles.length) {
            return resources.getString(R.string.none_label);
        }

        return titles[probeStatus.getStatus()];
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
