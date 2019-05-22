package com.eugeneskachko.notes.dataBase;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DataBaseClient {

    private Context context;
    private static DataBaseClient instance;
    private AppDataBase appDataBase;

    private DataBaseClient(Context context) {
        this.context = context;
        appDataBase = Room.databaseBuilder(context, AppDataBase.class, "Notes").fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DataBaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseClient(context);
        }

        return instance;
    }

    public AppDataBase getAppDataBase() {
        return appDataBase;
    }
}
