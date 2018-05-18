package it.polimi.ingsw.network;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/*import static it.polimi.ingsw.network.ServerMainRMI.PORT;

public class ClientMainRMI {

    public static void main( String[] args ){
        try {
        // Getting the registry
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
        // Looking up the registry for the remote object
        String remoteObjectName = "rmi://127.0.0.1/Sagrada_project";
        ServerIntRMI stub = (ServerIntRMI) registry.lookup(remoteObjectName);
        // Calling the remote method using the obtained object
        ClientIntRMI a = new ClientMainRMI("Duculo");
        Boolean logged = stub.login(a);
        System.out.println("Remote method invoked " + logged);
        }catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

class ClientMainRMI  extends UnicastRemoteObject implements ClientIntRMI{

    private String name;
    public ClientMainRMI (String n) throws RemoteException {
        name=n;
    }

    public void tell(String st) throws RemoteException{
        System.out.println(st);
    }
    public String getName() throws RemoteException{
        return name;
    }

}
public class ClientMainRMI  extends UnicastRemoteObject implements ClientIntRMI{

    private String name;
    private ChatUI ui;
    public ClientMainRMI (String n) throws RemoteException {
        name=n;
    }

    public void tell(String st) throws RemoteException{
        System.out.println(st);
        ui.writeMsg(st);
    }
    public String getName() throws RemoteException{
        return name;
    }

    public void setGUI(ChatUI t){
        ui=t ;
    }
}
class ChatUI{
    private ClientMainRMI client;
    private ServerIntRMI server;
    public void doConnect(){
        if (connect.getText().equals("Connect")){
            if (name.getText().length()<2){
                JOptionPane.showMessageDialog(frame, "You need to type a name.");
                return;
            }
            if (ip.getText().length()<2){
                JOptionPane.showMessageDialog(frame, "You need to type an IP.");
                return;
            }
            try{
                client=new ClientMainRMI(name.getText());
                client.setGUI(this);

                server=(ServerIntRMI) Naming.lookup("rmi://"+ip.getText()+"/Sagrada");
                server.login(client);

                updateUsers(server.getConnected());
                connect.setText("Disconnect");
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect....");
            }
        }else{
            //updateUsers(null);
            try{
                server.logout(client);
                updateUsers(null);
                connect.setText("Connect");
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "FATAL ERROR, we wouldn't disconnect correctly...");
            }
        }
    }

    public void sendText(){
        if (connect.getText().equals("Connect")){
            JOptionPane.showMessageDialog(frame, "You need to connect first."); return;
        }
        String st=tf.getText();
        st="["+name.getText()+"] "+st;
        tf.setText("");
        //Remove if you are going to implement for remote invocation
        try{
            server.publish(st);
        }catch(Exception e){e.printStackTrace();}
    }

    public void writeMsg(String st){  tx.setText(tx.getText()+"\n"+st);  }

    public void updateUsers(Vector v){
        DefaultListModel listModel = new DefaultListModel();
        if(v!=null) for (int i=0;i<v.size();i++){
            try{  String tmp=((ClientIntRMI)v.get(i)).getName();
                listModel.addElement(tmp);
            }catch(Exception e){e.printStackTrace();}
        }
        lst.setModel(listModel);
    }

    public static void main(String [] args){
        System.out.println("Hello World !");
        ChatUI c=new ChatUI();
    }

    //User Interface code.
    public ChatUI(){
        frame=new JFrame("Group Chat");
        JPanel main =new JPanel(); JPanel top =new JPanel();  JPanel cn =new JPanel();
        JPanel bottom =new JPanel();
        ip=new JTextField(); tf=new JTextField(); name=new JTextField();
        tx=new JTextArea();
        connect=new JButton("Connect"); JButton bt=new JButton("Send");
        lst=new JList();
        main.setLayout(new BorderLayout(5,5));
        top.setLayout(new GridLayout(1,0,5,5));
        cn.setLayout(new BorderLayout(5,5));
        bottom.setLayout(new BorderLayout(5,5));
        top.add(new JLabel("Your name: "));top.add(name);
        top.add(new JLabel("Server Address: "));top.add(ip);
        top.add(connect);
        cn.add(new JScrollPane(tx), BorderLayout.CENTER);
        cn.add(lst, BorderLayout.EAST);
        bottom.add(tf, BorderLayout.CENTER);
        bottom.add(bt, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10) );
        //Events
        connect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ doConnect();   }  });
        bt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ sendText();   }  });
        tf.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ sendText();   }  });

        frame.setContentPane(main);
        frame.setSize(600,600);
        frame.setVisible(true);
    }
    JTextArea tx;
    JTextField tf,ip, name;
    JButton connect;
    JList lst;
    JFrame frame;
}
*/

