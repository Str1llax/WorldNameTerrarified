package git.str1llax.wnt.config;

import git.str1llax.wnt.WorldNameTerrarified;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConfigEntry<T extends Comparable<? super T>> {
    public final String key;
    private T value;
    public final T defaultValue;
    public String comment;
    public String translationKey;
    private boolean changed;
    private final boolean ranged;
    public final T min;
    public final T max;
    public final Class<T> clazz;

    ConfigEntry(Class<T> clazz, String key, String comment, T defaultValue) {
        this.key = key;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.comment = comment;
        this.translationKey = String.format("config.property.%s", key);
        this.changed = false;
        this.ranged = false;
        min = null;
        max = null;
        this.clazz = clazz;
    }

    ConfigEntry(Class<T> clazz, String key, String comment, T defaultValue, @Nonnull T min, @Nonnull T max) {
        this.key = key;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.comment = comment;
        this.translationKey = String.format("config.property.%s", key);
        this.changed = false;
        this.ranged = true;
        this.min = min;
        this.max = max;
        this.clazz = clazz;
    }

    ConfigEntry(ConfigEntry<T> entry) {
        this.key = entry.key;
        this.value = entry.value;
        this.defaultValue = entry.defaultValue;
        this.comment = entry.comment;
        this.translationKey = entry.translationKey;
        this.changed = entry.changed;
        this.ranged = entry.ranged;
        this.min = entry.min;
        this.max = entry.max;
        this.clazz = entry.clazz;
    }

    public boolean isRanged() {
        return this.ranged;
    }

    public boolean inRange() {
        return this.value.compareTo(this.min) >= 0 && this.value.compareTo(this.max) <= 0;
    }

    public boolean inRange(T value) {
        return value.compareTo(this.min) >= 0 && value.compareTo(this.max) <= 0;
    }

    public boolean inRangeParsing(String value) {
        T parsedValue = parseValue(value);
        return parsedValue.compareTo(this.min) >= 0 && parsedValue.compareTo(this.max) <= 0;
    }

    public boolean isChanged() {
        if (this.changed) {
            this.changed = false;
            return true;
        }
        return false;
    }

    public final void reset() {
        this.changed = !this.value.equals(this.defaultValue);
        this.value = defaultValue;
    }

    public final T get() {
        return this.value;
    }

    public final void set(ConfigEntry<T> entry) {
        this.changed = !this.value.equals(entry.value);
        this.value = entry.value;
    }

    public final void set(T value) {
        this.changed = !this.value.equals(value);
        this.value = value;
    }

    public final void setParsing(String value) {
        T newValue = parseValue(value);
        this.changed = !this.value.equals(newValue);
        this.value = newValue;
    }

    @SuppressWarnings("unchecked")
    public final T parseValue(String value) {
        String trimmed = value.trim();

        if (this.clazz == Integer.class) {
            return (T) Integer.valueOf(Integer.parseInt(trimmed));
        } else if (this.clazz == Double.class) {
            return (T) Double.valueOf(Double.parseDouble(trimmed));
        } else if (this.clazz == Long.class) {
            return (T) Long.valueOf(Long.parseLong(trimmed));
        } else if (this.clazz == Float.class) {
            return (T) Float.valueOf(Float.parseFloat(trimmed));
        } else if (this.clazz == Boolean.class) {
            return (T) Boolean.valueOf(Boolean.parseBoolean(trimmed));
        } else if (this.clazz == String.class) {
            return (T) trimmed;
        } else {
            WorldNameTerrarified.LOGGER.log(Level.ERROR, String.format("%s: Couldn't parse value (%s).", WorldNameTerrarified.MOD_NAME, value));
            return null;
        }
    }
}
