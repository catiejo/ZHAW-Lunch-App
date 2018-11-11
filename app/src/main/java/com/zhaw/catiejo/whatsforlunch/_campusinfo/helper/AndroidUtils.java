package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Various Android related utilities.
 */
public final class AndroidUtils {

    private AndroidUtils() {
    }

    /**
     * Converts given size in Density independent pixels (dps) into pixels using the supplied
     * scale factor. The scale factor is device dependent and can be obtained by calling
     * <p/>
     * <code>getContext().getResources().getDisplayMetrics().density</code>
     *
     * @param dps   Density independent pixels
     * @param scale Scale factor
     * @return Pixel value
     */
    public static int dpToPixels(int dps, float scale) {
        return (int) (dps * scale + 0.5f);
    }

    /**
     * Reads SQL statements from the given resource file and returns an Iterable containing
     * the separated SQL statements (split by <code>;</code>) ready to execute.
     *
     * @param context    Android context
     * @param resourceId Resource file with SQL statements
     * @return Iterable with separated SQL statements
     */
    public static Iterable<String> readSqlFile(Context context, int resourceId) {
        try {
            final InputStream is = context.getResources().openRawResource(resourceId);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            final StringBuilder builder = new StringBuilder();

            int numRead = 0;
            final char[] buf = new char[1024];
            while ((numRead = reader.read(buf)) != -1) {
                builder.append(String.valueOf(buf, 0, numRead));
            }
            String toSplit = builder.toString().replaceAll("(^|\\r?\\n)(CREATE|ALTER)", "<SPLIT>$2");
            return Splitter.on("<SPLIT>").omitEmptyStrings().trimResults().split(toSplit);
        } catch (IOException e) {
            throw new SqlFileReaderException(e);
        }
    }

    /**
     * Creates a new {@link AlertDialog.Builder} using the given style. On API levels below 11, a
     * {@link ContextThemeWrapper} is used to apply the requested theme. On API level 11 and above, the
     * constructor {@link AlertDialog.Builder#Builder(android.content.Context, int)} is used.
     *
     * @param context The context which the dialog should be displayed in.
     * @param theme   The theme to apply.
     * @return The themed {@link AlertDialog.Builder}
     */
    public static AlertDialog.Builder createAlertDialogBuilderWithStyle(Context context, int theme) {
        if (Build.VERSION.SDK_INT < 11) {
            return new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
        }
        return new AlertDialog.Builder(context, theme);
    }

    /**
     * Applies the given {@code backgroundResource} to the {@code view} without losing the padding (which Android does
     * by default).
     *
     * @param view               To apply the background resource to.
     * @param backgroundResource The background resource to apply.
     * @param <T>                The type of view.
     */
    public static <T extends View> void applyBackgroundResourceRetainingPadding(T view, int backgroundResource) {
        int bottom = view.getPaddingBottom();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int left = view.getPaddingLeft();
        view.setBackgroundResource(backgroundResource);
        view.setPadding(left, top, right, bottom);
    }

    /**
     * Tests whether the the current orientation is landscape. Returns true if it is, false otherwise.
     *
     * @param context Application context.
     * @return True if the current orientation is landscape, false otherwise.
     */
    public static boolean isInLandscapeOrientation(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
