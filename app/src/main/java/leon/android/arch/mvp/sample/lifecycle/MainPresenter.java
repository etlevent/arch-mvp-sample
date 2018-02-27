package leon.android.arch.mvp.sample.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;

import leon.android.arch.lifecycle.BaseLifecyclePresenter;

/**
 * Created by roothost on 2018/2/27.
 */

public class MainPresenter extends BaseLifecyclePresenter {
    @Override
    public void onAny(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {
        Log.d("Test", "owner=" + owner + ", event=" + event);
    }
}