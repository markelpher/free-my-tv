package io.freemytv;

/** The splash is TCL-only and consumed once per launcher process, never on each Home press. */
public final class StartupBrandTest {
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        check(new StartupBrand().take("TCL", "TCL", true), "TCL manufacturer shows splash");
        check(new StartupBrand().take(" tcl ", "unknown", true), "manufacturer comparison ignores case and whitespace");
        check(new StartupBrand().take("unknown", "TCL", true), "TCL brand is accepted");
        check(!new StartupBrand().take("Sony", "BRAVIA", true), "Sony must not see TCL branding");
        check(!new StartupBrand().take("Amazon", "Amazon", true), "Fire TV must not see TCL branding");
        check(!new StartupBrand().take(null, null, true), "missing identity must not show TCL branding");
        check(!new StartupBrand().take("NotTCL", "TCL-like", true), "partial identity must not match");
        check(!new StartupBrand().take("TCL", "TCL", false), "disabled fallback must not duplicate native boot");
        check(StartupBrand.supports("TCL", "TCL"), "show fallback setting only on TCL");
        check(!StartupBrand.supports("Sony", "BRAVIA"), "hide fallback setting on other brands");
        StartupBrand once = new StartupBrand();
        check(once.take("TCL", "TCL", true), "first cold start shows splash");
        check(!once.take("TCL", "TCL", true), "recreation and later Home presses must not replay splash");
        System.out.println("TCL startup brand checks passed.");
    }
}
