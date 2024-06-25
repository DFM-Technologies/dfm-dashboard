package za.co.dfmsoftware.utility.ui.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import javax.annotation.Nullable;

import io.reactivex.functions.Action;
import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.service.DfmRetrofit;
import za.co.dfmsoftware.utility.utils.ProgressDialogFragment;

public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView> extends AppCompatActivity implements BaseView {

    private DfmRealm dfmRealm;
    private DfmRetrofit dfmRetrofit;
    private Dialog dialog;
    private ProgressDialogFragment progressDialogFragment;

    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);

        this.dfmRealm = DfmRealm.getInstance();
        this.dfmRetrofit =DfmRetrofit.getInstance();

        this.setContentView(this.getLayoutResId());
        this.createPresenter();

        P presenter = getPresenter();
        if(presenter != null){
            presenter.attach(this.getBaseView());
        }

        this.setupViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        P presenter = getPresenter();
        if(presenter != null){
            presenter.detach();
        }
    }

    public boolean onSupportNavigation() {
        getOnBackPressedDispatcher();
        return true;
    }

    public void showDialog(@NonNull Context context,
                           @StringRes int title,
                           @StringRes int message,
                           @StringRes int positiveButtonCaption,
                           @NonNull Action onPositiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        this.dialog = builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(positiveButtonCaption, (dialog1, which) -> {
                    try {
                        dialog1.dismiss();
                        onPositiveClickListener.run();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                })
                .create();

        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    public void hideDialog() {
        if(this.dialog != null){
            this.dialog.dismiss();
            this.dialog = null;
        }
    }

    protected void showProgressDialog() {
        showProgressDialog(getString(R.string.please_wait_label));
    }

    protected void showProgressDialog(@NonNull String message) {
        hideProgressDialog();
        progressDialogFragment = ProgressDialogFragment.newInstance();
        progressDialogFragment.show(getSupportFragmentManager(), "progress");
    }

    public void hideProgressDialog() {
        if(this.progressDialogFragment != null) {
            progressDialogFragment.dismiss();
            progressDialogFragment = null;
        }
    }

    //TOOLBAR NAV
    protected void setupActionBar(@Nullable Toolbar toolbar, @StringRes int title){
        String titleString = this.getString(title);
        this.setupActionBar(toolbar, titleString);
    }

    protected void setupActionBar(@Nullable Toolbar toolbar, String title) {
        if(toolbar != null){
            TextView appTitle = toolbar.findViewById(R.id.toolbar_textview);
            appTitle.setText(title);

            ActionBar actionBar = setupActionBar(toolbar);
            if(actionBar != null){
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    private androidx.appcompat.app.ActionBar setupActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        return getSupportActionBar();
    }

    //BOTTOM NAV BAR
    protected void showFragment(Class<? extends Fragment> fragment, boolean addToBackStack)
        throws IllegalStateException, IllegalAccessException, InstantiationException{
        this.showFragment(fragment, null, addToBackStack, false);
    }

    protected void showFragment(Class<? extends Fragment> fragment, Bundle arguments, boolean addToBackStack, boolean removeCurrentFragment)
        throws IllegalStateException, IllegalAccessException, InstantiationException {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        String fragmentTAG = fragment.getSimpleName();

        Fragment fragmentToAdd;
        Fragment fragmentCached = this.getSupportFragmentManager().findFragmentByTag(fragmentTAG);

        if(fragmentCached != null){
            fragmentToAdd = fragmentCached;
        }else {
            fragmentToAdd = fragment.newInstance();
        }

        if (arguments != null){
            fragmentToAdd.setArguments(arguments);
        }

        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragmentTAG);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        }else {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.disallowAddToBackStack();
        }

        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(currentFragment != null){
            if(removeCurrentFragment){
                fragmentTransaction.remove(currentFragment);
            }else{
                fragmentTransaction.detach(currentFragment);
            }
        }

        if(this.getSupportFragmentManager().findFragmentByTag(fragmentTAG) == null) {
            fragmentTransaction.add(R.id.content_frame, fragmentToAdd, fragmentTAG);
        }else{
            fragmentTransaction.attach(fragmentToAdd);
        }

        fragmentTransaction.commitAllowingStateLoss();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    protected void setupFragmentToolbarTitle(@Nullable Toolbar toolbar) {
        if(toolbar != null) {
            BaseFragment baseFragment = (BaseFragment) this.getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if(baseFragment != null) {
                TextView cardTitleView = toolbar.findViewById(R.id.toolbar);
                baseFragment.setToolbarTitle(cardTitleView);
            }
        }
    }

    protected abstract
    @LayoutRes
    int getLayoutResId();

    protected abstract V getBaseView();
    protected abstract void createPresenter();

    @Nullable
    protected abstract P getPresenter();

    protected abstract void setupViews();

    public DfmRealm getDfmRealm() {
        return dfmRealm;
    }

    public DfmRetrofit getDfmRetrofit() {
        return dfmRetrofit;
    }
}
