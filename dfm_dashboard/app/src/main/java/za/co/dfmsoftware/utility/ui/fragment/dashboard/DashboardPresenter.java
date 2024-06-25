package za.co.dfmsoftware.utility.ui.fragment.dashboard;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.service.DfmRetrofit;

public class DashboardPresenter implements  DashboardContract.Presenter{

    private DashboardContract.View view;
    private final DfmRetrofit dfmRetrofit;
    private final DfmRealm dfmRealm;
    private final CompositeDisposable compositeDisposable;

    public DashboardPresenter(DfmRetrofit dfmRetrofit, DfmRealm dfmRealm){
        this.dfmRetrofit = dfmRetrofit;
        this.dfmRealm = dfmRealm;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attach(@NonNull DashboardContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.compositeDisposable.clear();
        this.view = null;
    }

    @Nullable
    @Override
    public DashboardContract.View getView() { return this.view; }

    @Override
    public void loadProbes() {
        this.setProbeStatusViews();
        this.reloadProbes();
    }

    @SuppressLint("CheckResult")
    @Override
    public void reloadProbes() {
        this.view.showLoadProgress();
        this.dfmRetrofit.getProbes()
                .doOnSubscribe(this.compositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(probes -> {
                    this.view.hideLoadProgress();
                    this.setProbeStatusViews();
                }, throwable -> {
                    this.view.hideLoadProgress();
                });
    }

    private void setProbeStatusViews() { //used to get the ProbeStatus Views
        this.view.clearProbeView();

        int probeCount = this.dfmRealm.getProbesCount();
        this.view.addProbeView(ProbeStatus.ALL_PROBES, probeCount);

        HashMap<ProbeStatus, Integer> probeStatuses = this.dfmRealm.getAllProbesWithStatus();
        for(ProbeStatus status : probeStatuses.keySet()) {
            this.view.addProbeView(status, probeStatuses.get(status));
        }
    }
}
