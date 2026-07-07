import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketProxy {
   public static long IDLE_COUNT = 0L;

   public static void main(String[] args) {
      new Thread(() -> {
         while (true) {
            IDLE_COUNT++;

            try {
               Thread.sleep(1000L);
            } catch (Exception var1x) {
            }

            if (IDLE_COUNT > 600L) {
               System.exit(0);
            }
         }
      }).start();

      while (true) {
         try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket, args[1], Integer.parseInt(args[2]))).start();
         } catch (Exception exception) {
            exception.printStackTrace();
            return;
         }
      }
   }

   private static void handleClient(Socket clientSocket, String remoteHost, Integer remotePort) {
      try (
         Socket remoteSocket = new Socket(remoteHost, remotePort);
         InputStream clientInput = clientSocket.getInputStream();
         OutputStream clientOutput = clientSocket.getOutputStream();
         InputStream remoteInput = remoteSocket.getInputStream();
         OutputStream remoteOutput = remoteSocket.getOutputStream();
      ) {
         Thread forwardClientToRemote = new Thread(() -> forwardData(clientInput, remoteOutput));
         Thread forwardRemoteToClient = new Thread(() -> forwardData(remoteInput, clientOutput));
         forwardClientToRemote.start();
         forwardRemoteToClient.start();
         forwardClientToRemote.join();
         forwardRemoteToClient.join();
      } catch (IOException | InterruptedException exception) {
         exception.printStackTrace();
      } finally {
         try {
            clientSocket.close();
         } catch (IOException exception) {
            exception.printStackTrace();
         }
      }
   }

   private static void forwardData(InputStream input, OutputStream output) {
      byte[] buffer = new byte[1024];

      int bytesRead;
      try {
         while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            output.flush();
            IDLE_COUNT = 0L;
         }
      } catch (IOException var5) {
      }
   }
}
