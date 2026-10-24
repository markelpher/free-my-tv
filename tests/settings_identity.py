from pathlib import Path
source = (Path(__file__).resolve().parents[1] / 'app/src/io/freemytv/SettingsActivity.java').read_text(encoding='utf-8')
assert 'Symbols.draw(getContext(), c, "android", cx' in source
assert 'c.drawText("Android TV", cx' in source
assert 'if (Prefs.moiTheme()) c.drawColor(0xFF09090B)' in source
print('Settings identity checks passed.')
