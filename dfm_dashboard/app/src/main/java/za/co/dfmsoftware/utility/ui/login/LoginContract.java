package za.co.dfmsoftware.utility.ui.login;

import za.co.dfmsoftware.utility.ui.base.BasePresenter;
import za.co.dfmsoftware.utility.ui.base.BaseView;

/**
 * LOGIN CONTRACT
 * This interface ensures that the Login Activity and Login Presenter have a
 * consistent way to communicate with one another
 */
public interface LoginContract {

    interface View extends BaseView{
        String getEmailAddress();

        String getPassword();

        void loginSuccess();

        void showLoginProgress();

        void showEmailAddressError();

        void showPasswordError();

        void showLoginError();
    }

    interface Presenter extends BasePresenter<View>{
        void onLoginButtonClicked();
    }
}
