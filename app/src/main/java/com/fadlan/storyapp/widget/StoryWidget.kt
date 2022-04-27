package com.fadlan.storyapp.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.fadlan.storyapp.R

/**
 * Implementation of App Widget functionality.
 */
class StoryWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//    @SuppressLint("StringFormatInvalid")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val storyUser = intent.getStringExtra(EXTRA_ITEM)
                    ?: context.getString(R.string.default_user)

                Toast.makeText(
                    context,
                    context.getString(R.string.widget_story, storyUser),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val TOAST_ACTION = "com.fadlan.storyapp.TOAST_ACTION"
        const val EXTRA_ITEM = "com.fadlan.storyapp.EXTRA_ITEM"

        //pindahkan fungsi ini ke companion object, karena kita akan memanggil fungsi ini dari luar kelas
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StoriesWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastIntent = Intent(context, StoryWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val toastPendingIntent = PendingIntent.getBroadcast(
                context, 0, toastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else 0
            )

            val views = RemoteViews(context.packageName, R.layout.story_widget).apply {
                setRemoteAdapter(R.id.stack_view, intent)
                setEmptyView(R.id.stack_view, R.id.empty_view)
                setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}


