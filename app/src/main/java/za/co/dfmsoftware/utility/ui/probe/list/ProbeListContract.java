package za.co.dfmsoftware.utility.ui.probe.list;

import androidx.annotation.NonNull;

import java.util.List;

import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.ui.base.BasePresenter;
import za.co.dfmsoftware.utility.ui.base.BaseView;

public interface ProbeListContract {

    interface View extends BaseView {
        void showProbesList(@NonNull List<Probe> probeList);
        void showProbeDetailsScreen(@NonNull Probe probe);
    }

    interface Presenter extends BasePresenter<View> {
        void loadProbesForStatus(@NonNull ProbeStatus probeStatus);
        void onProbeItemClicked(@NonNull Probe probe);
    }
}
