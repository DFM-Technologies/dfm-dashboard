package za.co.dfmsoftware.utility.ui.fragment.profile;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.ui.base.BaseFragment;
import za.co.dfmsoftware.utility.ui.login.LoginActivity;
import za.co.dfmsoftware.utility.utils.SendEmail;

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

        deleteAccountButton.setOnClickListener(v -> {
            String userEmail = emailAddressTextView.getText().toString();
            if (!userEmail.isEmpty()) {
                String subject = "Request to delete Account";
                String message = "Please delete my user Account. My email address is: " + userEmail;
                String recipientAddress = "nehemiah@dfmsoftware.co.za";

                Intent emailIntent = SendEmail.createEmailIntent(recipientAddress, subject, message);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No email clients installed on device.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "User email not found", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(v -> {
            DfmRealm dfmRealm = this.getDfmRealm();
            dfmRealm.clearAllData();

            Intent loginIntent = new Intent(this.getActivity(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(loginIntent);
            this.getActivity().finish();
        });
    }

    @Override
    protected void createPresenter() { this.presenter = new ProfilePresenter(this.getDfmRealm()); }

    @Override
    public void setEmailAddress(@Nullable String emailAddress) {
        this.emailAddressTextView.setText(emailAddress);
    }
}

