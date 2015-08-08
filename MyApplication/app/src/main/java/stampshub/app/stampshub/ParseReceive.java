package stampshub.app.stampshub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseReceive extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }
}
