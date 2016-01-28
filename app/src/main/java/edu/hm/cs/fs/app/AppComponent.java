package edu.hm.cs.fs.app;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import edu.hm.cs.fs.app.service.BlackboardNotificationService;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.widgets.NextLessonWidget;
import edu.hm.cs.fs.domain.DataServiceMigration;
import edu.hm.cs.fs.domain.DataService;
import edu.hm.cs.fs.domain.SchedulerProvider;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    Context getContext();

    SharedPreferences getSharedPreferences();

    SchedulerProvider getSchedulerProvider();

    DataServiceMigration getVersionManager();

    DataService getDataService();

    MainActivity inject(MainActivity mainActivity);

    BlackboardNotificationService inject(BlackboardNotificationService service);

    NextLessonWidget inject(NextLessonWidget widget);
}
