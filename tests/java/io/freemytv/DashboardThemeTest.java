package io.freemytv;

/** Small JVM-only regression checks for the TV dashboard geometry and palette selection. */
public final class DashboardThemeTest {
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    private static void near(float actual, float expected, String message) {
        check(Math.abs(actual - expected) < 0.1f, message + ": expected " + expected + ", got " + actual);
    }

    public static void main(String[] args) {
        check("moi-dark".equals(DashboardTheme.normalize("moi-dark")), "accept the Moi Dark theme");
        check("moi-light".equals(DashboardTheme.normalize("moi-light")), "accept the Moi Light theme");
        check("moi-dark".equals(DashboardTheme.normalize("moi")), "migrate the early Moi theme to Moi Dark");
        check("light".equals(DashboardTheme.normalize("light")) && "dark".equals(DashboardTheme.normalize("dark")),
                "keep existing light and dark themes");
        check("auto".equals(DashboardTheme.normalize("unknown")), "unknown theme falls back to automatic");
        check("auto".equals(DashboardTheme.normalize(null)), "missing theme falls back to automatic");
        check(DashboardTheme.isDashboard("moi-light") && DashboardTheme.isDashboard("moi-dark"),
                "both Moi palettes select the dashboard layout");
        check(!DashboardTheme.isDashboard("auto"), "classic automatic theme keeps the existing layout");
        check(DashboardTheme.isLight("moi-light", true), "Moi Light does not become dark at night");
        check(!DashboardTheme.isLight("moi-dark", false), "Moi Dark does not become light by day");
        check(DashboardTheme.isLight("auto", false) && !DashboardTheme.isLight("auto", true),
                "automatic theme follows day and night");
        System.out.println("Dashboard theme checks passed.");
    }
}
