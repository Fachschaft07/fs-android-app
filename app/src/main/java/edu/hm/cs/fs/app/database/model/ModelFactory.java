package edu.hm.cs.fs.app.database.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The Model Factory creates a model class as a singleton after the first call of a model.
 *
 * @author Fabio
 */
public final class ModelFactory {

    /**
     * Contains every model instance.
     */
    private static final Map<Class<? extends IModel>, IModel> INSTANCES = new HashMap<>();

    private ModelFactory() {
    }

    /**
     * Get the Blackboard model.
     *
     * @return the model.
     */
    @NonNull
    public static BlackBoardModel getBlackboard() {
        return getInstance(new BlackBoardModel());
    }

    /**
     * Get the FS model.
     *
     * @return the model.
     */
    @NonNull
    public static FsModel getFs() {
        return getInstance(new FsModel());
    }

    /**
     * Get the Home model.
     *
     * @return the model.
     */
    @NonNull
    public static HomeModel getHome() {
        return getInstance(new HomeModel());
    }

    /**
     * Get the Info model.
     *
     * @param context for android specific stuff.
     * @return the model.
     */
    @NonNull
    public static InfoModel getInfo(@NonNull final Context context) {
        return getInstance(new InfoModel(context));
    }

    /**
     * Get the Job model.
     *
     * @return the model.
     */
    @NonNull
    public static JobModel getJob() {
        return getInstance(new JobModel());
    }

    /**
     * Get the Meal model.
     *
     * @return the model.
     */
    @NonNull
    public static MealModel getMeal() {
        return getInstance(new MealModel());
    }

    /**
     * Get the Public Transport model.
     *
     * @return the model.
     */
    @NonNull
    public static PublicTransportModel getPublicTransport() {
        return getInstance(new PublicTransportModel());
    }

    /**
     * Get the Room model.
     *
     * @return the model.
     */
    @NonNull
    public static RoomSearchModel getRoom() {
        return getInstance(new RoomSearchModel());
    }

    /**
     * Get the Timetable model.
     *
     * @return the model.
     */
    @NonNull
    public static TimetableModel getTimetable(@NonNull final Context context) {
        return getInstance(new TimetableModel(context));
    }

    /**
     * Get the Lost & Found model.
     *
     * @return the model.
     */
    @NonNull
    public static LostFoundModel getLostFound() {
        return getInstance(new LostFoundModel());
    }

    /**
     * Get the Calendar model.
     *
     * @return the model.
     */
    @NonNull
    public static CalendarModel getCalendar() {
        return getInstance(new CalendarModel());
    }

    /**
     * Get the Module model.
     *
     * @return the model.
     */
    @NonNull
    public static ModuleModel getModule() {
        return getInstance(new ModuleModel());
    }

    /**
     * Get or save an instance of the model.
     *
     * @param model to get an instance of.
     * @param <T>   type of the model.
     * @return the model instance.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    private static <T extends IModel> T getInstance(@NonNull final T model) {
        if (!INSTANCES.containsKey(model.getClass())) {
            INSTANCES.put(model.getClass(), model);
        }
        return (T) INSTANCES.get(model.getClass());
    }
}
