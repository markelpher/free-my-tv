"""No market widgets or price requests, including when old settings were enabled."""
from pathlib import Path
root = Path(__file__).resolve().parents[1] / 'app/src/io/freemytv'
prefs = (root / 'Prefs.java').read_text(encoding='utf-8')
data = (root / 'HomeData.java').read_text(encoding='utf-8')
settings = (root / 'SettingsActivity.java').read_text(encoding='utf-8')
assert 'static boolean quotes() { return false; }' in prefs, 'legacy saved preference must not enable crypto'
assert 'static int[] active() { return new int[0]; }' in data, 'no active coin widgets on any screen'
assert 'api.binance' not in data and 'getPrices()' not in data, 'price endpoints and requests must be removed'
assert 'Ui.tr("Quotes widget")' not in settings and 'l.add("Bitcoin")' not in settings, 'crypto settings must be removed'
assert 'setQuotes(' not in settings and 'setCoin(' not in settings
print('No-crypto Android regression checks passed.')
