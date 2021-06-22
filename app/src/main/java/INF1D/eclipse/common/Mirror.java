package INF1D.eclipse.common;

import android.net.nsd.NsdServiceInfo;
import org.json.JSONObject;

import java.io.Serializable;

public class Mirror implements Serializable {
    public String name;
    private final String address;
    private final int port;

    public Mirror(NsdServiceInfo serviceInfo)  {
        this.name = serviceInfo.getServiceName();
        this.port = serviceInfo.getPort();
        this.address = serviceInfo.getHost().getHostAddress();
    }

    public String getName() {
        return name;
    }

    public void openWebSocket() {
        MirrorSocket.setWebSocket(this.address, this.port);
    }

    public void closeWebsocket () {
            MirrorSocket.closeWebSocket();
    }

    public void sendJSONToMirror(JSONObject json) {
        MirrorSocket.webSocket.send(json.toString());
        System.out.println(json);
    }

}
