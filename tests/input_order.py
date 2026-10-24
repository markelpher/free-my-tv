"""Regression guard for TV first, then the existing ordered HDMI rail."""
from pathlib import Path

source = (Path(__file__).resolve().parents[1] / 'app/src/io/freemytv/HomeApps.java').read_text(encoding='utf-8')
start = source.index('static List<Item> allInputs(Context c)')
end = source.index('\n    private static List<Item> inputs(Context c)', start)
body = source[start:end]
assert 'items.add(0, live);' in body, 'Live TV must be inserted before every HDMI input'
assert 'items.addAll(inputs(c, true));' in body, 'preserve all HDMI inputs and their existing order'
assert 'live.connected = liveTv(c) != null;' in body, 'preserve tuner availability detection'
assert body.count('items.add(0, live);') == 1, 'do not duplicate Live TV'
print('TV-first input order source checks passed.')
