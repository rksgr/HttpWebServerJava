import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;

public class Handlers {
    public static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1> Server start success if you see this message </h1>"+
                            "<h1>"+SimpleHttpServer.port+"</h1>";
            exchange.sendResponseHeaders(200,response.length());
            try{
                Thread.sleep(10000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    public static class EchoHeaderHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getRequestHeaders();
            Set<Map.Entry<String,List<String>>> entries = headers.entrySet();
            String response = "";
            for(Map.Entry<String,List<String>> entry:entries)
                response += entry.toString() + "\n";
            exchange.sendResponseHeaders(200,response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    public static class EchoGetHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{
            Map<String,Object> parameters = new HashMap<>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            EchoPostHandler.parseQuery(query,parameters);
            String response ="";
            for(String key: parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";
            exchange.sendResponseHeaders(200,response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }}
    public static class EchoPostHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
            String response = "";
            for (String key : parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
            if (query != null) {
                String pairs[] = query.split("[&]");
                for (String pair : pairs) {
                    String param[] = pair.split("[=]");
                    String key = null;
                    String value = null;
                    if (param.length > 1) {
                        key = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                    }
                    if (param.length > 0) {
                        key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                    }
                    parameters.put(key, value);
                }
            }
        }
    }
}
