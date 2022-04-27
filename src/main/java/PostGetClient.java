import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class PostGetClient {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5683/coffee";

    public static void main(String[] args) {

        //Initialize coapClient
        CoapClient coapClient = new CoapClient(COAP_ENDPOINT);

        post(coapClient);
        get(coapClient);


    }


    public static void post(CoapClient coapClient){
        //Request Class is a generic CoAP message: in this case we want a GET.
        //"Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.POST);

        //Set Request as Confirmable
        request.setConfirmable(true);

        System.out.println(String.format("Request Pretty Print: \n%s", Utils.prettyPrint(request)));

        try {
            CoapResponse coapResp = coapClient.advanced(request);
            System.out.println(String.format("Response Pretty Print: \n%s", Utils.prettyPrint(coapResp)));
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void get(CoapClient coapClient){
        Request request= new Request(CoAP.Code.GET);
        request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_JSON));

        //Set Request as Confirmable
        request.setConfirmable(true);

        //Synchronously send the GET message (blocking call)
        CoapResponse coapResp = null;

        try {
            coapResp = coapClient.advanced(request);
            //Pretty print for the received response
            System.out.println(String.format("Response Pretty Print: \n%s", Utils.prettyPrint(coapResp)));
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

}
