import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;


public class SimpleHttpServer {
    public static int DEFAULT_PORT = 9000;
    private HttpServer httpServer;
    public static int port;
    private void start(int port) throws IOException {
        this.port = port;
        try{
            httpServer = HttpServer.create(new InetSocketAddress(port),0);
            System.out.println("Server started at "+port);
            httpServer.createContext("/",new Handlers.RootHandler());
            httpServer.createContext("/echoHeader",new Handlers.EchoHeaderHandler());
            httpServer.createContext("/echoGet",new Handlers.EchoGetHandler());
            httpServer.createContext("/echoPost",new Handlers.EchoPostHandler());
            httpServer.setExecutor(null);
            httpServer.start();
        }catch(IOException e)
        {e.printStackTrace();}
    }
    public static void main(String[] args) throws IOException {
        SimpleHttpServer httpServer = new SimpleHttpServer();
        httpServer.start(DEFAULT_PORT);
    }
}
