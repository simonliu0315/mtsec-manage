package com.cht.network.monitoring.commons.base;

import java.util.Locale;

import org.jvnet.localizer.Localizable;


public abstract class LocalizableStatusCode implements StatusCode {

    private static final long serialVersionUID = 5276716138470192688L;

    private final Localizable localizable;

    @Override
    public String getName() {
        return localizable.getKey();
    }

    public LocalizableStatusCode(Localizable localizable) {
        this.localizable = localizable;
    }

    public String getMessage() {
        return this.localizable.toString();
    }

    public String getMessage(Locale locale) {
        return this.localizable.toString(locale);
    }

    @Override
    public String toString() {
        return getName() + " " + getMessage();
    }

    public String toString(Locale locale) {
        return getName() + " " + getMessage(locale);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((localizable == null || localizable.getKey() == null) ? 0
                        : /* 只算 key... 勉強合理吧? */localizable.getKey().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocalizableStatusCode other = (LocalizableStatusCode) obj;
        if (localizable == null) {
            if (other.localizable != null)
                return false;
        } else if (/* 只比對 key... 勉強算合理吧? */!localizable.getKey().equals(other.localizable.getKey()))
            return false;
        return true;
    }
}
