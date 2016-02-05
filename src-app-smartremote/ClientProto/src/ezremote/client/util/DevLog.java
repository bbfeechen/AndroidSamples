
package ezremote.client.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DevLog {
    private static boolean LOG_ENABLED = true; // TODO set false in official release

    private static final String DEFAULT_TAG = "Ez Client";

    private static final String APPLICATION_PREFIX = "[Ez] ";

    private static final String LIFE_CYCLE_PREFIX = "[LifeCycle] ";

    public static void toast(Context context, String str) {
        if (LOG_ENABLED) {
            Toast.makeText(context, DEFAULT_TAG + ": " + str, Toast.LENGTH_LONG).show();
        }
    }

    public static void toast(Context context, View view) {
        if (LOG_ENABLED) {
            final Toast t = new Toast(context);
            final LinearLayout layout = new LinearLayout(context);
            final TextView text = new TextView(context);
            text.setText("DevToast: ");
            layout.addView(text);
            layout.addView(view);
            layout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            layout.setBackgroundColor(Color.DKGRAY);
            layout.setPadding(7, 3, 7, 3);
            t.setView(layout);
            t.show();
        }
    }

    public static void v(String str) {
        if (LOG_ENABLED) {
            v(DEFAULT_TAG, str);
        }
    }

    public static void v(String tag, String str) {
        if (LOG_ENABLED) {
            Log.v(tag, APPLICATION_PREFIX + str);
        }
    }

    public static void d(String str) {
        if (LOG_ENABLED) {
            d(DEFAULT_TAG, str);
        }
    }

    public static void d(String tag, String str) {
        if (LOG_ENABLED) {
            Log.d(tag, APPLICATION_PREFIX + str);
        }
    }

    public static void i(String str) {
        if (LOG_ENABLED) {
            i(DEFAULT_TAG, str);
        }
    }

    public static void i(String tag, String str) {
        if (LOG_ENABLED) {
            Log.i(tag, APPLICATION_PREFIX + str);
        }
    }

    public static void w(String str) {
        if (LOG_ENABLED) {
            w(DEFAULT_TAG, str);
        }
    }

    public static void w(String tag, String str) {
        if (LOG_ENABLED) {
            Log.w(tag, APPLICATION_PREFIX + str);
        }
    }

    public static void e(String str) {
        if (LOG_ENABLED) {
            e(DEFAULT_TAG, str);
        }
    }

    public static void e(String tag, String str) {
        if (LOG_ENABLED) {
            Log.e(tag, APPLICATION_PREFIX + str);
        }
    }

    public static void l(String str) {
        if (LOG_ENABLED) {
            l(DEFAULT_TAG, str);
        }
    }

    public static void l(String tag, String str) {
        if (LOG_ENABLED) {
            d(tag, LIFE_CYCLE_PREFIX + str);
        }
    }
}
