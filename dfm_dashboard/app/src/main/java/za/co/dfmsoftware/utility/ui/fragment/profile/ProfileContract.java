package za.co.dfmsoftware.utility.ui.fragment.profile;

import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.ui.base.BasePresenter;
import za.co.dfmsoftware.utility.ui.base.BaseView;

/**
 * PROFILE CONTRACT
 */
public interface ProfileContract {
    interface View extends BaseView {
        void setEmailAddress(@Nullable String emailAddress);
    }

    interface Presenter extends BasePresenter<View> {
        void init();
    }
}
