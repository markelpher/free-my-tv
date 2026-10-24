"""Identity/version guard for the locally branded TCL build."""
from pathlib import Path
import xml.etree.ElementTree as ET
root = Path(__file__).resolve().parents[1]
manifest = ET.parse(root / 'app/AndroidManifest.xml').getroot()
a = '{http://schemas.android.com/apk/res/android}'
assert manifest.attrib['package'] == 'io.freemytv'
assert int(manifest.attrib[a+'versionCode']) > 2, 'updates need a higher versionCode than the installed app'
application = manifest.find('application')
assert application is not None
assert application.attrib[a+'label'] == 'Android TV', 'personal app name must be Android TV'
assert application.attrib[a+'icon'] == '@drawable/icon', 'app icon must resolve to replacement graphic'
assert (root / 'app/res/drawable-nodpi/icon.png').is_file()
print('Android TV identity checks passed.')
