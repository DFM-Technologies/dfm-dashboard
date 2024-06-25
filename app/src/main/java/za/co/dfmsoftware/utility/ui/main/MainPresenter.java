package za.co.dfmsoftware.utility.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * MAIN PRESENTER
 */
public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;
    @Override
    public void attach(@NonNull MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Nullable
    @Override
    public MainContract.View getView() {
        return this.view;
    }
}
