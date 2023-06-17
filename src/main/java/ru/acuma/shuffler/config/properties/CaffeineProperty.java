package ru.acuma.shuffler.config.properties;

import java.io.Serializable;

public class CaffeineProperty implements Serializable {
    public String value = "";

    public CaffeineProperty(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ValueObject{" +
                "value='" + value + '\'' +
                '}';
    }
}
