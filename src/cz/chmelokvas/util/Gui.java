package cz.chmelokvas.util;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import cz.chmelokvas.brewery.Brewery;
import cz.chmelokvas.brewery.Car;
import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Order;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Stock;
import cz.chmelokvas.brewery.Time;
import cz.chmelokvas.generate.Data;

/**
 * Graficke uzivatelske rozhrani
 * @author Lukas Cerny A13B0286P
 *
 */
public class Gui extends JFrame {
	
	/**
	 * Instance tridy - jedinacek
	 */
	private static final Gui INSTANCE = new Gui();
	
	private static final long serialVersionUID = 1L;
	/**
	 * Urcuje, zda je simulace pozastavena
	 */
	public boolean stopAndPlay = true;
	
	/**
	 * Slouzi k nenasilnemu ukonceni simulace
	 */
	public boolean end = false;
	
	/**
	 * JKOMPONENTY
	 */
	public JSlider jSlider1 = new JSlider(0, 1000);
	private final JButton jButton1 = new JButton();
	private final JButton jButton2 = new JButton();
	private final JLabel jLabel1 = new JLabel();
	private final JTextArea jTextArea1 = new JTextArea();
	private final JTextArea jTextArea2 = new JTextArea();
	private final JTextArea jTextArea3 = new JTextArea();
	private final JTextArea jTextArea4 = new JTextArea();
	private final JTextArea jTextArea5 = new JTextArea();
	private final JTextArea jTextArea6 = new JTextArea();
	private final JTextArea jTextArea7 = new JTextArea();
	private final JTextField jTextField1 = new JTextField();
	private final JTextField jTextField2 = new JTextField();
    private final JTabbedPane jTabbedPane1 = new JTabbedPane();
    private final JLabel jLabel2 = new JLabel();
    private final JLabel jLabel3 = new JLabel();
    private final JLabel jLabel4 = new JLabel();
    private final JLabel jLabel5 = new JLabel();
    private final JLabel jLabel6 = new JLabel();
    private final JLabel jLabel7 = new JLabel();
    private final JLabel jLabel8 = new JLabel();
    private final JPanel jPanel1 = new JPanel();
    private final JPanel jPanel2 = new JPanel();
    private final JPanel jPanel3 = new JPanel();
    private final JPanel jPanel4 = new JPanel();
    private final JPanel jPanel5 = new JPanel();
    private final JPanel jPanel6 = new JPanel();
    private final JScrollPane jScrollPane1 = new JScrollPane();
    private final JScrollPane jScrollPane2 = new JScrollPane();
    private final JScrollPane jScrollPane3 = new JScrollPane();
    private final JScrollPane jScrollPane4 = new JScrollPane();
    private final JScrollPane jScrollPane5 = new JScrollPane();
    private final JScrollPane jScrollPane6 = new JScrollPane();
    private final JScrollPane jScrollPane7 = new JScrollPane();
    private final JMenuBar menuBar = new JMenuBar();
    private JFileChooser fileChooser;
    
    /**
     * privatni konstruktor
     */
    private Gui() {
        initComponents();
    }
    
    /**
     * Vraci jedinacka tridy
     * @return jedinacek
     */
    public static Gui getInstance(){
		return INSTANCE;
	}

    @Override
    public void dispose(){
    	end = true;
    	super.dispose();
    }
    
    /**
     * Vyprazdni text arey od predchozich vypisu
     */
    private void cleanTextAreas(){
    	jTextArea2.setText("");
    	jTextArea3.setText("");
    	jTextArea4.setText("");
    	jTextArea5.setText("");
    	jTextArea6.setText("");
    	jTextArea7.setText("");
    }
    
