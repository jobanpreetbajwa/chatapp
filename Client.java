import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.Calendar;

public class Client extends JFrame implements ActionListener,MouseListener{
    private JTextField t1;
    private JPanel p2;
    static JLabel l2;
    static DataOutputStream output;
    Box vertical=Box.createVerticalBox();
    public Client(){
        Container c =getContentPane();
        setLayout(null);
        setBounds(700,50,480,700);
        setTitle("Server...");
        setBackground(Color.WHITE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p1 = new JPanel();
        p1.setBackground(Color.gray);
        p1.setLayout(null);
        p1.setBounds(0,0,480,70);

        //arrow image
        // ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("photo.jpg"));
        JButton b1 = new JButton("<<");
        b1.setBounds(5,20,25,25);
        p1.add(b1);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                
            }
        });

        JLabel l1 = new JLabel("Client");
        l1.setBounds(205,20,150,25);
        l1.setForeground(Color.WHITE);
        p1.add(l1);

        l2 = new JLabel("Active now");
        l2.setBounds(200,40,100,25);
        l2.setForeground(Color.WHITE);
        p1.add(l2);

        p2 = new JPanel();
        p2.setBounds(5,75,470,570);
        p2.setBackground(Color.WHITE);

        t1= new JTextField();
        t1.setBounds(4,645,400,50);
        t1.addMouseListener(this);

        JButton b2 = new JButton(" send ");
        b2.setBounds(406,646,65,49);
        b2.setBackground(Color.CYAN);
        b2.setForeground(Color.BLACK);
        b2.addActionListener(this);

        c.add(p1);
        c.add(p2);
        c.add(t1);
        c.add(b2);

        setUndecorated(true);//for removing top close bar.
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        JLabel l3 = new JLabel(t1.getText());
        l2.setText("Active now");
        l3.setFont(new Font("Tahoma",Font.BOLD,16));
        try {
            if (t1.getText().length()==0){
                output.writeUTF("");
                return;
            }
            System.out.println(t1.getText());
            output.writeUTF(t1.getText());
        } catch (Exception exp) {
            System.out.println("error in server while sending");
        }

        t1.setText("");
        JPanel p3 = new JPanel();
        p3.setBackground(Color.gray);
        p3.add(l3);

        Calendar cal = Calendar.getInstance();  // time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setFont(new Font("serif",Font.ITALIC,10));
        time.setText(sdf.format(cal.getTime()));
        p3.setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
        p3.add(time);

        // JPanel timePanel= new JPanel();
        // timePanel.add(time);
        // p3.add(timePanel,BorderLayout.LINE_END);

        p2.setLayout(new BorderLayout());

        JPanel p4 = new JPanel(new BorderLayout()); //msg panel
        p4.add(p3,BorderLayout.LINE_END);
        p4.setBackground(Color.WHITE);

        vertical.add(p4);
        vertical.add(Box.createVerticalStrut(15));

        p2.add(vertical,BorderLayout.PAGE_START);
        repaint();
        invalidate();
        validate();
    }
    @Override                                       //mouse listene.
    public void mouseClicked(MouseEvent e) {
        try {
            output.writeUTF("Typing...");
        } catch (Exception ex) {
            System.out.println("Status error...");
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    public void getMsg(String s){                                      // getting new msg.
        System.out.println(s);
        l2.setText("Active Now");
        JLabel msg = new JLabel(s);
        JPanel p3 = new JPanel();
        msg.setFont(new Font("Tahoma",Font.ITALIC,16));
        p3.add(msg);

        Calendar cal = Calendar.getInstance();  // time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setFont(new Font("serif",Font.ITALIC,10));
        time.setText(sdf.format(cal.getTime()));
        JPanel timePanel= new JPanel();
        timePanel.setBackground(Color.gray);
        timePanel.add(time);
        p3.add(timePanel,BorderLayout.LINE_START);
        p3.setBackground(Color.gray);
        p3.setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));

        p2.setLayout(new BorderLayout());

        JPanel p4 = new JPanel(new BorderLayout());                         //msg panel
        p4.add(p3,BorderLayout.LINE_START);
        p4.setBackground(Color.WHITE);

        vertical.add(p4);
        vertical.add(Box.createVerticalStrut(15));

        p2.add(vertical,BorderLayout.PAGE_START);
        validate();
        
    }
    public static void main(String[] args) throws IOException{
        Client obj = new Client();
        Socket s = new Socket("localhost",8000);
        System.out.println("going to send");
        output = new DataOutputStream(s.getOutputStream());
        DataInputStream input = new DataInputStream(s.getInputStream());
        while(true){
            // byte []buffer = new byte[1024];
            // int sizeBuffer = input.read(buffer);
            // String msgRead = new String(buffer,0,sizeBuffer);
            String msgRead = input.readUTF();
            Integer i = msgRead.compareTo("Typing...");
            if (i==0 || msgRead.isEmpty()){                                                      // as it returns 0 on true.
                if (msgRead.isEmpty())
                        l2.setText("Active Now");
                    else{
                        l2.setText(msgRead);
                    }
            }else{
                obj.getMsg(msgRead);
            }
            
            

        }
        
    }

    
}