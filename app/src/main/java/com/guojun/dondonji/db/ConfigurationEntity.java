package com.guojun.dondonji.db;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.guojun.dondonji.model.Configuration;

@Entity(tableName = "configurations")
public class ConfigurationEntity implements Configuration {

    public ConfigurationEntity(@NonNull String name, String value) {
        this.name = name;
        this.value = value;
    }

    @PrimaryKey
    @NonNull
    private String name;

    private String value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }




}
