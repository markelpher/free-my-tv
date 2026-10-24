from pathlib import Path
source = (Path(__file__).resolve().parents[1] / 'app/src/io/freemytv/SettingsActivity.java').read_text(encoding='utf-8')
assert 'drawTvIdentity(c, cx)' in source
assert 'Symbols.draw(getContext(), c, "android", x' in source
assert 'TvIdentity.brand(android.os.Build.MANUFACTURER, android.os.Build.BRAND)' in source
assert 'TvIdentity.platform(hasPackage("com.google.android.apps.tv.launcherx"))' in source
assert 'Your TV\'s home screen and screensaver' not in source
prefs = (Path(__file__).resolve().parents[1] / 'app/src/io/freemytv/Prefs.java').read_text(encoding='utf-8')
assert 'getBoolean("tclStartupFallback", false)' in prefs, 'native boot must not be duplicated by default'
assert 'l.add(Ui.tr("TCL startup fallback"));' in source
workflow = (Path(__file__).resolve().parents[1] / '.github/workflows/release.yml').read_text(encoding='utf-8')
assert '--title "Free My TV $GITHUB_REF_NAME"' in workflow
assert 'if (Prefs.moiTheme()) c.drawColor(0xFF09090B)' in source
print('Settings identity checks passed.')
