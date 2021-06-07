package INF1D.eclipse.discovery;

import android.util.Log;
import androidx.annotation.NonNull;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Mirror implements Serializable {
    public String serialNo;
    public String name;
    public Inet4Address IP;
    public byte[] macAdress; //TODO

    private WebSocket webSocket;


    public Mirror(String name, String host, String serialNo) {
        this.name = name;
        this.serialNo = serialNo;

        try {
            this.IP = (Inet4Address) Inet4Address.getByName(host);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void openWebSocket() {
        OkHttpClient client = new OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build();
        String SERVER_URL = "ws:/"+ this.IP.getHostAddress() +":80";
        Request request = new Request.Builder().url(SERVER_URL).build();

        webSocket = client.newWebSocket(request, new Listener());
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void sendJSONToMirror(JSONObject json) {
        webSocket.send(json.toString());
    }

    public static class Listener extends WebSocketListener {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            try {
                webSocket.send(new JSONObject().put("type", "hoi").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
        }
    }
}
