package com.mobila.project.today.model.dataProviding.dataAccess;

import android.database.sqlite.SQLiteDatabase;

public class ParentDataAccessImpl implements ParentDataAccess {
    SQLiteDatabase database;

    @Override
    public void openDbConnection(SQLiteDatabase database) {
        this.database = database;
    }
}
