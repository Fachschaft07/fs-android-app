package edu.hm.cs.fs.app;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.ui.MainActivity;
import nz.bradcampbell.compartment.ComponentCacheApplication;

public class App extends ComponentCacheApplication {
    private AppComponent mComponent;

    public static AppComponent getAppComponent(@NonNull final MainActivity mainActivity) {
        App app = (App) mainActivity.getApplicationContext();
        if (app.mComponent == null) {
            app.mComponent = DaggerAppComponent.builder()
                    .appModule(app.getApplicationModule())
                    .build();
        }
        return app.mComponent;
    }

    public static void clearAppComponent(Context context) {
        App app = (App) context.getApplicationContext();
        app.mComponent = null;
    }

    protected AppModule getApplicationModule() {
        return new AppModule(this);
    }
}
