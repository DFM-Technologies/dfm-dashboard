package za.co.dfmsoftware.utility.ui.main;

import za.co.dfmsoftware.utility.ui.base.BasePresenter;
import za.co.dfmsoftware.utility.ui.base.BaseView;

/**
 * MAIN CONTRACT
 */
public interface MainContract {
    interface View extends BaseView {}

    interface Presenter extends BasePresenter<View> {}
}
