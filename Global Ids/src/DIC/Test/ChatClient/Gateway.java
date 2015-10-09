package DIC.Test.ChatClient;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Gateway implements Runnable {
    private int numberOfConnectedDevices;
    private HashMap<String, ClientDetails> clientDetailsMap;
    private HSSFWorkbook workbook;
    private HSSFSheet clientSheet;
    private FileOutputStream fileOutputStream;
    private int totalBluetoothPackets, totalRfidPackets, totalEthernetPackets, totalZigbeePackets;
    private int lostBluetoothPackets, lostRfidPackets, lostEthernetPackets, lostZigbeePackets;
    double slope = .22;
    int numberOfGateways = 1;     // todo change number of Gateways

    public HashMap<String, ClientDetails> getClientDetailsMap() {
        return clientDetailsMap;
    }

    public Gateway() {
        try {
            numberOfConnectedDevices = 0;
            totalBluetoothPackets = 0;
            totalRfidPackets = 0;
            totalEthernetPackets = 0;
            totalZigbeePackets = 0;
            lostBluetoothPackets = 0;
            lostRfidPackets = 0;
            lostEthernetPackets = 0;
            lostZigbeePackets = 0;
            System.out.println("Gateway");
            clientDetailsMap = new HashMap<String, ClientDetails>();
            workbook = new HSSFWorkbook();
            clientSheet = workbook.createSheet("Clients");
            fileOutputStream = new FileOutputStream(new File("E:\\Clients.xls"));        //todo change directory
            Row row = clientSheet.createRow(numberOfConnectedDevices);
            int column = 0;
            Cell cell = row.createCell(column++);
            cell.setCellValue("Name");
            cell = row.createCell(column++);
            cell.setCellValue("Address");
            cell = row.createCell(column);
            cell.setCellValue("Type");
            writeToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile() throws IOException {
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private ClientDetails findClientDetails(int port) {

        for (Map.Entry<String, ClientDetails> entry : clientDetailsMap.entrySet()) {
            if (entry.getValue().getSocket().getPort() == port)
                return entry.getValue();
        }
        return null;
    }

    @Override
    public void run() {
        go();
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket) {
            try {
                socket = clientSocket;
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStreamReader);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
//                    System.out.println("Read " + message);
                    if (message.startsWith("##type:")) {      //admin message
                        ClientDetails clientDetails = findClientDetails(socket.getPort());
                        if (message.endsWith("bluetooth"))
                            clientDetails.setType(ClientType.bluetooth);
                        else if (message.endsWith("zigbee"))
                            clientDetails.setType(ClientType.zigbee);
                        else if (message.endsWith("rfid"))
                            clientDetails.setType(ClientType.rfid);
                        else
                            clientDetails.setType(ClientType.ethernet);
                        writeExcelSheet(clientDetails);
                    } else {     //normal chat messages
                        Packet packet = new Packet(message);
                        int destinationPort = packet.getDestinationPort();
                        ClientDetails destinationClientDetails = findClientDetails(destinationPort);
                        if (destinationClientDetails != null) {
                            PrintWriter printWriter = destinationClientDetails.getPrintWriter();
                            String outputStream = generateErrorString(packet.getOutputStream(destinationClientDetails.getType()), destinationClientDetails.getErrorProbability(), destinationClientDetails.getType());      //getErrorString
                            printWriter.println(outputStream);
//                            System.out.println("Sent " + outputStream);
                            printWriter.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeExcelSheet(ClientDetails clientDetails) {
        try {
            fileOutputStream = new FileOutputStream(new File("E:\\Clients.xls"));       //todo change directory
            Row row = clientSheet.createRow(numberOfConnectedDevices);
            int column = 0;
            Cell cell = row.createCell(column++);
            cell.setCellValue("Device #" + numberOfConnectedDevices);
            cell = row.createCell(column++);
            cell.setCellValue(clientDetails.getSocket().getPort());
            cell = row.createCell(column);
            cell.setCellValue(clientDetails.getType().toString());
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Gateway().go();
    }

    public void go() {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);            //todo change 5000 to a different number and start a new server
            while (true) {
                //a new client is trying to connect to the server
                Socket clientSocket = serverSocket.accept();
                PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
                ClientDetails clientDetails = new ClientDetails(clientSocket, printWriter);
                numberOfConnectedDevices++;
                String deviceName = "Device #" + numberOfConnectedDevices;

                //tell others about the new client
                tellEveryone("##new:" + deviceName + ",port:" + clientSocket.getPort());

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
                System.out.println("Got a connection");

                //tell about existing clients
                for (Map.Entry<String, ClientDetails> entry : clientDetailsMap.entrySet()) {
                    printWriter.println("##new:" + entry.getKey() + ",port:" + entry.getValue().getSocket().getPort());
                    printWriter.flush();
                }
                clientDetailsMap.put(deviceName, clientDetails);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tellEveryone(String message) {
        for (ClientDetails clientDetails : clientDetailsMap.values()) {
            try {
                PrintWriter printWriter = clientDetails.getPrintWriter();
                printWriter.println(message);
                printWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getTotalBluetoothPackets() {
        return totalBluetoothPackets;
    }

    public int getTotalRfidPackets() {
        return totalRfidPackets;
    }

    public int getTotalEthernetPackets() {
        return totalEthernetPackets;
    }

    public int getTotalZigbeePackets() {
        return totalZigbeePackets;
    }

    public int getLostBluetoothPackets() {
        return lostBluetoothPackets;
    }

    public int getLostRfidPackets() {
        return lostRfidPackets;
    }

    public int getLostEthernetPackets() {
        return lostEthernetPackets;
    }

    public int getLostZigbeePackets() {
        return lostZigbeePackets;
    }

    public String generateErrorString(String data, double deviceErrorProbability, ClientType clientType) {
        double random = Math.random() * 100;
        double errorProbability = deviceErrorProbability * 100 * numberOfConnectedDevices / 4 * slope * (numberOfGateways >= 2 ? 1.5 : 1);
        if (clientType == ClientType.bluetooth)
            totalBluetoothPackets++;
        else if (clientType == ClientType.rfid)
            totalRfidPackets++;
        else if (clientType == ClientType.ethernet)
            totalEthernetPackets++;
        else if (clientType == ClientType.zigbee)
            totalZigbeePackets++;
        if (random < errorProbability) {
            if (clientType == ClientType.bluetooth)
                lostBluetoothPackets++;
            else if (clientType == ClientType.rfid)
                lostRfidPackets++;
            else if (clientType == ClientType.ethernet)
                lostEthernetPackets++;
            else if (clientType == ClientType.zigbee)
                lostZigbeePackets++;
            System.out.println("Packet Lost");
        }
        return data;
    }
}
