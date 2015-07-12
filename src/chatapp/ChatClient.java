/********************************************
*	Basil Grant                             *
*	12/2/14                                 *
*	Mini-Project							*
********************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class ChatClient extends JFrame implements Runnable {
    
    public void run() {}
    
    public ChatClient() {
        this.setLayout(new BorderLayout());
	JPanel old = new JPanel();
	old.setLayout(new BorderLayout());
        this.setTitle("Client Messenger");
	this.add(old);
        this.setSize(500,300);
	this.setVisible(true);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel top = new JPanel(new FlowLayout());
	JPanel center = new JPanel(new BorderLayout());
	JPanel bottom = new JPanel(new FlowLayout());
        TextField userMessage= new TextField();
        TextArea convoArea = new TextArea();
        convoArea.setEditable(false);
        JButton sendButton = new JButton("Send");
        old.add(top, BorderLayout.PAGE_START);
	old.add(center, BorderLayout.CENTER);
	old.add(bottom, BorderLayout.PAGE_END);
        top.add(convoArea);
        center.add(userMessage, BorderLayout.CENTER);
        bottom.add(sendButton);
        
        try{
            Socket socket = new Socket("localhost", 8000);
            
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
            toServer.flush();
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());
            Thread receiveThread = new Thread(){
                public void run () {
                    while(true){
                    String receive;
                    try{
                        receive = fromServer.readUTF();
                        convoArea.append("Server: " + receive + "\n");
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
                sendButton.addActionListener(  
                    new ActionListener()  {
                        public void actionPerformed(ActionEvent e) {
                            try{
                                String send;
                                send = userMessage.getText();
                                convoArea.append("Client: " + send + "\n");
                                toServer.writeUTF(send);
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
        new ChatClient();
    }
    
}
