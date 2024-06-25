package za.co.dfmsoftware.utility.ui.probe.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.realm.DfmRealm;

/**
 * PROBE LIST PRESENTER
 */
public class ProbeListPresenter implements ProbeListContract.Presenter {
    private ProbeListContract.View view;
    private final DfmRealm dfmRealm;

    public ProbeListPresenter(@NonNull DfmRealm dfmRealm) { this.dfmRealm = dfmRealm; }

    @Override
    public void attach(@NonNull ProbeListContract.View view) { this.view = view; }

    @Override
    public void detach() {
        this.view = null;
    }

    @Nullable
    @Override
    public ProbeListContract.View getView() {
        return this.view;
    }

    @Override
    public void loadProbesForStatus(@NonNull ProbeStatus probeStatus) {
        List<Probe> probeList = (probeStatus == probeStatus.ALL_PROBES) ?
                this.dfmRealm.getAllProbes() :
                this.dfmRealm.getAllProbesForStatus(probeStatus);

        this.view.showProbesList(probeList);
    }

    @Override
    public void onProbeItemClicked(@NonNull Probe probe) {
        this.view.showProbeDetailsScreen(probe);
    }
}
