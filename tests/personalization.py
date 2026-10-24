"""Regression guards for the theme presets and theme-aware personalization controls."""
from pathlib import Path

root = Path(__file__).resolve().parents[1]
src = root / 'app/src/io/freemytv'
settings = (src / 'SettingsActivity.java').read_text(encoding='utf-8')
apps = (src / 'HomeApps.java').read_text(encoding='utf-8')
prefs = (src / 'Prefs.java').read_text(encoding='utf-8')

assert 'PERSONALIZATION' in settings and 'Ui.tr("Personalization")' in settings
assert 'CLASSIC_THEMES = {"auto", "light", "dark"}' in settings
assert 'MOI_THEMES = {"moi-dark", "moi-light"}' in settings
assert 'ORBIT_THEMES = {"orbit-dark", "orbit-light"}' in settings
assert '"classic", "aurora", "ocean", "sunset", "forest", "ember", "lavender", "midnight"' in settings
assert 'for (String theme : themes) for (String gradient : GRADIENTS)' in settings
for gradient in ('forest', 'ember', 'lavender', 'midnight'):
    assert f'"{gradient}".equals(style)' in (src / 'Shaders.java').read_text(encoding='utf-8')
assert 'Prefs.setHomeGradient(parts[1])' in settings and 'Prefs.setGradient(parts[1])' in settings
assert 'Prefs.previewThemePreset(parts[0], parts[1])' in settings
assert 'Prefs.moiTheme() ? MOI_TOP_DEFAULT : TOP_DEFAULT' in apps
assert 'setMoiTopButtons(shelfOrder)' in settings
assert 'previewThemePreset(String theme, String gradient)' in prefs
assert 'static String homeDesign()' in prefs and 'previewHomeDesign(String design)' in prefs
assert 'Ui.tr("Home design")' in settings and 'case HOME_DESIGN_ID: Prefs.setHomeDesign(value)' in settings
assert '"weather".equals(Prefs.homeDesign()) ? 4' in (src / 'HomeView.java').read_text(encoding='utf-8')
home = (src / 'HomeView.java').read_text(encoding='utf-8')
assert 'private void drawOrbitDashboard(Canvas c)' in home
assert 'selectedHomeGradient = new LinearGradient' in home
assert 'previewFallback = new HomeView(owner)' in home
assert 'HomeView.drawLivePreview(activity, c, box)' in settings
assert apps.count('if (!keys.contains(TOP_SETTINGS)) keys.add(TOP_SETTINGS)') == 2
assert 'if (editingTop && HomeApps.TOP_SETTINGS.equals(pkg)) return;' in settings
assert 'case PERSONALIZATION: return listLabels().length;' in settings
assert '"orbit-dark", "orbit-light"' in settings
print('Personalization preset checks passed.')
