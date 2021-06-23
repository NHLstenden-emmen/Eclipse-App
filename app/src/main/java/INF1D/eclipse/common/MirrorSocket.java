package INF1D.eclipse.common;

import INF1D.eclipse.settings.widgethandler.data.DataProvider;
import androidx.annotation.NonNull;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MirrorSocket {
    public static WebSocket webSocket;

    public static WebSocket getWebSocket() {
        return webSocket;
    }

    public static void closeWebSocket() {
        if(webSocket != null) {
            webSocket.close(1001, "user closed settings page");
        }
    }

    public static void setWebSocket(String address, int port) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://" + address + ":" + port).build();
        webSocket = client.newWebSocket(request, new Listener());
    }

    public static void sendToMirror(String text) {
        if(webSocket != null) {
            webSocket.send(text);
        }
    }

    public static JSONObject parseTileDataToJSON()  {
        JSONObject jsonObject = new JSONObject();
        DataProvider.mData.forEach((k, w) -> {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(w.type);
            if(w.hasParams()) {
                try {
                    JSONArray jsonArray1 = new JSONArray(w.params);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        jsonArray.put(jsonArray1.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                jsonObject.put(String.valueOf(k), jsonArray);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        });
        System.out.println(jsonObject);
        return jsonObject;
    }

    public static class Listener extends WebSocketListener {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            System.out.println("Connected");
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            System.out.println("Incoming: " + text);
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            System.out.println("Closing " + code + "/" + reason);
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            System.out.println("Closed " + code + "/" + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
            System.out.println(t.getMessage());
        }
    }
}
