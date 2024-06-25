package za.co.dfmsoftware.utility.service;

import android.annotation.SuppressLint;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import za.co.dfmsoftware.utility.BuildConfig;
import za.co.dfmsoftware.utility.Logger;
import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.model.model.User;
import za.co.dfmsoftware.utility.realm.DfmRealm;

public class DfmRetrofit implements ApiInterface {

    private static final String TAG = DfmRetrofit.class.getSimpleName();

    private static volatile DfmRetrofit INSTANCE;
    private ApiInterface dfmEndpoint;
    private DfmRealm dfmRealm;
    private Disposable disposable;

    private DfmRetrofit() {} // Private constructor

    public static DfmRetrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (DfmRetrofit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DfmRetrofit();
                }
            }
        }
        return INSTANCE;
    }

    /* REALM DB */
    public void init(@NonNull DfmRealm dfmRealm) {
        this.dfmRealm = dfmRealm;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.dfmEndpoint = retrofit.create(ApiInterface.class);
    }

    /* USERS */
    @Override
    public Observable<User> authenticateUser(String grantType, String username, String password) {
        return Observable.create(e -> {
            disposable = this.dfmEndpoint.authenticateUser(grantType, username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(user -> {
                        dfmRealm.addOrUpdateUser(user);
                        e.onNext(user);
                        e.onComplete();
                    }, throwable -> {
                        Logger.e(TAG, "Authenticate User", throwable);
                        e.onError(throwable);
                    });
        });
    }

    @Override
    public Observable<User> refreshAuthorizationToken(String grantType, String refreshToken) {
        return Observable.create(e -> this.dfmEndpoint.refreshAuthorizationToken(grantType, refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(user -> {
                    e.onNext(user);
                    e.onComplete();
                }, e::onError));
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /* PROBES */
    public Observable<List<Probe>> getProbes() {
        return this.getProbes(null);
    }

    @Override
    public Observable<List<Probe>> getProbes(String authorization){
        return Observable.create(e -> this.refreshAuthTokenIfNeeded()
                .flatMap(aBoolean -> this.dfmEndpoint.getProbes(this.getAuthorizationHeader()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(probes -> {
                    this.dfmRealm.addOrUpdateProbes(probes);
                    e.onNext(probes);
                    e.onComplete();
                }, throwable -> {
                    Logger.e(TAG, "GET PROBES", throwable);
                    e.onError(throwable);
                }));
    }

    private String getAuthorizationHeader() {
        return String.format("bearer %s", this.dfmRealm.getCurrentUser().getAccessToken());
    }

    @SuppressLint("CheckResult")
    private Observable<Boolean> refreshAuthTokenIfNeeded() {
        return Observable.create(e -> {
            User user = this.dfmRealm.getCurrentUser();

            if((System.currentTimeMillis() - user.getTokenExpireTime()) > 0) {
                this.refreshAuthorizationToken(GrantType.REFRESH_TOKEN.getValue(), user.getRefreshToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(refreshUser -> {
                            dfmRealm.addOrUpdateUser(refreshUser);
                            e.onNext(true);
                            e.onComplete();
                        }, e::onError);

                return;
            }

            e.onNext(true);
            e.onComplete();
        });
    }
}

