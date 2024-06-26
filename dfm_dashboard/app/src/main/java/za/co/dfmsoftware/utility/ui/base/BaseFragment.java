package za.co.dfmsoftware.utility.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.service.DfmRetrofit;

public abstract class BaseFragment <P extends BasePresenter<V>, V extends BaseView> extends Fragment {
    private DfmRealm dfmRealm;
    private DfmRetrofit dfmRetrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.dfmRealm = DfmRealm.getInstance();
        this.dfmRetrofit = DfmRetrofit.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        P presenter = getPresenter();
        if(presenter != null) {
            presenter.detach();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(this.getLayoutResId(), container, false);

        this.createPresenter();

        P presenter = this.getPresenter();
        if(presenter != null){
            presenter.attach(this.getBaseView());
        }

        this.setupViews(view);
        return  view;
    }

    //todo ?===
    public boolean setToolbarTitle(@NonNull TextView title) { return false; }

    protected abstract
    @LayoutRes
    int getLayoutResId();

    protected abstract V getBaseView();

    @Nullable
    protected abstract P getPresenter();

    protected abstract void setupViews(View view);

    protected abstract void createPresenter();

    public DfmRealm getDfmRealm() { return  dfmRealm; }

    public DfmRetrofit getDfmRetrofit() { return dfmRetrofit; }

}
