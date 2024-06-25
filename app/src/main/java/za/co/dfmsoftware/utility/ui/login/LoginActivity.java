package za.co.dfmsoftware.utility.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.ui.base.BaseActivity;
import za.co.dfmsoftware.utility.ui.main.MainActivity;

/**
 * LOGIN ACTIVITY
 * This class is responsible displaying data to the user, handling user interactions,
 * delegating user input actions to the Login Presenter, and implementing methods
 * defined in the LoginContract.View
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter, LoginContract.View> implements LoginContract.View{

    private EditText userEditText, passwordEditText;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;
    private LoginContract.Presenter presenter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        userEditText = findViewById(R.id.user_email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        passwordToggle = findViewById(R.id.password_toggle);

        setupViews();
        createPresenter();

        //attach the view to the presenter
        presenter.attach(this);

        //password toggle
        passwordToggle();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginContract.View getBaseView() {
        return this;
    }

    @Override
    protected void createPresenter() {
        this.presenter = new LoginPresenter(this.getDfmRetrofit());
    }

    @Nullable
    @Override
    protected LoginContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void loginSuccess() {
        this.hideProgressDialog();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        this.startActivity(mainActivityIntent);
        this.finish();
    }

    //
    @Override
    protected void setupViews() {
        findViewById(R.id.login_button).setOnClickListener(view -> this.presenter.onLoginButtonClicked());
    }

    @Override
    public String getEmailAddress() { return userEditText.getText().toString(); }

    public String getPassword() { return passwordEditText.getText().toString(); }

    @Override
    public void showLoginProgress() {
        this.showProgressDialog();
    }

    public void passwordToggle() {
        passwordToggle.setOnClickListener(view -> {
            if(isPasswordVisible) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordToggle.setImageResource((R.drawable.invisible));
            }else{
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordToggle.setImageResource(R.drawable.visible);
            }

            isPasswordVisible = !isPasswordVisible;
            passwordEditText.setSelection(passwordEditText.getText().length());
        });
    }

    //ERRORS IN LOGIN
    public void showEmailAddressError() {
        userEditText.setError(getString(R.string.user_email_error));
    }

    public void showPasswordError() {
        passwordEditText.setError(getString(R.string.user_password_error));
    }

    @Override
    public void showLoginError() {
        this.hideProgressDialog();
        this.showDialog(this, R.string.login_error, R.string.login_error_description, R.string.ok_label, () -> {});
    }
}
