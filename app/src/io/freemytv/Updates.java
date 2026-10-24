package io.freemytv;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** GitHub Releases update checks and user-confirmed APK downloads. */
final class Updates {
    private static final String API = "https://api.github.com/repos/markelpher/free-my-tv/releases/latest";
    private static final String PREFS = "update-checks";
    private static final String CHANNEL = "updates";
    private static final ExecutorService worker = Executors.newSingleThreadExecutor();

    interface Callback { void done(Release release, String error); }
    static final class Release {
        final String version, apk;
        Release(String version, String apk) { this.version = version; this.apk = apk; }
    }

    private Updates() {}

    static void automatic(Context context) {
        if (!Prefs.updateChecks()) return;
        long last = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getLong("last", 0);
        if (System.currentTimeMillis() - last < 24L * 60 * 60 * 1000) return;
        check(context.getApplicationContext(), (release, error) -> {});
    }

    static void check(Context context, Callback callback) {
        Context app = context.getApplicationContext();
        worker.execute(() -> {
            Release result = null;
            String error = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(API).openConnection();
                connection.setConnectTimeout(12000);
                connection.setReadTimeout(12000);
                connection.setRequestProperty("Accept", "application/vnd.github+json");
                connection.setRequestProperty("User-Agent", "Free-My-TV");
                try {
                    if (connection.getResponseCode() != 200) throw new java.io.IOException("HTTP " + connection.getResponseCode());
                    StringBuilder body = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                        String line;
                        while ((line = reader.readLine()) != null) body.append(line);
                    }
                    JSONObject release = new JSONObject(body.toString());
                    String tag = release.optString("tag_name", "");
                    JSONArray assets = release.optJSONArray("assets");
                    String apk = null;
                    for (int i = 0; assets != null && i < assets.length(); i++) {
                        JSONObject asset = assets.optJSONObject(i);
                        if (asset != null && asset.optString("name").endsWith(".apk")) {
                            apk = asset.optString("browser_download_url"); break;
                        }
                    }
                    String installed = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName;
                    if (!tag.isEmpty() && apk != null && newer(tag, installed)) result = new Release(tag, apk);
                } finally { connection.disconnect(); }
            } catch (Exception e) { error = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage(); }
            app.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putLong("last", System.currentTimeMillis()).apply();
            Release found = result;
            String problem = error;
            if (found != null) notifyAvailable(app, found.version);
            if (callback != null) new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.done(found, problem));
        });
    }

    static void download(Context context, Release release) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(release.apk));
        request.setTitle("Free My TV " + release.version);
        request.setDescription("Free My TV " + release.version);
        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(false);
        manager.enqueue(request);
    }

    private static void notifyAvailable(Context context, String version) {
        Ui.init(context);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) manager.createNotificationChannel(new NotificationChannel(CHANNEL,
                Ui.tr("Free My TV updates"), NotificationManager.IMPORTANCE_DEFAULT));
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(context, CHANNEL)
                : new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.icon).setContentTitle(Ui.tr("Free My TV update"))
                .setContentText(Ui.trf("Version {version} is available", "version", version))
                .setStyle(new Notification.BigTextStyle().bigText(Ui.trf("Free My TV {version} is available. Open Settings and choose Check for updates to download it.", "version", version)))
                .setAutoCancel(true);
        manager.notify(7313, builder.build());
    }

    private static boolean newer(String remote, String local) {
        String[] a = remote.replaceFirst("^[^0-9]*", "").split("\\."), b = local.split("\\.");
        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int x = i < a.length ? number(a[i]) : 0, y = i < b.length ? number(b[i]) : 0;
            if (x != y) return x > y;
        }
        return false;
    }
    private static int number(String part) {
        String digits = part.replaceAll("[^0-9].*$", "");
        try { return Integer.parseInt(digits); } catch (Exception ignored) { return 0; }
    }
}