    /**
     * Inicializace kopmonent
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        
        //inicializace hlavni nabidky
        initFileMenu();
        initStatMenu();
        setJMenuBar(menuBar);
       
        //inicializace textarey
        initTextArea3();
        initTextArea4();
        initTextArea5();
        initTextArea7();
        initTextArea6();       
        initTextArea2();
        initTextAreaStat();       
        
        //inicializace formulare pro vytvareni rucni objednavky
        initOrderForm();

        //inicializace tlacitka pro pozastaveni simulace
        initStopAndPlay();
        
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        setHorizontalGroup(layout);
        setVerticalGroup(layout);
        
        pack();
        setVisible(true);
    }
    
    /**
     * Inicializace textArey3
     */
    private void initTextArea3(){
    	jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);
        setAutoScroll(jTextArea3);

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
    }
    
    /**
     * Inicializace textArey4
     */
    private void initTextArea4(){
    	jTextArea4.setEditable(false);
        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);
        setAutoScroll(jTextArea4);
        
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
    }
    
    /**
     * Inicializace textArey5
     */
    private void initTextArea5(){
    	jTextArea5.setEditable(false);
        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane5.setViewportView(jTextArea5);
        setAutoScroll(jTextArea5);

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
    }
    
    /**
     * Inicializace textArey7
     */
    private void initTextArea7(){
    	jTextArea7.setEditable(false);        
        jTextArea7.setColumns(20);
        jTextArea7.setRows(5);
        jScrollPane7.setViewportView(jTextArea7);
        setAutoScroll(jTextArea7);

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

        jTabbedPane1.addTab("PRODUKCE", jPanel5);
    }
    
    /**
     * Inicializace textArey6
     */
    private void initTextArea6(){
    	jPanel6.setAlignmentY(30.0F);

        jTextArea6.setEditable(false);
        jTextArea6.setColumns(20);
        jTextArea6.setRows(5);
        jScrollPane6.setViewportView(jTextArea6);
        setAutoScroll(jTextArea6);

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

        jTabbedPane1.addTab("INFORMACE O OBJEKTU", jPanel6);
    }
    
    /**
     * Inicializace textArey2
     */
    private void initTextArea2(){
    	jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);
        setAutoScroll(jTextArea2);

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
    }
    
    /**
     * inicializace textArey pro statistiku
     */
    private void initTextAreaStat(){
    	jLabel1.setFont(new Font("Dialog", 1, 36));
        
        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        
        jLabel2.setText("Statistika");

    }
    
    /**
     * Inicializace formulare pro zadavani rucnich objednavek
     */
    private void initOrderForm(){
    	//Zadavani objednavky
        jLabel3.setText("Zadani objednavky");
        jLabel4.setText("Hospoda");
        jLabel5.setText("Mnozstvi");
        jButton1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jButton1.setText("Odeslat");
        jButton1.setEnabled(false);
        jButton1.addActionListener((e)->{
        	manualOrder();
        });
    }
    
    /**
     * Inicializace tlacitka pro pozastaveni simulace
     */
    private void initStopAndPlay(){
    	//Tlacitko na spusteni/pozastaveni simulace
        jLabel7.setText("Rychlost simulace");
        jButton2.setText(">||");
        jButton2.setEnabled(false);
        jButton2.addActionListener((e)->{
        	stopAndPlay();
        });
    }
    
    /**
     * Inicializace nabidky statistiky o objektech
     */
    private void initStatMenu(){
        JMenu statMenu = new JMenu("Statistiky o objektech");
        menuBar.add(statMenu);   

        JMenuItem vybrat = new JMenuItem("Vybrat objekt", KeyEvent.VK_S);
        statMenu.add(vybrat);
        
        vybrat.addActionListener((e)->{
        	selectLoggable();
        });
    }
    
    /**
     * Inicializace nabidky Soubor
     */
    private void initFileMenu(){
    	JMenu fileMenu = new JMenu("Soubor");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem menuItemOpen = new JMenuItem("Otevrit", KeyEvent.VK_N);
        menuItemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	openFile();
            }
          });
        
        fileMenu.add(menuItemOpen);
        
        
        JMenuItem newMenuItemGen = new JMenuItem("Generovat", KeyEvent.VK_G);
        fileMenu.add(newMenuItemGen);        
        newMenuItemGen.addActionListener((e)->{
        	generateFile();
        });
        
        JMenuItem exit = new JMenuItem("Zavrit", KeyEvent.VK_G);
        fileMenu.add(exit);
        
        exit.addActionListener((e)->{
        	end = true;
        	System.exit(0);
        });
    }

    /**
     * Rozlozeni layoutu - generovano programem NetBeans
     * @param layout rozlozeni
     */
    private void setVerticalGroup(GroupLayout layout){
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
    }
    
    /**
     * Rozlozeni layoutu - generovano programem NetBeans
     * @param layout rozlozeni
     */
    private void setHorizontalGroup(GroupLayout layout){
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
    }
    
    /**
     * Vytvoreni rucni objednavky
     */
    private void manualOrder() {
        String pubS = jTextField2.getText();
        String amountS = jTextField1.getText();
        int pub, amount;
        //parsovani hodnot
        try{
        	pub = Integer.parseInt(pubS);
        	amount = Integer.parseInt(amountS);
        }catch(Exception e){
        	JOptionPane.showMessageDialog(this, "Mus�te zadat ��slo", "Chybn� vstup", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        //kontrola mnozstvi
        if(amount < 1 || amount > 6){
        	JOptionPane.showMessageDialog(this, "Neplatn� mno�stv� sud�/hl", "Chybn� vstup", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        Controller c = Controller.c;
        
        //kontrola id
        if(pub < (c.dock.size() + 1) || pub >= c.nodes.size()){
        	JOptionPane.showMessageDialog(this, "Neplatn� id hospody", "Chybn� vstup", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        //zadani objednavky
        Pub p = (Pub)c.nodes.get(pub);
        if(p.getCustomOrder() == null){
        	Order o = new Order(new Time(c.mainTime.value()), p, amount);
        	p.setCustomOrder(o);
        	if(p.isTank()){
        		c.brewery.recieveOrder(o);
        	}else{
        		p.getProvider().recieveOrder(o);
        	}
        }else {
        	JOptionPane.showMessageDialog(this, "Tato hospoda ji� ru�n� zadanou objedn�vku m�.", "Objedn�vka u� existuje", JOptionPane.WARNING_MESSAGE);
        	return;
        }
        
    }
    
    /**
     * Pozastaveni/pokracovani simulace
     */
    private void stopAndPlay() {
        if(stopAndPlay){
            stopAndPlay = false;
            jButton1.setEnabled(stopAndPlay);
        }else{
            stopAndPlay = true;
            if(Controller.c.mainTime.getHour() >= 8 && Controller.c.mainTime.getHour() < 16){
            	jButton1.setEnabled(stopAndPlay);
            }
        }
        
    }
    
    /**
     * Vyber objektu o kterem budeme vypisovat statistiky
     */
    private void selectLoggable(){
    	if(Controller.c == null){
    		JOptionPane.showMessageDialog(this, "Nejdrive musite spustit simulaci",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	String s = (String)JOptionPane.showInputDialog(
    	                    this,
    	                    "Zadejte oznaceni:\n"
    	                    + "typ-id\n"
    	                    + "typ:\n"
    	                    + "p - pivovar\n"
    	                    + "r - prekladiste\n"
    	                    + "k - kamion\n"
    	                    + "c - cisterna\n"
    	                    + "n - nakladak\n"
    	                    + "h - hospoda",
    	                    "Vyber sledovaneho objektu",
    	                    JOptionPane.INFORMATION_MESSAGE);
    	if (s != null) {
    		if(s.length() > 0){
    			selectLoggableObject(s);
    		}else {
    			JOptionPane.showMessageDialog(this, "Nejdrive musite neco zadat", "Chyba vstupu", JOptionPane.WARNING_MESSAGE);
    		}
    	    
    	}
    }
    
    /**
     * Vybere pivovar pro vypisovani statistik
     * @param data oznaceni v retezci
     */
    private void setLoggableBrewery(String[] data){
    	int id = - 1;
		
		try{
			id = Integer.parseInt(data[1]);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Musite pouzit cislo",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(id != 0){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Logger.getInstance().setLoggableObject(Controller.c.brewery);
    }
    
    /**
     * Vybere prekladiste pro vypisovani statistik
     * @param data oznaceni v retezci
     */
    private void setLoggableDock(String[]data){
    	int id = -1;
		
		try{
			id = Integer.parseInt(data[1]);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Musite pouzit cislo",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(id < 1 || id > Controller.c.dock.size()){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Logger.getInstance().setLoggableObject((Stock)Controller.c.nodes.get(id));
    }
   
    /**
     * Vybere kamion pro vypisovani statistik
     * @param data oznaceni v retezci
     */
    private void setLoggableCamion(String[] data){
    	int idStock = -1, idCam = -1;
		
		try{
			idStock = Integer.parseInt(data[1]);
			idCam = Integer.parseInt(data[2]);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Musite pouzit cislo",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(idStock != 0){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		List<Car> garage = ((Brewery)Controller.c.nodes.get(idStock)).getCamionGarage();
		
		if(idCam < 0 || idCam >= garage.size()){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
		}
		
		
		Logger.getInstance().setLoggableObject(garage.get(idCam));
    }
    
    private void setLoggableCistern(String[] data){
    	int idStock = -1, idCist = -1;
		
		try{
			idStock = Integer.parseInt(data[1]);
			idCist = Integer.parseInt(data[2]);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Musite pouzit cislo",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(idStock != 0){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		List<Car> garage = ((Brewery)Controller.c.nodes.get(idStock)).getCisternGarage();
		
		if(idCist < 0 || idCist >= garage.size()){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
		}
		
		
		Logger.getInstance().setLoggableObject(garage.get(idCist));
    }
    
    /**
     * Vybere nakladak pro vypisovani statistik
     * @param data oznaceni v retezci
     */
    private void setLoggableTruck(String[] data){
    	int idStock = -1, idN = -1;
		
		try{
			idStock = Integer.parseInt(data[1]);
			idN = Integer.parseInt(data[2]);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Musite pouzit cislo",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(idStock <= 0 || idStock > Controller.c.dock.size()){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		List<Car> garage = ((Dock)Controller.c.nodes.get(idStock)).getGarage();
		
		if(idN < 0 || idN >= garage.size()){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
		}
		
		
		Logger.getInstance().setLoggableObject(garage.get(idN));
    }
    
    /**
     * Vybere hospodu pro vypisovani statistik
     * @param data oznaceni v retezci
     */
    private void setLoggablePub(String[] data){
    	int	id = -1;
		
		try{
			id = Integer.parseInt(data[1]);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Musite pouzit cislo",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(id < Controller.c.dock.size() || id > Controller.c.nodes.size()){
			JOptionPane.showMessageDialog(this, "Neplatne id",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Logger.getInstance().setLoggableObject((Pub)Controller.c.nodes.get(id));
    }
    
    /**
     * Rozparzuje s nastavi objekt o kterem vypisujeme statistiky
     * @param s oznaceni v retezci
     */
    public void selectLoggableObject(String s){

    	String[] data = s.split("-");
    	if(data[0].length() > 1){
    		JOptionPane.showMessageDialog(this, "Neplatny typ objektu",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	switch(data[0].charAt(0)){
    		case 'p': setLoggableBrewery(data);break;
    		
    		case 'r': setLoggableDock(data);break;
    		
    		case 'k': setLoggableCamion(data);break;
    		
    		case 'c': setLoggableCistern(data);break;
    		
    		case 'n': setLoggableTruck(data);break;
    		
    		case 'h': setLoggablePub(data);break;
    		
    		default:JOptionPane.showMessageDialog(this, "Neplatny typ objektu",
    				"Chyba vstupu", JOptionPane.WARNING_MESSAGE);
    		
    	}
    }
    
    /**
     * Generovani dat do souboru vybraneho pomoci filechooseru
     */
    private void generateFile(){
    	fileChooser = new JFileChooser();
    	int returnValue = fileChooser.showSaveDialog(null);
      
    	if (returnValue == JFileChooser.APPROVE_OPTION) {
    		File f = fileChooser.getSelectedFile();
           	Data d = new Data(f.getAbsolutePath());
           	d.toString();
           	JOptionPane.showMessageDialog(null, "Generovani dokonceno", "Hotovo", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
    /**
     * Otevre soubor, ktery mu vybral uzivatel
     */
    private void openFile(){
    	fileChooser = new JFileChooser();

        FileFilter filter = new FileNameExtensionFilter("TXT soubory", "txt");
        fileChooser.setFileFilter(filter);
    	
    	 int returnValue = fileChooser.showOpenDialog(null);
         if (returnValue == JFileChooser.APPROVE_OPTION) {
        	 end = true;
         	stopAndPlay = true;
        	 File f = fileChooser.getSelectedFile();
         	try{
         		
         		cleanTextAreas();
         		end = false;
         		Main.initController(f.getAbsolutePath());
         		jButton2.setEnabled(true);
             }catch(IllegalArgumentException e1){
            	JOptionPane.showMessageDialog(null, "Soubor je bud poskozeny, nebo neobsahuje predepsana data.", "Chyba souboru", JOptionPane.INFORMATION_MESSAGE);
             }
         }
    }
    
    /**
     * Nastavi textAree autoscroll
     * @param a textArea
     */
    private void setAutoScroll(JTextArea a){
    	((DefaultCaret) a.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
    
    /**
     * Vrati textAreu statistik
     * @return textArea statistik
     */
    public JTextArea getAreaStatistika(){
    	return jTextArea1;
    }

    public JTextArea getArea1(){
    	return jTextArea2;
    }
    public JTextArea getArea2(){
    	return jTextArea3;
    }
    public JTextArea getArea3(){
    	return jTextArea4;
    }
    public JTextArea getArea4(){
    	return jTextArea5;
    }
    public JTextArea getArea5(){
    	return jTextArea6;
    }
    public JTextArea getArea6(){
    	return jTextArea7;
    }
    /**
     * Nastavi jLabelu nazev hospody
     */
    public void setName(String name){
    	jLabel1.setText(name);
    }
}
