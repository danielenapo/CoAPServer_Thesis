package resources;

import com.google.gson.Gson;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project coap-playground
 * @created 20/10/2020 - 21:54
 */
public class CoffeeActuatorResource extends CoapResource {

    private static final String OBJECT_TITLE = "CoffeeActuator";

    private Gson gson;

    private CoffeeHistoryDescriptor coffeeHistoryDescriptor;

    public CoffeeActuatorResource(String name) {
        super(name);
        init();
    }

    private void init(){
        getAttributes().setTitle(OBJECT_TITLE);

        setObservable(true);
        setObserveType(CoAP.Type.CON);

        this.gson = new Gson();
        this.coffeeHistoryDescriptor = new CoffeeHistoryDescriptor();
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try{
            String responseBody = this.gson.toJson(this.coffeeHistoryDescriptor);
            exchange.respond(ResponseCode.CONTENT, responseBody, MediaTypeRegistry.APPLICATION_JSON);
        }catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        try{
            this.coffeeHistoryDescriptor.increaseShortCoffee();
            exchange.respond(ResponseCode.CHANGED);
            changed();
        }catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange) {

        try{

            String receivedPayload = new String(exchange.getRequestPayload());
            MakeCoffeeRequestDescriptor makeCoffeeRequestDescriptor = this.gson.fromJson(receivedPayload, MakeCoffeeRequestDescriptor.class);

            if(makeCoffeeRequestDescriptor != null && makeCoffeeRequestDescriptor.getType() != null && makeCoffeeRequestDescriptor.getType().length() > 0) {
                if(makeCoffeeRequestDescriptor.getType().equals(MakeCoffeeRequestDescriptor.COFFEE_TYPE_SHORT)) {
                    this.coffeeHistoryDescriptor.increaseShortCoffee();
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else if(makeCoffeeRequestDescriptor.getType().equals(MakeCoffeeRequestDescriptor.COFFEE_TYPE_MEDIUM)){
                    this.coffeeHistoryDescriptor.increaseMediumCoffee();
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else if(makeCoffeeRequestDescriptor.getType().equals(MakeCoffeeRequestDescriptor.COFFEE_TYPE_LONG)){
                    this.coffeeHistoryDescriptor.increaseLongCoffee();
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else
                    exchange.respond(ResponseCode.BAD_REQUEST);
            }
            else
                exchange.respond(ResponseCode.BAD_REQUEST);

        }catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}