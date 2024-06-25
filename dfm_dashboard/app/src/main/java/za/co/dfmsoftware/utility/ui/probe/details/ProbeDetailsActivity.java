package za.co.dfmsoftware.utility.ui.probe.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.HashMap;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.model.model.User;
import za.co.dfmsoftware.utility.ui.base.BaseActivity;
import za.co.dfmsoftware.utility.ui.view.GraphFilterView;
import za.co.dfmsoftware.utility.utils.GraphFilterSelection;
import za.co.dfmsoftware.utility.utils.PreferenceHelper;

/**
 * 25/06/2024 - DONE BY NEHEMIAH PIETERSEN
 * PROBE DETAILS ACTIVITY
 * This is the Probe Details Activity that will be responsible for:
 */
public class ProbeDetailsActivity extends BaseActivity<ProbeDetailsContract.Presenter, ProbeDetailsContract.View> implements ProbeDetailsContract.View {
    public static String ARG_PROBE_ID = "probe.id";
    private ProbeDetailsContract.Presenter presenter;
    private HashMap<String, String> headers;
    private CompositeDisposable compositeDisposable;
    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;
    private GraphFilterView graphFilterView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.compositeDisposable.clear();
    }

    @Override
    protected int getLayoutResId() { return R.layout.activity_probe_details; }

    @Override
    protected ProbeDetailsContract.View getBaseView() { return this; }

    @Override
    protected void createPresenter() {
        this.presenter = new ProbeDetailsPresenter(this.getDfmRealm());
    }

    @Nullable
    @Override
    protected ProbeDetailsContract.Presenter getPresenter() { return this.presenter; }

    @SuppressLint({"SetJavaScriptEnabled", "CheckResult"})
    @Override
    protected void setupViews() {

        //initialise variables
        toolbar = findViewById(R.id.app_toolbar);
        webView = findViewById(R.id.probe_details_web_view);
        progressBar = findViewById(R.id.progress_indicator);
        graphFilterView = findViewById(R.id.graph_filter_view);

        Intent intent = this.getIntent();
        if(!intent.hasExtra(ARG_PROBE_ID)) {
            throw new IllegalArgumentException("No Probe was set on the Activity");
        }

        this.setupActionBar(this.toolbar, R.string.probe_details_label);

        this.compositeDisposable = new CompositeDisposable();

        int probeId = intent.getIntExtra(ARG_PROBE_ID, 0);

        User user = this.getDfmRealm().getCurrentUser();
        this.headers = new HashMap<>(1);
        this.headers.put("Authorization", String.format(Locale.getDefault(), "bearer %s", user.getAccessToken()));
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        this.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);

                if(progress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        this.graphFilterView.restoreFilterSelection(PreferenceHelper.getInstance(this).getGraphFilterSelection());
        this.graphFilterView.getOnFilterSelectionUpdateSubject()
                .doOnSubscribe(this.compositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(graphFilterSelection -> {
                    PreferenceHelper.getInstance(this).setGraphFilterSelection(graphFilterSelection);
                    this.presenter.onFilterSelectionUpdated(graphFilterSelection);
                });

        this.presenter.loadProbe(probeId);
    }

    @Override
    public void loadGraph(int probeId, int graphType, int days) {
        webView.loadUrl(getString(R.string.url_probe_details, probeId, days, graphType), headers);
    }

    @Override
    public void setToolbarTitle(@NonNull String title) {
        this.setupActionBar(this.toolbar, title);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme()));
        }
    }

    @Override
    public GraphFilterSelection getCurrentFilterSelection() {
        return this.graphFilterView.getCurrentFilterSelection();
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
