package chatapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class ChatServer extends JFrame implements Runnable {
    
    public void run() {}
    
    public ChatServer() {
        this.setLayout(new BorderLayout());
	JPanel old = new JPanel();
	old.setLayout(new BorderLayout());
	this.add(old);
        this.setSize(500,300);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel top = new JPanel(new FlowLayout());
	JPanel center = new JPanel(new BorderLayout());
	JPanel bottom = new JPanel(new FlowLayout());
        TextField userMessage= new TextField();
        TextArea convoArea = new TextArea();
        convoArea.setEditable(false);
        JButton sendButton = new JButton("Send");
        try{
            this.setTitle("Server Messenger " + InetAddress.getLocalHost().getHostAddress());
        }
        catch(UnknownHostException i){
            System.err.println(i);
        }
        old.add(top, BorderLayout.PAGE_START);
	old.add(center, BorderLayout.CENTER);
	old.add(bottom, BorderLayout.PAGE_END);
        top.add(convoArea);
        center.add(userMessage, BorderLayout.CENTER);
        bottom.add(sendButton);
        this.setVisible(true);
        
        try{
            ServerSocket serverSocket = new ServerSocket(8000);
            
            Socket socket = serverSocket.accept();
            
            DataInputStream fromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
            Thread receiveThread = new Thread(){
                public void run () {
                    while(true){
                    String receive;
                    try{
                        receive = fromClient.readUTF();
                        convoArea.append("Client: " + receive + "\n");
                    }
                    
                    catch (IOException ex){
                        System.err.println(ex);
                    }
                    }
                }
            };
            receiveThread.start();
            Thread sendThread = new Thread() {
                    
                public void run () {
                sendButton.addActionListener(new ActionListener()  {
                        public void actionPerformed(ActionEvent e) {
                            try{
                                String send;
                                send = userMessage.getText();
                                convoArea.append("Server: " + send + "\n");
                                toClient.writeUTF(send);
                            }
                            catch (IOException ex) {
                                System.err.println(ex);
                            }
                            userMessage.setText(null);
                        }
                    }
                );
                }
            };
            sendThread.start();
        }
        catch(IOException ex){
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
    
}
