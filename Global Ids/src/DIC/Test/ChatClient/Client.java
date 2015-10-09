package DIC.Test.ChatClient;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


public abstract class Client {
    protected ClientType type;
    protected String clientName;
    //    protected JComboBox<PortDetails> otherClientsComboBox;
//    private DefaultComboBoxModel<PortDetails> otherClientsModel;
    private JTextArea incoming;
    private JTextField outgoing;
    private BufferedReader bufferedReader;
    protected PrintWriter printWriter;
    protected Socket socket;
    static HSSFWorkbook workbook;
    static HSSFSheet receiveTimeSheet;
    static FileOutputStream fileOutputStream;
    static int numberOfPackets = 0;
    static int numberOfGateways = 1;        // todo modify this to change the number of gateways

    public abstract void sendData(String data, int destinationPort);

    static {
        workbook = new HSSFWorkbook();
        receiveTimeSheet = workbook.createSheet("Receive Time Sheet");
        try {
            fileOutputStream = new FileOutputStream(new File("E:\\ReceiveTime.xls"));       //todo change directory
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void go() {
        JFrame jFrame = new JFrame(clientName);
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 40);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(incoming);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());

        mainPanel.add(scrollPane);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
//        mainPanel.add(otherClientsComboBox);
        jFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        jFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        jFrame.setSize(500, 500);
//        jFrame.setVisible(true);

    }

    private void setUpNetworking() {
        try {
            socket = new Socket("127.0.0.1", 5000);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("##type:" + type);
            printWriter.flush();
            System.out.println("Networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                printWriter.println(outgoing.getText());
                printWriter.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public abstract String getOutputStream(String data);

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            try {
                String message;
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println("read " + message);
                    fileOutputStream = new FileOutputStream(new File("E:\\ReceiveTime.xls"));    //todo change directory
                    if (!message.startsWith("##new")) {
                        Packet packet = new Packet(message);
                        Row row = receiveTimeSheet.createRow(numberOfPackets++);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(System.nanoTime() + numberOfGateways * packet.getDelay());
                        writeToFile();
                    }
                    /*if (message.startsWith("##new:")) {
                        int comma = message.lastIndexOf(',');
                        String deviceName = message.substring(6, comma);
                        int port = Integer.parseInt(message.substring(comma + 6, message.length()));
                        otherClientsModel.addElement(new PortDetails(deviceName, port));
                    } else */
                    if (type != ClientType.rfid)
                        incoming.append(message.replace(",", "") + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void writeToFile() {
            try {
//                fileOutputStream = new FileOutputStream(new File("D:\\ReceiveTime.xls"));
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
//                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
