package za.co.dfmsoftware.utility.ui.fragment.dashboard;

import androidx.annotation.NonNull;

import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.ui.base.BasePresenter;
import za.co.dfmsoftware.utility.ui.base.BaseView;

/**
 * DASHBOARD CONTRACT
 */
public interface DashboardContract {

    interface View extends BaseView{ //used by Dashboard fragment
        void addProbeView(@NonNull ProbeStatus probeStatus, int totalProbes);

        void clearProbeView();

        void showLoadProgress();

        void hideLoadProgress();
    }

    interface Presenter extends BasePresenter<View> { //used by Dashboard presenter
        void loadProbes();

        void reloadProbes();
    }

}
