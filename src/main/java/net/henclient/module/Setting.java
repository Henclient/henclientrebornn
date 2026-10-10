package net.henclient.module;

public class Setting<T> {
private final String name;
private T value;
private final T defaultValue;
private final double min;
private final double max;
public Setting(String name, T defaultValue) {
    this(name, defaultValue, 0, 0);
}

public Setting(String name, T defaultValue, double min, double max) {
    this.name = name;
    this.value = defaultValue;
    this.defaultValue = defaultValue;
    this.min = min;
    this.max = max;
}

public String getName() {
    return name;
}

public T getValue() {
    return value;
}

public void setValue(T value) {
    this.value = value;
}

public T getDefaultValue() {
    return defaultValue;
}

public double getMin() {
    return min;
}

public double getMax() {
    return max;
}
}
