package stampshub.app.stampshub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseReceive extends ParsePushBroadcastReceiver {

    public static String msg;
    String jsonData;
    JSONObject jsonObject;
    ParseObject pushData;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        pushData=new ParseObject("pushData");
        try
        {
            Bundle extras = intent.getExtras();
            jsonData = extras.getString("com.parse.Data");
            jsonObject = new JSONObject(jsonData);
            msg = jsonObject.getString("alert");
            pushData.put("Message",msg);
            pushData.save();
        }
        catch (JSONException e)
        {
            msg = e.getMessage();
        }
        catch (Exception e)
        {
            msg=e.getMessage();
        }
    }
}
