package ext.android.arch.mvp.sample;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ext.android.arch.lifecycle.rxjava2.RxLifecycleObserver;
import ext.android.arch.mvp.sample.dagger2.DaggerMVPComponent;
import ext.android.arch.mvp.sample.dagger2.MVPComponent;
import ext.android.arch.mvp.sample.dagger2.MVPModule;
import ext.android.arch.mvp.sample.lifecycle.MainObserver;
import ext.android.arch.mvp.sample.mvp.MainPresenter;
import ext.android.arch.mvp.sample.network.MovieEntity;
import ext.android.arch.mvp.sample.network.api.NetworkApi;
import ext.android.dagger2.DaggerActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    MainPresenter mainPresenter;
    @Inject
    NetworkApi networkApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MVPComponent component = DaggerMVPComponent.builder().mVPModule(new MVPModule(getLifecycle())).build();
        component.inject(this);
        RxLifecycleObserver presenter = new MainObserver(getLifecycle());
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(presenter.bindUntilEvent(Lifecycle.Event.ON_STOP))
                .subscribe(event -> Log.e("Test", "activity lifecycle  current event: " + event));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv:
                startActivity(new Intent(this, DaggerActivity.class));
                break;
            case R.id.btn:
                networkApi.movieInTheaters(1, 20)
                        .delay(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(mainPresenter.bindUntilEvent(Lifecycle.Event.ON_STOP))
                        .subscribe(new DefaultObserver<MovieEntity>() {
                            @Override
                            public void onNext(MovieEntity movieEntity) {
                                Log.d("Test", "onNext=" + movieEntity);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Test", "onError=" + e);
                            }

                            @Override
                            public void onComplete() {
                                Log.v("Test", "onComplete=");
                            }
                        });
                break;

        }
    }
}
