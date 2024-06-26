package za.co.dfmsoftware.utility.ui.fragment.profile;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.ui.base.BaseFragment;

public class ProfileFragment extends BaseFragment<ProfileContract.Presenter, ProfileContract.View> implements ProfileContract.View {

    private TextView emailAddressTextView;
    private ProfileContract.Presenter presenter;
    private Button logoutButton;
    private Button deleteAccountButton;

    @Override
    protected int getLayoutResId() { return R.layout.fragment_profile; }

    @Override
    protected ProfileContract.View getBaseView() {
        return this;
    }

    @Nullable
    @Override
    protected ProfileContract.Presenter getPresenter() { return this.presenter; }

    @Override
    protected void setupViews(View view) {
        this.emailAddressTextView = view.findViewById(R.id.email_address_textview);
        this.logoutButton = view.findViewById(R.id.logout_button);
        this.deleteAccountButton = view.findViewById(R.id.delete_account_button);
        this.presenter.init();

        logoutButton.setOnClickListener(v -> {
            System.out.println("Start logout intent");
        });

        deleteAccountButton.setOnClickListener(v -> {
            System.out.println("Start delete account intent");
        });
    }

    @Override
    protected void createPresenter() { this.presenter = new ProfilePresenter(this.getDfmRealm()); }

    @Override
    public void setEmailAddress(@Nullable String emailAddress) {
        this.emailAddressTextView.setText(emailAddress);
    }
}

