package edu.hm.cs.fs.app.database;

/**
 * Created by FHellman on 12.08.2015.
 */
public class HomeModel implements IModel {
    private static HomeModel mInstance;

    private HomeModel() {
    }

    public static HomeModel getInstance() {
        if(mInstance == null) {
            mInstance = new HomeModel();
        }
        return mInstance;
    }
}
