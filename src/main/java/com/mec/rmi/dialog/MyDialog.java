 package com.mec.rmi.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mec.rmi.core.RMIClient;

public class MyDialog extends JDialog{
    private static final long serialVersionUID = -2376720023243061211L;
    
    private RMIClient rmiClient;
    private Method method;
    private Object[] argus;
    
    private Container container;
    private JLabel jlblMessage;
    private JPanel jpnlMessage;

    private Object result;
    
    public void setRmiClient(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
    }
    
    public void setMethod(Method method) {
        this.method = method;
    }

    public void setArgus(Object[] argus) {
        this.argus = argus;
    }

    public Object getResult() {
        return result;
    }
    
    public MyDialog setCaption(String message) {
        Font font = new Font("ËÎÌו", Font.BOLD, 16);
        Container parent = getParent();
        int parentLeft = parent.getX();
        int parentTop = parent.getY();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        int width = (message.length() + 4) * font.getSize();
        int height = 5 * font.getSize();
        
        setSize(width, height);
        setLocation(parentLeft + (parentWidth - width) / 2, 
                parentTop + (parentHeight - height) / 2);
        container = getContentPane();
        container.setLayout(null);
        setUndecorated(true);
        
        jpnlMessage = new JPanel();
        jpnlMessage.setSize(width, height);
        jpnlMessage.setLayout(new BorderLayout());
        jpnlMessage.setBackground(Color.lightGray);
        jpnlMessage.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        container.add(jpnlMessage);
        
        jlblMessage = new JLabel(message, JLabel.CENTER);
        jlblMessage.setFont(font);
        jlblMessage.setSize(width, height);
        jlblMessage.setForeground(Color.blue);
        jlblMessage.setHorizontalTextPosition(JLabel.CENTER);
        jpnlMessage.add(jlblMessage, BorderLayout.CENTER);
        
        dealAction();
        
        return this;
    }
    
    private void dealAction() {
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                try {
                    result = rmiClient.invokeMethod(method, argus,false);
                    
                } catch (Exception e1) {
                     e1.printStackTrace();
                } finally {
                    exitDialog();
                }
            }
        });
    }
    
    public void showDialog() {
        setVisible(true);
    }
    
    public void exitDialog() {
        dispose();
    }

    public MyDialog() {
        super();
    }

    public MyDialog(Dialog owner, boolean modal) {
        super(owner, modal);
    }

    public MyDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public MyDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public MyDialog(Dialog owner, String title) {
        super(owner, title);
    }

    public MyDialog(Dialog owner) {
        super(owner);
    }

    public MyDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }

    public MyDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public MyDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public MyDialog(Frame owner, String title) {
        super(owner, title);
    }

    public MyDialog(Frame owner) {
        super(owner);
    }

    public MyDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    public MyDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
    }

    public MyDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
    }

    public MyDialog(Window owner, String title) {
        super(owner, title);
    }

    public MyDialog(Window owner) {
        super(owner);
    }

}
