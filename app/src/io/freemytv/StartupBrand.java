package io.freemytv;

/** Small, Android-independent gate so non-TCL devices never receive manufacturer branding. */
final class StartupBrand {
    private boolean shown;

    boolean take(String manufacturer, String brand) {
        if (shown || !(isTcl(manufacturer) || isTcl(brand))) return false;
        shown = true;
        return true;
    }

    private static boolean isTcl(String value) {
        return value != null && "TCL".equalsIgnoreCase(value.trim());
    }
}
