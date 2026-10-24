package io.freemytv;

/** The splash is TCL-only and consumed once per launcher process, never on each Home press. */
public final class StartupBrandTest {
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        check(new StartupBrand().take("TCL", "TCL"), "TCL manufacturer shows splash");
        check(new StartupBrand().take(" tcl ", "unknown"), "manufacturer comparison ignores case and whitespace");
        check(new StartupBrand().take("unknown", "TCL"), "TCL brand is accepted");
        check(!new StartupBrand().take("Sony", "BRAVIA"), "Sony must not see TCL branding");
        check(!new StartupBrand().take("Amazon", "Amazon"), "Fire TV must not see TCL branding");
        check(!new StartupBrand().take(null, null), "missing identity must not show TCL branding");
        check(!new StartupBrand().take("NotTCL", "TCL-like"), "partial identity must not match");
        StartupBrand once = new StartupBrand();
        check(once.take("TCL", "TCL"), "first cold start shows splash");
        check(!once.take("TCL", "TCL"), "recreation and later Home presses must not replay splash");
        System.out.println("TCL startup brand checks passed.");
    }
}
