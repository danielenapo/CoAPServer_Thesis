package simpleServer;/*package it.unimore.fum.iot;
import it.unimore.fum.iot.resource.CapsulePresenceSensorResource;
import it.unimore.fum.iot.resource.CoffeeActuatorResource;
import it.unimore.fum.iot.resource.resources.TemperatureSensorResource;*/
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.EndpointManager;
import resources.TemperatureSensorResource;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project java-coap-laboratory
 * @created 12/11/2021 - 11:57
 */
public class HelloWorldServer extends CoapServer {

    public HelloWorldServer(){

        super();

        this.add(new TemperatureSensorResource("temperature"));

    }

    public static void main(String[] args) {
        EndpointManager.clear();

        HelloWorldServer coapServer = new HelloWorldServer();
        coapServer.start();
        System.out.println(coapServer.getEndpoints().toString());

        coapServer.getRoot().getChildren().forEach(resource -> {
            System.out.println(String.format("Resource %s -> URI: %s (Observable: %b)", resource.getName(), resource.getURI(), resource.isObservable()));
        });
    }
}