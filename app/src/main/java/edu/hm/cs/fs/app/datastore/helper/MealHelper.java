package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.cs.fs.app.datastore.model.Meal;
import edu.hm.cs.fs.app.datastore.model.constants.Additive;
import edu.hm.cs.fs.app.datastore.model.constants.MealType;
import edu.hm.cs.fs.app.datastore.model.constants.StudentWorkMunich;
import edu.hm.cs.fs.app.datastore.model.impl.MealImpl;
import edu.hm.cs.fs.app.datastore.web.MealFetcher;
import edu.hm.cs.fs.app.util.PrefUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 11.03.2015.
 */
public class MealHelper extends BaseHelper implements Meal {
    private static final Pattern ADDITIVES_PATTERN = Pattern.compile("\\(([0-9,]+)");
    private static final Pattern FOOD_PART_PATTERN = Pattern.compile("\\(([RS,]+)");
    private static final Pattern FOOD_TYPE_PATTERN = Pattern.compile("\\(([vf])");
    private final Date date;
    private final MealType type;
    private final String name;
    private final List<Additive> additives;

    private MealHelper(final Context context, MealImpl meal) {
        super(context);
        date = meal.getDate();
        MealType typeRaw = MealType.of(meal.getType());
        additives = new ArrayList<>();
        String nameRaw = meal.getDescription();
        nameRaw = nameRaw.replaceAll("\\sfleischlos", "");
        nameRaw = nameRaw.replaceAll("\\svegan", "");
        nameRaw = nameRaw.replaceAll("\\smit Fleisch", "");


        Matcher matcher = FOOD_PART_PATTERN.matcher(nameRaw);
        if(matcher.find()) {
            String group = matcher.group(1);
            nameRaw = nameRaw.replaceAll("\\([RS,]+\\)", "");
            if(!TextUtils.isEmpty(group)) {
                if(group.contains("R")) {
                    additives.add(Additive.BEEF);
                }
                if(group.contains("S")) {
                    additives.add(Additive.PIG);
                }
            }
        }

        matcher = ADDITIVES_PATTERN.matcher(nameRaw);
        if(matcher.find()) {
            final String group = matcher.group(1);
            nameRaw = nameRaw.replaceAll("\\([0-9,]+\\)", "");
            if(!TextUtils.isEmpty(group)) {
                final String additiveStr = group.trim();
                final List<String> strings = Arrays.asList(additiveStr.split(","));
                for (String string : strings) {
                    Additive additive = Additive.of(string);
                    if (additive != null) {
                        additives.add(additive);
                    }
                }
            }
        }

        matcher = FOOD_TYPE_PATTERN.matcher(nameRaw);
        if(matcher.find()) {
            final String group = matcher.group(1);
            nameRaw = nameRaw.replaceAll("\\([vf]\\)", "");
            if(!TextUtils.isEmpty(group)) {
                if(group.contains("v")) {
                    typeRaw = MealType.VEGAN;
                }
                if(group.contains("f")) {
                    typeRaw = MealType.MEATLESS;
                }
            }
        }
        name = nameRaw;
        type = typeRaw;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public MealType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Additive> getAdditives() {
        return additives;
    }

    public static void listAll(Context context, StudentWorkMunich studentWorkMunich, Callback<List<Meal>> callback) {
        PrefUtils.setUpdateInterval(context, MealFetcher.class, TimeUnit.MILLISECONDS.convert(3l, TimeUnit.DAYS));

        listAll(context, new MealFetcher(context, studentWorkMunich), MealImpl.class, callback, new OnHelperCallback<Meal, MealImpl>(MealImpl.class) {
            @Override
            public Meal createHelper(final Context context, final MealImpl meal) {
                return new MealHelper(context, meal);
            }

            @Override
            public void copyToRealmOrUpdate(final Realm realm, final MealImpl meal) {
                realm.copyToRealmOrUpdate(meal);
            }
        });
    }
}
