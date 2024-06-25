package za.co.dfmsoftware.utility.ui.base;

import androidx.annotation.NonNull;

import javax.annotation.Nullable;

public interface BasePresenter<V extends BaseView> {
    void attach(@NonNull V view);

    void detach();

    @Nullable
    V getView();
}
