package dev.openlanport;

/**
 * Holds the port the player typed into the "Open to LAN" screen.
 * Java 8 compatible.
 */
public final class PortHolder {
    public static final int MIN_PORT = 1024;
    public static final int MAX_PORT = 65535;

    /** Text remembered between visits to the screen (this session only). */
    public static String lastText = "";

    /** Port to use for the next LAN share, or -1 for "pick automatically". */
    private static int pending = -1;

    private PortHolder() {
    }

    /** Empty means "automatic" and is considered valid. */
    public static boolean isValid(String text) {
        return parse(text) != -2;
    }

    /** Returns the port, -1 if empty/automatic, or -2 if invalid. */
    private static int parse(String text) {
        if (text == null || text.isEmpty()) {
            return -1;
        }
        try {
            int value = Integer.parseInt(text);
            if (value < MIN_PORT || value > MAX_PORT) {
                return -2;
            }
            return value;
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    /** Called when the player clicks "Start LAN World". */
    public static void apply(String text) {
        lastText = text == null ? "" : text;
        int parsed = parse(lastText);
        pending = parsed > 0 ? parsed : -1; // invalid -> fall back to automatic
    }

    /** Called by the HttpUtil mixin. Returns the port once, then resets. */
    public static int consume() {
        int value = pending;
        pending = -1;
        return value;
    }
}
