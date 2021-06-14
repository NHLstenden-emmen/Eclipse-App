package INF1D.eclipse.discovery;

import android.net.nsd.NsdServiceInfo;
import androidx.annotation.NonNull;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Mirror implements Serializable {
    public String name;
    private InetAddress address;
    private final int port;
    private final HashMap<String, String> attributes = new HashMap<>();

    private WebSocket webSocket;


    public Mirror() throws UnknownHostException {
        this.name = "testmirror";
        this.port = 80;
        this.address = InetAddress.getByName("127.0.0.1");
    }
    public Mirror(NsdServiceInfo serviceInfo)  {
        this.name = serviceInfo.getServiceName();
        this.port = serviceInfo.getPort();

        try {
            this.address = InetAddress.getByName(serviceInfo.getHost().getHostAddress());
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        serviceInfo.getAttributes().forEach((key, value) -> this.attributes.put(key, new String(value)));
    }

    public InetAddress getAddress() {
        if(address != null) {
            try {
                return InetAddress.getByAddress(address.getHostAddress().replace("/", "").getBytes(StandardCharsets.UTF_8));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void openWebSocket() {
        new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("ws://" + getAddress().getHostAddress() + ":" + port).build();
                webSocket = client.newWebSocket(request, new Listener());
           // client.dispatcher().executorService().shutdown();
        }, "websocket").start();
    }

    public void closeWebSocket() {
        webSocket.close(1, "user closed settings");
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void sendJSONToMirror(JSONObject json) {
        System.out.println(json);

        //   webSocket.send(json.toString());
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
