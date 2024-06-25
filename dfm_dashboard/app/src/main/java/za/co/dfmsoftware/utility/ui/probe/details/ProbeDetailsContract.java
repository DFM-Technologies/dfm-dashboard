package za.co.dfmsoftware.utility.ui.probe.details;

import androidx.annotation.NonNull;

import za.co.dfmsoftware.utility.ui.base.BasePresenter;
import za.co.dfmsoftware.utility.ui.base.BaseView;
import za.co.dfmsoftware.utility.utils.GraphFilterSelection;

public interface ProbeDetailsContract {

    interface View extends BaseView{
        void loadGraph(int probeId, int graphType, int days);
        void setToolbarTitle(@NonNull String title);
        GraphFilterSelection getCurrentFilterSelection();
    }

    interface Presenter extends BasePresenter<View> {
        void loadProbe(int probeId);
        void onFilterSelectionUpdated(@NonNull GraphFilterSelection graphFilterSelection);
    }
}
