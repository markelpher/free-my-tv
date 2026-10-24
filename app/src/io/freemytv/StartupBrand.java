package io.freemytv;

/** Small, Android-independent gate so non-TCL devices never receive manufacturer branding. */
final class StartupBrand {
    private boolean shown;

    boolean take(String manufacturer, String brand, boolean fallbackEnabled) {
        if (!fallbackEnabled || shown || !supports(manufacturer, brand)) return false;
        shown = true;
        return true;
    }

    static boolean supports(String manufacturer, String brand) {
        return isTcl(manufacturer) || isTcl(brand);
    }

    private static boolean isTcl(String value) {
        return value != null && "TCL".equalsIgnoreCase(value.trim());
    }
}
