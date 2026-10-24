package io.freemytv;

public final class TvIdentityTest {
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        check("TCL".equals(TvIdentity.brand("TCL", "Google")), "prefer the TV manufacturer over the platform brand");
        check("Sony".equals(TvIdentity.brand("Sony Corporation", "BRAVIA")), "normalize Sony branding");
        check("TV".equals(TvIdentity.brand("generic", "unknown")), "use a neutral label when the brand is unknown");
        check("Google TV".equals(TvIdentity.platform(true)), "detect the Google TV launcher");
        check("Android TV".equals(TvIdentity.platform(false)), "fall back to the Android TV platform");
        System.out.println("TV identity checks passed.");
    }
}
