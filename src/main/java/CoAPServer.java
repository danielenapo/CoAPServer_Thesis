import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.elements.util.NetworkInterfacesUtil;
import resources.TemperatureSensorResource;

public class CoAPServer extends CoapServer {

    static {
        CoapConfig.register();
        UdpConfig.register();
    }


    public static void main(String[] args) {
        try {
            int port = Configuration.getStandard().get(CoapConfig.COAP_PORT);

            CoAPServer server = new CoAPServer();
            // add endpoints on all IP addresses
            server.addEndpoints(port);
            System.out.println("RESOURCES:");
            server.getRoot().getChildren().forEach(resource -> {
                System.out.println(String.format("Resource %s -> URI: %s (Observable: %b)", resource.getName(), resource.getURI(), resource.isObservable()));
            });
            server.start();
        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

    /**
     * Add individual endpoints listening on default CoAP port on all IPv4
     * addresses of all network interfaces.
     */
    private void addEndpoints(int port) {
        Configuration config = Configuration.getStandard();
        System.out.println("ENDPOINTS:");
        for (InetAddress addr : NetworkInterfacesUtil.getNetworkInterfaces()) {
            InetSocketAddress bindToAddress = new InetSocketAddress(addr, port);
            System.out.println(bindToAddress);

            CoapEndpoint.Builder builder = new CoapEndpoint.Builder();
            builder.setInetSocketAddress(bindToAddress);
            builder.setConfiguration(config);
            addEndpoint(builder.build());
        }
    }

    /*
     * Constructor for a new Hello-World server. Here, the resources of the
     * server are initialized.
     */
    public CoAPServer() throws SocketException {

        // provide an instance of a Hello-World resource
        add(new TemperatureSensorResource("temperature"));
    }

}