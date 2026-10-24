"""Source guards for PR #222; these do not replace on-device remote tests."""
from pathlib import Path
root = Path(__file__).resolve().parents[1]
src = root / 'app/src/io/freemytv'
home = (src / 'HomeActivity.java').read_text(encoding='utf-8')
view = (src / 'HomeView.java').read_text(encoding='utf-8')
apps = (src / 'HomeApps.java').read_text(encoding='utf-8')
manifest = (root / 'app/AndroidManifest.xml').read_text(encoding='utf-8')
assert 'new HomeView(this)' in home
assert 'new NativeDashboardView' not in home
assert 'drawMoiTile' in view and 'drawMoiSurface' in view
assert 'if (!moi() && (tileRects[i].right' in view, 'Moi grid must not be culled by the empty dock rectangle'
assert 'DeviceNotificationService' in manifest
assert 'HomeMode.restoreFreeDreamAfterAmbient(this)' in home
assert 'if (moi()) return;' in view[view.index('private void revealShelf'):], 'grid must never scroll horizontally'
assert '"Android TV", "android"' in apps
for path in src.glob('*.java'):
    s = path.read_text(encoding='utf-8')
    assert '<<<<<<< ours' not in s and '>>>>>>> theirs' not in s, path
print('PR #222 integration source checks passed.')
