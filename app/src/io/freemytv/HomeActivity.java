package io.freemytv;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * The Free My TV home screen (Android's "Home" role in Free My TV mode). It runs in the :home process: if it crashes,
 * the safety net in the main process keeps working. On the 3rd crash within 5 min, the Watchdog gives the home screen
 * back to the TV's original one.
 */
public class HomeActivity extends Activity {
    private static final StartupBrand STARTUP_BRAND = new StartupBrand();
    private static final long STARTUP_MS = 1200;
    private final Handler startupHandler = new Handler(Looper.getMainLooper());
    private final Runnable dismissStartup = this::hideStartup;
    private FrameLayout startupFrame;
    private ImageView startup;
    private HomeView home;

    private int prefsVersion = Prefs.version;   // settings changed while we were away: start over

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Watchdog.install(this);
        if (Watchdog.crashedTooOften(this)) {
            // Don't try to draw again: only show a notice while the main process restores the original home screen.
            HomeMode.requestRevertHome(this);
            TextView t = new TextView(this);
            t.setText(Ui.tr("Free My TV's home screen ran into a problem.\nGoing back to the TV's original home screen…"));
            t.setTextColor(Color.WHITE);
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            t.setGravity(Gravity.CENTER);
            t.setBackgroundColor(Color.rgb(7, 11, 30));
            setContentView(t);
            return;
        }
        Ui.init(this);
        home = new HomeView(this);
        if (STARTUP_BRAND.take(Build.MANUFACTURER, Build.BRAND)) {
            startupFrame = new FrameLayout(this);
            startupFrame.addView(home, new FrameLayout.LayoutParams(-1, -1));
            startup = new ImageView(this);
            startup.setBackgroundColor(Color.BLACK);
            startup.setImageResource(R.drawable.tcl_startup_logo);
            startup.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int inset = Math.round(getResources().getDisplayMetrics().widthPixels * 0.39f);
            startup.setPadding(inset, 0, inset, 0);
            startup.setContentDescription("TCL");
            startup.setOnClickListener(v -> hideStartup());
            startupFrame.addView(startup, new FrameLayout.LayoutParams(-1, -1));
            setContentView(startupFrame);
            // Start the brief hold after attachment; the launcher loads underneath without blocking the UI thread.
            startup.post(() -> { if (startup != null) startupHandler.postDelayed(dismissStartup, STARTUP_MS); });
        } else setContentView(home);
        getWindow().setBackgroundDrawable(null);
    }

    private void hideStartup() {
        startupHandler.removeCallbacks(dismissStartup);
        if (startup != null && startupFrame != null) startupFrame.removeView(startup);
        startup = null;
        startupFrame = null;
    }

    @Override protected void onResume() {
        super.onResume();
        HomeMode.restoreFreeDreamAfterAmbient(this);
        if (prefsVersion != Prefs.version) {
            prefsVersion = Prefs.version;
            HomeData.get(this).settingsChanged();
            recreate();   // a new language or city: draw everything again
            return;
        }
        if (home != null) home.resume();

    }

    @Override protected void onPause() {
        hideStartup();
        if (home != null) home.pause();

        super.onPause();
    }

    /** Home button pressed on the home screen: back to the entry state (focus on the first app, sheet closed). */
    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        hideStartup();
        if (home != null) home.reset(true);

    }

    @Override protected void onDestroy() {
        hideStartup();
        if (home != null) home.destroy();

        super.onDestroy();
    }

    @Override public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= TRIM_MEMORY_COMPLETE) Recs.get(this).trim();
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (startup != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            hideStartup();
            // Let system keys (volume, power, Home) retain their normal behavior.
            int key = event.getKeyCode();
            if (key == KeyEvent.KEYCODE_DPAD_CENTER || key == KeyEvent.KEYCODE_ENTER
                    || key == KeyEvent.KEYCODE_BACK || key == KeyEvent.KEYCODE_DPAD_UP
                    || key == KeyEvent.KEYCODE_DPAD_DOWN || key == KeyEvent.KEYCODE_DPAD_LEFT
                    || key == KeyEvent.KEYCODE_DPAD_RIGHT) return true;
        }
        if (RemoteKeys.onKey(this, event)) return true;
        if (home != null && home.onKey(event)) return true;

        return super.dispatchKeyEvent(event);
    }

}
