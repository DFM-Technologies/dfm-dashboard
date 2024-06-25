package za.co.dfmsoftware.utility.ui.fragment.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.model.model.User;
import za.co.dfmsoftware.utility.realm.DfmRealm;

/**
 * PROFILE PRESENTER
 */
public class ProfilePresenter implements ProfileContract.Presenter {

    private final DfmRealm dfmRealm;
    private ProfileContract.View view;

    public ProfilePresenter(@NonNull DfmRealm dfmRealm) { this.dfmRealm = dfmRealm; }

    @Override
    public void init() {
        User currentUser = this.dfmRealm.getCurrentUser();
        //todo fix the user info
        System.out.println("USER: " + currentUser.getUsername());
        this.view.setEmailAddress(currentUser.getUsername());
    }

    @Override
    public void attach(@NonNull ProfileContract.View view) { this.view = view; }

    @Override
    public void detach() { this.view = null; }

    @Nullable
    @Override
    public ProfileContract.View getView() {
        return this.view;
    }
}
