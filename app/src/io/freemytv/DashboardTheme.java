package io.freemytv;

/** Names and palette rules for the existing and dashboard home layouts. */
final class DashboardTheme {
    private DashboardTheme() {}

    static String normalize(String value) {
        if ("moi".equals(value)) return "moi-dark";
        if ("light".equals(value) || "dark".equals(value)
                || "moi-light".equals(value) || "moi-dark".equals(value)
                || "orbit-light".equals(value) || "orbit-dark".equals(value)
                || "studio-light".equals(value) || "studio-dark".equals(value)) return value.startsWith("studio-")
                        ? value.replace("studio-", "orbit-") : value;
        return "auto";
    }

    static boolean isDashboard(String value) {
        return "moi-light".equals(value) || "moi-dark".equals(value)
                || "orbit-light".equals(value) || "orbit-dark".equals(value)
                || "studio-light".equals(value) || "studio-dark".equals(value);
    }

    static boolean isLight(String value, boolean night) {
        if ("moi-light".equals(value) || "orbit-light".equals(value) || "studio-light".equals(value) || "light".equals(value)) return true;
        if ("moi-dark".equals(value) || "orbit-dark".equals(value) || "studio-dark".equals(value) || "dark".equals(value)) return false;
        return !night;
    }
}
