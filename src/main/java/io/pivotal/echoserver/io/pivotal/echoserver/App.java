package io.pivotal.echoserver.io.pivotal.echoserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
 
public class App {
        private ServerSocket serverSocket;
 
        public App(String ipAddress, int port) throws Exception {
                serverSocket = new ServerSocket(port, 0, InetAddress.getByName(ipAddress));
                while (true) {
                        Socket clientSocket = serverSocket.accept();
                        RequestHandler service = new RequestHandler(clientSocket);
                        service.start();
                }
        }
 
        public void destroy() throws Exception {
                serverSocket.close();
        }
 
        class RequestHandler extends Thread {
                Socket clientSocket;
 
                public RequestHandler(Socket socket) {
                        this.clientSocket = socket;
                }
 
                @Override
                public void run() {
                        try {
                                InputStream is = this.clientSocket.getInputStream();
                                OutputStream os = this.clientSocket.getOutputStream();
                                int bytesRead = 0;
                                byte[] byteArray = new byte[1024];
                                System.out.print("\nEcho message: ");
                                while ((bytesRead = is.read(byteArray, 0, byteArray.length)) != -1) {
                                        os.write(byteArray, 0, bytesRead);
                                        System.out.print(new String(Arrays.copyOfRange(byteArray,
                                                        0, bytesRead), Charset.forName("UTF-8")));
 
                                }
                                os.flush();
                                is.close();
                                os.close();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }
 
        public static void main(String[] args) throws Exception {
                //Map<String, String> argsMap = getArguments(args);
                //String portValue = argsMap.get("-" + ConsoleArguments.port.toString());
                //String ipaddressValue = argsMap.get("-" + ConsoleArguments.ipaddress.toString());
 
                //if (portValue != null) {
                   App app = new App("localhost", Integer.parseInt("8080"));
                //} else {
                        //getHelp();
                //}
        }
}
