package pluginTest;

//questa sarebbe la parte che va su Unity
public class PluginMain {
    private static final GETClientPlugin plugin =GETClientPlugin.getInstace();

    public static void main(String[] args) {
        System.out.println(plugin.getResponse("localhost","temperature"));
    }
}
