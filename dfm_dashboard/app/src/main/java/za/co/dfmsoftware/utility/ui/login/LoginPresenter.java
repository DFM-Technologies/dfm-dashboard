package za.co.dfmsoftware.utility.ui.login;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import za.co.dfmsoftware.utility.service.DfmRetrofit;
import za.co.dfmsoftware.utility.service.GrantType;

/**
 * LOGIN PRESENTER
 * This is the presenter class
 * This mediates the interaction between the VIEW and the MODEL
 * This class is used to respond to validate user input, respond
 * to user interactions, making network requests, and updating
 * the View based on the results on the performed operation
 * */
public class LoginPresenter implements LoginContract.Presenter{

    private final DfmRetrofit dfmRetrofit;
    private LoginContract.View view;
    private final CompositeDisposable compositeDisposable;

    public LoginPresenter(@NonNull DfmRetrofit dfmRetrofit) {
        this.dfmRetrofit = dfmRetrofit;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attach(@NonNull LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.compositeDisposable.clear();
        this.view = null;
    }

    @Nullable
    @Override
    public LoginContract.View getView() {
        return this.view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLoginButtonClicked() {
        String emailAddress = this.view.getEmailAddress();
        String password = this.view.getPassword();

        if(!this.validateEmailAddress(emailAddress) || !this.validatePassword(password)){
            return;
        }

        this.view.showLoginProgress();
        this.dfmRetrofit.authenticateUser(GrantType.PASSWORD.getValue(), emailAddress, password)
                .doOnSubscribe(this.compositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> this.view.loginSuccess(),
                        throwable -> this.view.showLoginError()
                );
    }

    //Method used to validate user email address
    private boolean validateEmailAddress(@NonNull String emailAddress){
        if(TextUtils.isEmpty(emailAddress) || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            this.view.showEmailAddressError();
            return false;
        }

        return true;
    }

    //Method used to validate user password
    private boolean validatePassword(@NonNull String password){
        if(TextUtils.isEmpty(password)){
            this.view.showPasswordError();
            return false;
        }

        return true;
    }
}
