package DIC.Test.ChatClient;
/********************************************************************
 *                 Global IDs CONFIDENTIAL INFORMATION              *
 *                     (c)2001-2013 Global IDs Inc.                 *
 *                        All Rights Reserved                       *
 *                                                                  *
 *  This program contains confidential and proprietary information  *
 *  of the Global IDs, Inc.  Any reproduction, disclosure, or use   *
 *  in whole or in part is expressly prohibited, except as may be   *
 *  specifically authorized by prior written agreement.             *
 ********************************************************************/

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Author: Arnab Saha
 * Date: 7/21/15
 * Time: 12:34 PM
 */
public class StaticClients {
    private ArrayList<Client> clients;
    private int totalNumberOfClients;
    double interval = 1;    // todo Interval between sending two packets
    Gateway gateway;
    double mins = 1;        // todo Total time for execution
    private HSSFWorkbook workbook;
    private HSSFSheet sendTimeSheet;
    private FileOutputStream fileOutputStream;

    public StaticClients() throws InterruptedException, FileNotFoundException {
        workbook = new HSSFWorkbook();
        sendTimeSheet = workbook.createSheet("Clients");
        fileOutputStream = new FileOutputStream(new File("E:\\SendingTime.xls"));     //todo change directory
        totalNumberOfClients = 20;  // todo Modify this for total number of clients. Each type will have this number / 4 number of clients
        clients = new ArrayList<Client>();
        gateway = new Gateway();
        Thread thread = new Thread(gateway);
        thread.start();
        Thread.sleep(1000);
        for (int i = 0; i < totalNumberOfClients / 4; i++) {
            BluetoothClient bluetoothClient = new BluetoothClient();
            bluetoothClient.go();
            clients.add(bluetoothClient);
            RfidClient rfidClient = new RfidClient();
            rfidClient.go();
            clients.add(rfidClient);
            ZigbeeClient zigbeeClient = new ZigbeeClient();
            zigbeeClient.go();
            clients.add(zigbeeClient);
            EthernetClient ethernetClient = new EthernetClient();
            ethernetClient.go();
            clients.add(ethernetClient);
        }
    }

    public void go() throws InterruptedException {
        Timer timer = new Timer(); // Instantiate Timer Object
        ScheduledTask st = new ScheduledTask(); // Instantiate ScheduledTask class
        timer.schedule(st, 0, (long) (interval * 1000)); // Create Repetitively task for every interval secs

        //for demo only.
        for (int i = 0; i <= mins; i++) {
            System.out.println("Expected time " + (mins - i) + " mins left");
            Thread.sleep(1000 * 60);
        }
        System.out.println("Total Bluetooth Packets Lost = " + gateway.getLostBluetoothPackets() + " / " + gateway.getTotalBluetoothPackets());
        System.out.println("Total Ethernet Packets Lost = " + gateway.getLostEthernetPackets() + " / " + gateway.getTotalEthernetPackets());
        System.out.println("Total Rfid Packets Lost = " + gateway.getLostRfidPackets() + " / " + gateway.getTotalRfidPackets());
        System.out.println("Total Zigbee Packets Lost = " + gateway.getLostZigbeePackets() + " / " + gateway.getTotalZigbeePackets());
        System.out.println("Application Terminates");
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            StaticClients staticClients = new StaticClients();
            Thread.sleep(1000);
            staticClients.go();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    class ScheduledTask extends TimerTask {
        int rowNum;

        @Override
        public void run() {
            HashMap<String, ClientDetails> clientDetailsMap = gateway.getClientDetailsMap();
            String data = UUID.randomUUID().toString().replace("-", "");
            Client client = clients.get((int) (Math.random() * clients.size()));
            double num = Math.random() * clientDetailsMap.size() + 1;

            System.out.println("Device #" + (int) num);
            ClientDetails receiverClientDetails = clientDetailsMap.get("Device #" + ((int) num));
            Row row = sendTimeSheet.createRow(rowNum++);
            int column = 0;
            Cell cell = row.createCell(column++);
            cell.setCellValue(System.nanoTime());
            cell = row.createCell(column++);
            cell.setCellValue(receiverClientDetails.getType().toString());
            writeToFile();
            client.sendData(data, receiverClientDetails.getSocket().getPort());
        }

        private void writeToFile() {
            try {
                fileOutputStream = new FileOutputStream(new File("E:\\SendingTime.xls"));     //change directory
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
