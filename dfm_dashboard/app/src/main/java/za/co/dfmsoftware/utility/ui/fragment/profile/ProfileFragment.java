package za.co.dfmsoftware.utility.ui.fragment.profile;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.ui.base.BaseFragment;
import za.co.dfmsoftware.utility.ui.login.LoginActivity;

public class ProfileFragment extends BaseFragment<ProfileContract.Presenter, ProfileContract.View> implements ProfileContract.View {

    private TextView emailAddressTextView;
    private ProfileContract.Presenter presenter;

    @Override
    protected int getLayoutResId() { return R.layout.fragment_profile; }

    @Override
    protected ProfileContract.View getBaseView() {
        return this;
    }

    @Nullable
    @Override
    protected ProfileContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    protected void setupViews(View view) {
        this.emailAddressTextView = view.findViewById(R.id.email_address_textview);
        this.presenter.init();

        //set up MenuProvider
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.profile_action_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                return handleMenuItemClick(menuItem);
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    protected void createPresenter() { this.presenter = new ProfilePresenter(this.getDfmRealm()); }

    @Override
    public void setEmailAddress(@Nullable String emailAddress) {
        this.emailAddressTextView.setText(emailAddress);
    }

    private boolean handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout_button) {
            DfmRealm dfmRealm = this.getDfmRealm();
            dfmRealm.clearAllData();

            Intent loginIntent = new Intent(this.getActivity(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(loginIntent);
            this.getActivity().finish();

            return true;
        }

        return false;
    }
}

