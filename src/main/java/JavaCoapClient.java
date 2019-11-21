import java.io.IOException;
import java.net.*;
import java.util.Scanner;


public class JavaCoapClient {

    public static void main(String args[]) throws SocketException, UnknownHostException {
        DatagramSocket ds = new DatagramSocket();
        int PORT = 5683;
        InetAddress ip = InetAddress.getByName("coap.me");
        byte buf[] = null;
        System.out.println(ip.getHostAddress());

        Scanner sc = new Scanner(System.in);

        String inp = "";

        while(true){
            System.out.println("Välj en method av följande:");
            System.out.println("1. POST");
            System.out.println("2. PUT");
            System.out.println("3. GET");
            System.out.println("4. DELETE");
            System.out.println("5. Exit");

            inp = sc.nextLine();
            int intInp = Integer.parseInt(inp);

            if(intInp == 1){
                System.out.println("POST");
                System.out.println();
            }
            else if(intInp == 2){
                System.out.println("PUT");
                System.out.println();
            }
            else if(intInp == 3){
                System.out.println("GET");
                System.out.println();
            }
            else if(intInp == 4){
                System.out.println("DELETE");
                System.out.println();
            } else if (intInp == 5) {
                System.out.println("Hejdå");
                break;
            }
            else {
                System.out.println("Du har angivit fel alternativ, försök igen");
                System.out.println();
            }
        }
    }
}
