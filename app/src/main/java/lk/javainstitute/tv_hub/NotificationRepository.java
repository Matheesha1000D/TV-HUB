package lk.javainstitute.tv_hub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

    private NotificationDbHelper dbHelper;

    public NotificationRepository(Context context) {
        dbHelper = new NotificationDbHelper(context);
    }

    public void insertNotification(String text, String timestamp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotificationDbHelper.COLUMN_TEXT, text);
        values.put(NotificationDbHelper.COLUMN_TIMESTAMP, timestamp);
        db.insert(NotificationDbHelper.TABLE_NAME, null, values);
    }


    public List<String> getAllNotifications() {
        List<String> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NotificationDbHelper.TABLE_NAME,
                new String[]{NotificationDbHelper.COLUMN_TEXT, NotificationDbHelper.COLUMN_TIMESTAMP},
                null, null, null, null, NotificationDbHelper.COLUMN_TIMESTAMP + " DESC");

        while (cursor.moveToNext()) {
            String text = cursor.getString(cursor.getColumnIndexOrThrow(NotificationDbHelper.COLUMN_TEXT));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(NotificationDbHelper.COLUMN_TIMESTAMP));
            notifications.add(text + " - " + timestamp);
        }
        cursor.close();
        return notifications;
    }
}