"""Regression guards for the Moi header and Brazilian Portuguese UI strings."""
import json
from pathlib import Path
root = Path(__file__).resolve().parents[1]
source = (root / 'app/src/io/freemytv/HomeView.java').read_text(encoding='utf-8')
header = source[source.index('private void drawMoiDashboard'):source.index('private void drawMoiDashboard') + 850]
assert 'c.drawText(clockText, x, 106 * k, moiTime);' in header, 'clock must be first on the home header'
assert 'x + moiTime.measureText(clockText) + 26 * k' in header, 'date must follow the clock with a measured gap'
pt = json.loads((root / 'app/assets/i18n/pt.json').read_text(encoding='utf-8'))
expected = {'All Apps': 'Todos os aplicativos', 'Apps': 'Aplicativos', 'Settings': 'Configurações',
            'Home Settings': 'Configurações da tela inicial', 'TV Settings': 'Configurações da TV',
            'Series': 'Séries', 'Sunrise {time}': 'Nascer do sol {time}'}
for key, value in expected.items():
    assert pt[key] == value, (key, pt[key])
print('Home header and PT-BR regression checks passed.')
