package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * Created by Fabio on 11.03.2015.
 */
public enum MealType {
    MEAT("fleisch"), MEATLESS("fleischlos"), VEGAN("vegan");

    private final String mKey;

    private MealType(String key) {
        mKey = key;
    }

    @Override
    public String toString() {
        return mKey;
    }

    public static MealType of(String key) {
        for (MealType mealType : values()) {
            if(mealType.toString().equalsIgnoreCase(key)) {
                return mealType;
            }
        }
        throw new IllegalArgumentException("This is not a valid meal type: "
                + key);
    }

    public static MealType find(String sign) {
        if("f".equalsIgnoreCase(sign)) {
            return MEATLESS;
        } else if("v".equalsIgnoreCase(sign)) {
            return VEGAN;
        }
        return null;
    }
}
