package net.henclient.module;

public enum Category {
HUD("HUD & Info"),
EXPLORATION("Exploration"),
COMBAT("Combat Utilities");
private final String displayName;

Category(String displayName) {
    this.displayName = displayName;
}

public String getDisplayName() {
    return displayName;
}
}
