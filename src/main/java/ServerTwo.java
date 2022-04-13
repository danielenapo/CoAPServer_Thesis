import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.MyIpResource;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.config.TcpConfig;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.elements.tcp.netty.TcpServerConnector;
import org.eclipse.californium.elements.util.NetworkInterfacesUtil;

public class ServerTwo extends CoapServer {

    static {
        CoapConfig.register();
        UdpConfig.register();
        TcpConfig.register();
    }

    /*
     * Application entry point.
     */
    public static void main(String[] args) {
        try {
            // create server
            boolean udp = true;
            boolean tcp = false;
            int port = Configuration.getStandard().get(CoapConfig.COAP_PORT);
            if (0 < args.length) {
                tcp = args[0].equalsIgnoreCase("coap+tcp:");
                if (tcp) {
                    System.out.println("Please Note: the TCP support is currently experimental!");
                }
            }
            ServerTwo server = new ServerTwo();
            // add endpoints on all IP addresses
            server.addEndpoints(udp, tcp, port);
            server.start();

        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

    /**
     * Add individual endpoints listening on default CoAP port on all IPv4
     * addresses of all network interfaces.
     */
    private void addEndpoints(boolean udp, boolean tcp, int port) {
        Configuration config = Configuration.getStandard();
        for (InetAddress addr : NetworkInterfacesUtil.getNetworkInterfaces()) {
            InetSocketAddress bindToAddress = new InetSocketAddress(addr, port);
            if (udp) {
                CoapEndpoint.Builder builder = new CoapEndpoint.Builder();
                builder.setInetSocketAddress(bindToAddress);
                builder.setConfiguration(config);
                addEndpoint(builder.build());
            }
            if (tcp) {
                TcpServerConnector connector = new TcpServerConnector(bindToAddress, config);
                CoapEndpoint.Builder builder = new CoapEndpoint.Builder();
                builder.setConnector(connector);
                builder.setConfiguration(config);
                addEndpoint(builder.build());
            }

        }
    }

    /*
     * Constructor for a new Hello-World server. Here, the resources of the
     * server are initialized.
     */
    public ServerTwo() throws SocketException {

        // provide an instance of a Hello-World resource
        add(new TemperatureSensorResource("temperature"));
    }

}