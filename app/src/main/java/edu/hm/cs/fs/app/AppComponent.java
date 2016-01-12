package edu.hm.cs.fs.app;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import edu.hm.cs.fs.app.domain.DataMigration;
import edu.hm.cs.fs.app.domain.DataService;
import edu.hm.cs.fs.app.domain.SchedulerProvider;
import edu.hm.cs.fs.app.ui.MainActivity;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    Context getContext();

    SharedPreferences getSharedPreferences();

    SchedulerProvider getSchedulerProvider();

    DataMigration getVersionManager();

    DataService getDataService();

    MainActivity inject(MainActivity activity);
}
