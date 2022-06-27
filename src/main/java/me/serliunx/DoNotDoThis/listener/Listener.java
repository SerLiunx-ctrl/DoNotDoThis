package me.serliunx.DoNotDoThis.listener;

public abstract class Listener implements org.bukkit.event.Listener {
    private final boolean built_in;
    private boolean enable;

    public Listener(boolean built_in, boolean enable) {
        this.built_in = built_in;
        this.enable = enable;
    }

    /**
     * Check if the listener is a built-in event listener
     * @return true if it is a built-in event listener.
     */
    public boolean isBuilt_in() {
        return built_in;
    }

    /**
     * Check if the listener is enabled
     * @return ture if is enabled.
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Set the listener to enable or disable
     * @param enable true of false.
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
