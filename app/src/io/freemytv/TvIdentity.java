package io.freemytv;

/** Automatic TV-brand and launcher identity for the Settings welcome panel. */
final class TvIdentity {
    private TvIdentity() {}

    static String brand(String manufacturer, String brand) {
        String value = firstKnown(manufacturer, brand);
        if (value.isEmpty()) value = firstKnown(brand, manufacturer);
        String lower = value.toLowerCase(java.util.Locale.ROOT);
        if (lower.contains("tcl")) return "TCL";
        if (lower.contains("sony") || lower.contains("bravia")) return "Sony";
        if (lower.contains("xiaomi") || lower.equals("mi")) return "Xiaomi";
        if (lower.contains("hisense")) return "Hisense";
        if (lower.contains("philips")) return "Philips";
        if (lower.contains("panasonic")) return "Panasonic";
        if (lower.contains("samsung")) return "Samsung";
        if (lower.equals("lg") || lower.contains("lge")) return "LG";
        if (lower.contains("amazon")) return "Amazon";
        if (lower.contains("google")) return "Google";
        return value.isEmpty() || lower.equals("unknown") || lower.equals("generic") ? "TV" : value;
    }

    private static String firstKnown(String first, String second) {
        if (known(first)) return first.trim();
        return known(second) ? second.trim() : "";
    }

    private static boolean known(String value) {
        return value != null && !value.trim().isEmpty() && !value.trim().equalsIgnoreCase("unknown")
                && !value.trim().equalsIgnoreCase("generic");
    }

    static String platform(boolean googleTvLauncher) { return googleTvLauncher ? "Google TV" : "Android TV"; }
}
