package za.co.dfmsoftware.utility.ui.probe.details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.utils.GraphFilterSelection;

public class ProbeDetailsPresenter implements ProbeDetailsContract.Presenter{

    private ProbeDetailsContract.View view;
    private final DfmRealm dfmRealm;
    private Probe probe;

    public ProbeDetailsPresenter(DfmRealm dfmRealm) {
        this.dfmRealm = dfmRealm;
    }

    @Override
    public void attach(@NonNull ProbeDetailsContract.View view) { this.view = view; }

    @Override
    public void detach() { this.view = null; }

    @Nullable
    @Override
    public ProbeDetailsContract.View getView() {
        return this.view;
    }

    @Override
    public void loadProbe(int probeId) {
        this.probe = dfmRealm.getProbe(probeId);
        this.view.setToolbarTitle(this.probe.getProbe());

        GraphFilterSelection graphFilterSelection = this.view.getCurrentFilterSelection();
        this.onFilterSelectionUpdated(graphFilterSelection);
    }

    @Override
    public void onFilterSelectionUpdated(@NonNull GraphFilterSelection graphFilterSelection) {
        this.view.loadGraph(this.probe.getProbeId(), graphFilterSelection.getFilterType(), graphFilterSelection.getDays());
    }
}
