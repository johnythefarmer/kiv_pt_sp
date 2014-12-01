package cz.chmelokvas.util;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NewJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean stopAndPlay = true;
	
	private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JScrollPane jScrollPane6;
    private JScrollPane jScrollPane7;
    private JSlider jSlider1;
    private JTabbedPane jTabbedPane1;
    private JTextArea jTextArea1;
    private JTextArea jTextArea2;
    private JTextArea jTextArea3;
    private JTextArea jTextArea4;
    private JTextArea jTextArea5;
    private JTextArea jTextArea6;
    private JTextArea jTextArea7;
    private JTextField jTextField1;
    private JTextField jTextField2;
    
    public NewJFrame() {
        initComponents();
    }

    private void initComponents() {

        jTabbedPane1 = new JTabbedPane();
        jPanel1 = new JPanel();
        jScrollPane2 = new JScrollPane();
        jTextArea2 = new JTextArea();
        jPanel2 = new JPanel();
        jScrollPane3 = new JScrollPane();
        jTextArea3 = new JTextArea();
        jPanel3 = new JPanel();
        jScrollPane4 = new JScrollPane();
        jTextArea4 = new JTextArea();
        jPanel4 = new JPanel();
        jScrollPane5 = new JScrollPane();
        jTextArea5 = new JTextArea();
        jPanel6 = new JPanel();
        jScrollPane6 = new JScrollPane();
        jTextArea6 = new JTextArea();
        jPanel5 = new JPanel();
        jScrollPane7 = new JScrollPane();
        jTextArea7 = new JTextArea();
        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jButton1 = new JButton();
        jSlider1 = new JSlider(0, 50);
        jLabel7 = new JLabel();
        jButton2 = new JButton();
        jLabel6 = new JLabel();
        jLabel8 = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CHYBY", jPanel1);

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("VYRIZENI OBJ.", jPanel2);

        jTextArea4.setEditable(false);
        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("PODANI OBJ.", jPanel3);

        jTextArea5.setEditable(false);
        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane5.setViewportView(jTextArea5);

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("POHYB AUT", jPanel4);

        jPanel6.setAlignmentY(30.0F);

        jTextArea6.setEditable(false);
        jTextArea6.setColumns(20);
        jTextArea6.setRows(5);
        jScrollPane6.setViewportView(jTextArea6);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("ZASOB. DO PREKLADIST", jPanel6);

        jTextArea7.setEditable(false);
        jTextArea7.setColumns(20);
        jTextArea7.setRows(5);
        jScrollPane7.setViewportView(jTextArea7);

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("VSE", jPanel5);

        jLabel1.setFont(new Font("Dialog", 1, 36));
        
        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Statistika");

        jLabel3.setText("Zadani objednavky");

        jTextField1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Hospoda");

        jLabel5.setText("Mnozstvi");

        jButton1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jButton1.setText("Odeslat");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                manualOrder(evt);
            }
        });
        
        jSlider1.setLabelTable(jSlider1.createStandardLabels(10));
        jSlider1.setMinorTickSpacing(2);
        jSlider1.setMajorTickSpacing(10);
        jSlider1.setPaintTicks(true);
        jSlider1.setPaintLabels(true);
        
        jSlider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
            	slider(event);
            }
        });

        jLabel7.setText("Rychlost simulace");

        jButton2.setText(">||");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopAndPlay(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel6.setText("Den:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 634, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
                                    .addComponent(jScrollPane1, Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
                                            .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton2))
                                            .addGroup(Alignment.LEADING, layout.createParallelGroup(Alignment.TRAILING)
                                                .addComponent(jLabel7, Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(41, 41, 41)
                                                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(39, 39, 39)
                                                    .addComponent(jButton1))))))
                                .addContainerGap(24, Short.MAX_VALUE))
                            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addGap(145, 145, 145))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane1))
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel7)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                            .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
                                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        pack();
        setVisible(true);
    }

    
    private void jTextField1ActionPerformed(ActionEvent evt) {}

    private void jTextField2ActionPerformed(ActionEvent evt) {}

    private void manualOrder(ActionEvent evt) {
        String hospoda = jTextField2.getText();
        String mnozstvi = jTextField1.getText();
        jTextArea1.append("Hospoda: "+hospoda+" mnozstvi: " + mnozstvi +" sudu/hl.");
    }

    private void slider(ChangeEvent event){
    	 int value_pom = jSlider1.getValue();
         jTextArea1.append(value_pom+"\n");
    }
    
    private void stopAndPlay(ActionEvent evt) {
        if(stopAndPlay){
            jTextArea1.append("Stop\n");
            stopAndPlay = false;
        }else{
            jTextArea1.append("Start\n");
            stopAndPlay = true;
        }
    }

    private void addToArea(){
        // zmena dnu
        jLabel8.setText("1");
        jLabel1.setText("Chmelokvas");
        
        // append to tabbed
        for(int i = 1; i < 8; i++) jTextArea1.append("Statistika "+i+"\n");
        for(int i = 1; i < 31; i++) jTextArea2.append("Hospody "+i+"\n");
        for(int i = 1; i < 31; i++) jTextArea3.append("Prekladiste "+i+"\n");
        for(int i = 1; i < 31; i++) jTextArea4.append("Pivovar "+i+"\n");
        for(int i = 1; i < 31; i++) jTextArea5.append("Auta "+i+"\n");
        for(int i = 1; i < 31; i++) jTextArea6.append("Cisterny "+i+"\n");
        for(int i = 1; i < 31; i++) jTextArea7.append("Celkem "+i+"\n");
    }
    

    public static void main(String args[]) {
    	NewJFrame nf = new NewJFrame();
    	nf.addToArea();
    }
}
