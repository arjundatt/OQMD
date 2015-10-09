package DIC.Test.ChatClient;

import java.util.ArrayList;
import java.util.Arrays;

public class Packet {
    private int destinationPort;
    private int sourcePort;
    private String data;
    private ClientType type;
    private int dataLength;

    public Packet(int destinationPort, int sourcePort, String data, ClientType type) {
        this.destinationPort = destinationPort;
        this.sourcePort = sourcePort;
        this.data = data;
        this.type = type;
    }

    public ClientType getType() {
        return type;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public int getSourcePort() {
        return sourcePort;
    }
    /*public Packet(String outputStream) {
        try {
            if (outputStream.startsWith("FF8F8")) {     //bluetooth
                destinationPort = Integer.parseInt(outputStream.substring(5, 10));
                sourcePort = Integer.parseInt(outputStream.substring(10, 15));
                length = outputStream.substring(15, 18);
                int dataLength = Integer.parseInt(length);
                data = outputStream.substring(18, dataLength + 18);
                type = ClientType.bluetooth;
            } else if (outputStream.startsWith("12602M")) {    //zigbee
                destinationPort = Integer.parseInt(outputStream.substring(6, 11));
                sourcePort = Integer.parseInt(outputStream.substring(11, 16));
                length = outputStream.substring(16, 19);
                int dataLength = Integer.parseInt(length);
                data = outputStream.substring(19, dataLength + 19);
                type = ClientType.zigbee;
            } else if (outputStream.startsWith("rdrd1")) { //rfid
                destinationPort = Integer.parseInt(outputStream.substring(5, 10));
                sourcePort = Integer.parseInt(outputStream.substring(10, 15));
                length = outputStream.substring(15, 18);
                int dataLength = Integer.parseInt(length);
                data = outputStream.substring(18, dataLength + 18);
                type = ClientType.rfid;
            } else if (outputStream.startsWith("00")) {//ethernet
                destinationPort = Integer.parseInt(outputStream.substring(2, 7));
                sourcePort = Integer.parseInt(outputStream.substring(7, 12));
                length = outputStream.substring(12, 15);
                int dataLength = Integer.parseInt(length);
                data = outputStream.substring(15, dataLength + 15);
                type = ClientType.ethernet;
            } else {
                System.out.println("Could not understand data format");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Wrong packet info given");
        }
    }*/

    public Packet(String outputStream) {
        ArrayList<String> strings = splitString(outputStream);
        destinationPort = Integer.parseInt(strings.get(1));
        sourcePort = Integer.parseInt(strings.get(2));
        dataLength = Integer.parseInt(strings.get(3));
        data = strings.get(4);
        if (outputStream.startsWith("FF8F8"))
            type = ClientType.bluetooth;
        else if (outputStream.startsWith("12602M"))
            type = ClientType.zigbee;
        else if (outputStream.startsWith("rdrd1"))
            type = ClientType.rfid;
        else if (outputStream.startsWith("00"))
            type = ClientType.rfid;
        else
            System.out.println("Could not understand data format");
    }

    public String getOutputStream() {
        if (type == ClientType.bluetooth)
            return getBluetoothOutputStream();
        else if (type == ClientType.zigbee)
            return getZigbeeOutputStream();
        else
            return getEthernetOutputStream();
    }

    public String getBluetoothOutputStream() {
        return "FF8F8," + destinationPort + "," + sourcePort + "," + dataLength + "," + data + ",000";
    }

    public String getZigbeeOutputStream() {
        return "12602M," + destinationPort + "," + sourcePort + "," + dataLength + "," + data + ",000";
    }

    public String getRfidOutputStream() {
        return "rdrd1," + destinationPort + "," + sourcePort + "," + dataLength + "," + data + ",000";
    }

    public String getEthernetOutputStream() {
        return "00," + destinationPort + "," + sourcePort + "," + dataLength + "," + data + ",000";
    }

    private ArrayList<String> splitString(String str) {
        return new ArrayList<String>(Arrays.asList(str.split(",")));
    }

    public String getOutputStream(ClientType type) {
        if (type == ClientType.bluetooth)
            return getBluetoothOutputStream();
        else if (type == ClientType.zigbee)
            return getZigbeeOutputStream();
        else if (type == ClientType.rfid)
            return getRfidOutputStream();
        else
            return getEthernetOutputStream();
    }

    public long getDelay() {
        if (type == ClientType.bluetooth)
            return 0;
        else if (type == ClientType.ethernet)
            return 10000000;
        else if (type == ClientType.rfid)
            return 10000000;
        else          //zigbee
            return 20000000;
    }
}
