import com.sun.tools.sjavac.client.SjavacClient;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;


public class JavaCoapClient {
    private static DatagramSocket ds;
    private static int PORT;
    private static InetAddress ip;

    private static byte[] option(String resource, int optionDelta) {
        byte[] option = new byte[1 + resource.length()];
        option[0] = (byte) ((optionDelta << 4) | resource.length());
        for(int i = 0; i < resource.length(); i++) {
            option[i+1] = (byte) resource.charAt(i);
        }
        return option;
    }

    private static byte[] header(String resource, int method, int id) {
        int headerSize = 0;
        byte[] temp = option(resource, 11);
        headerSize = 4 + temp.length;
        if(method == 3) {
            headerSize++;
        }
        if(method == 3) {
            headerSize++;
        }
        byte[] header = new byte[headerSize];
        header[0] = (byte) 80;
        header[1] = (byte) method;
        //ID
        header[2] = (byte) (id >> 8);
        header[3] = (byte) id;
        for(int i = 0; i < temp.length; i++) {
            header[i+4] = temp[i];
        }
        if(method == 3) {
            header[headerSize - 2] = option("", 1)[0];
            header[headerSize - 1] = (byte) 255;
        }
        //
        return header;
    }

    private static byte[] payload(String value) {
        byte[] payload = new byte[value.length()];
        for(int i = 0; i < value.length(); i++) {
            payload[i] = (byte) value.charAt(i);
        }
        return payload;
    }

    public static void send(byte[] header, byte[] payload) throws IOException {
        byte[] data = null;
        if(payload == null) {
            data = header;
        } else {
            data = new byte[header.length + payload.length];
            for(int i = 0; i < data.length; i++) {
                data[i] = i < header.length ? header[i] : payload[i - (header.length)];
            }
        }
        System.out.println(new String(data).trim());
        /*System.out.println(Arrays.toString(data));
        for(int i = 0; i < data.length; i++) {
            System.out.println(data[i] & 0xFF);
        }*/
        DatagramPacket request = new DatagramPacket(data, data.length, ip, PORT);
        ds.send(request);
    }

    public static void receive() throws IOException {
        byte[] data = new byte[65535];
        DatagramPacket pkt = new DatagramPacket(data, data.length);
        //pkt.setLength(data.length);
        ds.receive(pkt);
        System.out.println(pkt);
        byte[] data1 = pkt.getData();
        int[] intdata = new int[data1.length];
        System.out.println(new String(data1).trim());
        /*for(int i = 0; i < data1.length; i++) {
            System.out.println(data1[i] & 0xFF);
        }*/
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        ds = new DatagramSocket();
        PORT = 5683;
        ip = InetAddress.getByName("coap.me");
        int id = 0;
        byte[] header = null;
        byte[] payload = null;

        //System.out.println(ip.getHostAddress());

        Scanner sc = new Scanner(System.in);
        String inp = "";
        ds.setSoTimeout(10000);
        while(true){
            header = null;
            payload = null;
            System.out.println("Välj en method av följande:");
            System.out.println("1. POST");
            System.out.println("2. PUT");
            System.out.println("3. GET");
            System.out.println("4. DELETE");
            System.out.println("5. Exit");

            inp = sc.nextLine();
            int intInp = Integer.parseInt(inp);
            String resource = "";

            if(intInp == 1){
                System.out.println("POST vad?");
                System.out.println();
                resource = sc.nextLine();
                header = header(resource, 2, id);
                id++;
                send(header, payload);
            }
            else if(intInp == 2){
                System.out.println("PUT vart?");
                System.out.println();
                resource = sc.nextLine();
                header = header(resource, 3, id);
                id++;
                System.out.println("PUT vad?");
                System.out.println();
                String value = sc.nextLine();
                payload = payload(value);
                send(header, payload);
            }
            else if(intInp == 3){
                System.out.println("GET på vad?");
                System.out.println();
                resource = sc.nextLine();
                header = header(resource, 1, id);
                id++;
                send(header, payload);
            }
            else if(intInp == 4){
                System.out.println("DELETE vad?");
                System.out.println();
                resource = sc.nextLine();
                header = header(resource, 4, id);
                id++;
                send(header, payload);
            } else if (intInp == 5) {
                System.out.println("Hejdå");
                break;
            }
            else {
                System.out.println("Du har angivit fel alternativ, försök igen");
                System.out.println();
            }
            //Thread.sleep(2000);
            receive();
        }
    }
}
