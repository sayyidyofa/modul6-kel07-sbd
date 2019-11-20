/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modul6;

import TabelData.GameData;
import TabelData.GameTable;
import TabelData.PlatformData;
import TabelData.PlatformTable;
import java.lang.reflect.Field;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author Administrator
 */
public class MainForm extends javax.swing.JFrame {
    
    private String pd_kodePlatform;
    private String pd_namaPlatform;
    private String pd_tglRilis;
    private String mf_idgame;
    private String mf_judulgame;
    private String mf_genre;
    private String mf_rilis;
    private String mf_developer;
    private String mf_kodeplatform;
    private String mf_platform;
    private String output;
    private GameTable tabelgame;
    private PlatformTable tabelplatform;
    private dbconnections db_conn;
    private Statement script;
    
    private void showGameData(){
        try {
            int row = tabelGame.getRowCount();
            for(int i=0;i<row;i++){
                tabelgame.delete(0, row);
            }
            String sql = "SELECT * from DB_GAME";
            ResultSet rsShow = db_conn.script.executeQuery(sql);
            while(rsShow.next()){
                GameData d = new GameData();
                d.setIdgame(rsShow.getString("IDGAME"));
                d.setJudulgame(rsShow.getString("JUDULGAME"));
                d.setGenre(rsShow.getString("GENRE"));
                d.setRilis(rsShow.getString("RILIS"));
                d.setDeveloper(rsShow.getString("DEVELOPER"));
                d.setPlatform(rsShow.getString("PLATFORM"));
                d.setKodeplatform(rsShow.getString("KODEPLATFORM"));
                tabelgame.add(d);
            }
        }catch(Exception e){
            System.err.print(e);
        }
    }
    public String fillGameData(GameData dm){
        gameIDLabel.setText(dm.getIdgame());
        gameField.setText(dm.getJudulgame());
        genreField.setText(dm.getGenre());
        rilisField.setText (dm.getRilis());
        developerField.setText (dm.getDeveloper());
        kodeplatformField.setText (dm.getKodeplatform());
        insertGameButton.setEnabled(false);
        return dm.getIdgame();
    }
    private void insertGameData(){
        mf_judulgame = gameField.getText();
        mf_genre = genreField.getText();
        mf_rilis = rilisField.getText();
        mf_developer = developerField.getText();
        mf_kodeplatform = kodeplatformField.getText();
        List<String> paramList = Arrays.asList(mf_judulgame, mf_genre, mf_rilis, mf_developer, mf_kodeplatform);
        if(!paramList.contains(null) && !paramList.contains("")){
            try{
                String cek_platform = "SELECT KODEPLATFORM from PLATFORM";
                ResultSet result = db_conn.script.executeQuery(cek_platform);
                List<String> KPlist = new ArrayList<>();
                while(result.next()) KPlist.add(result.getString(1));
                if (!KPlist.contains(mf_kodeplatform)) JOptionPane.showMessageDialog(null, "Kode platform tidak ada!");
                else {
                    String sql = "INSERT INTO GAME (idgame, judulgame, genre, rilis, developer, kodeplatform) "
                        + "values (SEQ_GAME.nextval+1,'" +mf_judulgame+ "','"+mf_genre+"','"+mf_rilis+"','" +mf_developer+ "',"+mf_kodeplatform+")";
                    System.out.println(sql);
                    db_conn.script.executeUpdate(sql);
                JOptionPane.showMessageDialog(null,"Data berhasil disimpan");
                }
                
            }
            catch(SQLSyntaxErrorException e) {
                JOptionPane.showMessageDialog(null, "Harap memasukkan data dg benar");
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
            showGameData();
            clearGameForm();
        }else{
            JOptionPane.showMessageDialog(null,"Setiap kolom harus diisi!");
        }
    }
    private void deleteGameData(){
    int app;
    mf_idgame = gameIDLabel.getText();
    if ((app=JOptionPane.showConfirmDialog(null,"Yakin ingin hapus data?","Hapus Data", JOptionPane.YES_NO_OPTION))==0){
    try {
        String sqlid = "SELECT IDGAME from GAME where idgame="+mf_idgame+"";
        ResultSet rsShow = db_conn.script.executeQuery(sqlid);
        while (rsShow.next()){
	output = rsShow.getString(1);
}
        String sql = "DELETE from GAME where idgame ="+mf_idgame+"";
        db_conn.script.executeUpdate(sql);
        showGameData();
        JOptionPane.showMessageDialog (null,"Berhasil dihapus");
        clearGameForm();
        enableMainButtons();
    }
    catch (SQLException e){
        System.err.print(e);
            }
        }
    }
    
    private void updateGameData(String mf_idgame){
        int app;
                       
        if((app = JOptionPane.showConfirmDialog(null, "Yakin ingin update date?","Ubah Data",JOptionPane.YES_NO_OPTION))==0){
        	try{ //Query untuk update pada table database
                    
                    mf_idgame = gameIDLabel.getText();
                    mf_judulgame = gameField.getText();
                    mf_genre = genreField.getText();
                    mf_rilis = rilisField.getText();
                    mf_developer = developerField.getText();
                    mf_kodeplatform = kodeplatformField.getText();
                    String cek_platform = "SELECT KODEPLATFORM from PLATFORM";
                    ResultSet result = db_conn.script.executeQuery(cek_platform);
                    List<String> KPlist = new ArrayList<>();
                    while(result.next()) KPlist.add(result.getString(1));
                    if (!KPlist.contains(mf_kodeplatform)) JOptionPane.showMessageDialog(null, "Kode platform tidak ada!");
                    else{
                        String sqlid = "SELECT IDGAME from GAME where idgame=" +mf_idgame+ "";
                        ResultSet rsShow = db_conn.script.executeQuery(sqlid);
                        while (rsShow.next()){
                        output = rsShow.getString(1);
                        } 
                        String sql = "UPDATE GAME SET judulgame='"+mf_judulgame+"',genre='"+mf_genre+"',rilis='"+mf_rilis+"',developer='"+mf_developer+"',kodeplatform="+mf_kodeplatform+" where idgame="+mf_idgame+"" ;
       			db_conn.script.executeUpdate(sql);
        		showGameData();
                        enableMainButtons();
                        //menampilkan message dialog bahwa data telah update
                        JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        		clearGameForm();
                    }
                }
        	catch(SQLException ex){
        	System.err.print(ex);
        }
      }
    }
    
    private void showPlatformData(){
        try {
            int row = tabelPlatform.getRowCount();
            for(int i=0;i<row;i++){
                tabelplatform.delete(0, row);
            }
            String sql = "SELECT * from PLATFORM";
            ResultSet rsShow = db_conn.script.executeQuery(sql);
            while(rsShow.next()){
                PlatformData p = new PlatformData();
                p.setKodeplatform(rsShow.getString("KODEPLATFORM"));
                p.setPlatform(rsShow.getString("PLATFORM"));
                p.setRilis(rsShow.getString("RILIS"));
                tabelplatform.add(p);
            }
        }catch(Exception e){
            System.err.print(e);
        }
    }
    public String fillPlatformData(PlatformData pd){
        kodePlatformLabel.setText(pd.getKodeplatform());
        namaPlatformField.setText(pd.getPlatform());
        tglRilisField.setText(pd.getRilis());
        insertPlatformButton.setEnabled(false);
        return pd.getKodeplatform();
    }
    private void insertPlatformData(){
        pd_namaPlatform = namaPlatformField.getText();
        pd_tglRilis = tglRilisField.getText();
        List<String> paramList = Arrays.asList(pd_namaPlatform, pd_tglRilis);
        if(!paramList.contains(null) && !paramList.contains("")){
            try{
                
                String sql = "INSERT INTO PLATFORM (kodeplatform, platform, rilis) "
                        + "values (SEQ_PLATFORM.nextval+1,'" +pd_namaPlatform+ "','"+pd_tglRilis+"')";
                System.out.println(sql);
                db_conn.script.executeUpdate(sql);
                JOptionPane.showMessageDialog(null,"Data berhasil disimpan");
            }
            catch(SQLSyntaxErrorException e) {
                JOptionPane.showMessageDialog(null, "Harap memasukkan data dg benar");
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
            showPlatformData();
            clearPlatformForm();
        }else{
            JOptionPane.showMessageDialog(null,"Setiap kolom harus diisi!");
        }
    }
    private void updatePlatformData(String pd_updateParam){
        int app;
                       
        if((app = JOptionPane.showConfirmDialog(null, "Yakin ingin update date?","Ubah Data",JOptionPane.YES_NO_OPTION))==0){
        	try{ //Query untuk update pada table database
                    pd_kodePlatform = kodePlatformLabel.getText();
                    pd_namaPlatform = namaPlatformField.getText();	
                    pd_tglRilis = tglRilisField.getText();
                    String sqlid = "SELECT KODEPLATFORM from PLATFORM where KODEPLATFORM=" +pd_kodePlatform+ "";
                    ResultSet rsShow = db_conn.script.executeQuery(sqlid);
                    
                        String sql = "UPDATE PLATFORM SET PLATFORM='"+pd_namaPlatform+"',RILIS='"+pd_tglRilis+"' where KODEPLATFORM="+pd_kodePlatform+"" ;
       			db_conn.script.executeUpdate(sql);
        		showPlatformData();
                        enablePlatformButtons();
		//menampilkan message dialog bahwa data telah update
        	JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        		clearPlatformForm();
        	}
        	catch(SQLException ex){
        	System.err.print(ex);
        }
      }
    }
    private void deletePlatformData(){
    int app;
    pd_kodePlatform = kodePlatformLabel.getText();
    if ((app=JOptionPane.showConfirmDialog(null,"Yakin ingin hapus data?","Hapus Data", JOptionPane.YES_NO_OPTION))==0){
    try {
        String sqlid = "SELECT KODEPLATFORM from PLATFORM where KODEPLATFORM="+pd_kodePlatform+"";
        ResultSet rsShow = db_conn.script.executeQuery(sqlid);
        
        String sql = "DELETE from PLATFORM where KODEPLATFORM ="+pd_kodePlatform+"";
        db_conn.script.executeUpdate(sql);
        showPlatformData();
        JOptionPane.showMessageDialog (null,"Berhasil dihapus");
        clearPlatformForm();
        enablePlatformButtons();
    }
    catch (SQLException e){
        System.err.print(e);
            }
        }
    }

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        tabbedPane.setEnabledAt(0, false);
        tabbedPane.setEnabledAt(1, false);
        db_conn = new dbconnections();
        tabelgame = new GameTable();
        tabelGame.setModel(tabelgame);
        showGameData();
    }
    public void clearGameForm(){
        gameField.setText(null);
        genreField.setText(null);
        rilisField.setText(null);
        developerField.setText (null);
        kodeplatformField.setText (null);
        gameIDLabel.setText(null);
    }
    public void clearPlatformForm(){
        namaPlatformField.setText(null);
        tglRilisField.setText(null);
        kodePlatformLabel.setText(null);
    }
    public void enableMainButtons(){
    	insertGameButton.setEnabled(true);
    	deleteGameButton.setEnabled(true);
    	updateGameButton.setEnabled(true);
    }
    public void enablePlatformButtons(){
    	insertPlatformButton.setEnabled(true);
    	deletePlatformButton.setEnabled(true);
    	updatePlatformButton.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelGame = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        gameField = new javax.swing.JTextField();
        genreField = new javax.swing.JTextField();
        rilisField = new javax.swing.JTextField();
        developerField = new javax.swing.JTextField();
        kodeplatformField = new javax.swing.JTextField();
        gameIDLabel = new javax.swing.JLabel();
        insertGameButton = new javax.swing.JButton();
        updateGameButton = new javax.swing.JButton();
        deleteGameButton = new javax.swing.JButton();
        gameSearchField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        addPlatformButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        platformPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelPlatform = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        namaPlatformField = new javax.swing.JTextField();
        tglRilisField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        kodePlatformLabel = new javax.swing.JLabel();
        insertPlatformButton = new javax.swing.JButton();
        updatePlatformButton = new javax.swing.JButton();
        deletePlatformButton = new javax.swing.JButton();
        kembaliKeMain = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabelGame.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Game", "Judul Game", "Genre", "Tanggal Rilis", "Pengembang", "Platform"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelGameMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelGame);

        jLabel1.setText("Judul Game");

        jLabel2.setText("Genre");

        jLabel3.setText("Tanggal Rilis");

        jLabel4.setText("Pengembang");

        jLabel5.setText("Kode Platform");

        genreField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genreFieldActionPerformed(evt);
            }
        });

        insertGameButton.setText("Insert");
        insertGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertGameButtonActionPerformed(evt);
            }
        });

        updateGameButton.setText("Update");
        updateGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGameButtonActionPerformed(evt);
            }
        });

        deleteGameButton.setText("Delete");
        deleteGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteGameButtonActionPerformed(evt);
            }
        });

        gameSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameSearchFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("Search");

        addPlatformButton.setText("Tambah Platform");
        addPlatformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlatformButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("ID: ");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(54, 54, 54)
                        .addComponent(gameField))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(80, 80, 80)
                        .addComponent(genreField, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(50, 50, 50)
                        .addComponent(rilisField))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(42, 42, 42)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(developerField)
                            .addComponent(kodeplatformField))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gameIDLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(updateGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(insertGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(gameSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jLabel7)))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addPlatformButton)
                        .addGap(38, 38, 38))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(gameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(gameIDLabel)
                        .addComponent(jLabel7)
                        .addComponent(jLabel6)))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(genreField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(rilisField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(developerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(kodeplatformField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(insertGameButton)
                            .addComponent(gameSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateGameButton)
                            .addComponent(addPlatformButton))
                        .addGap(18, 18, 18)
                        .addComponent(deleteGameButton)))
                .addGap(0, 23, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Game", mainPanel);

        tabelPlatform.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "KODE PLATFORM", "PLATFORM", "TANGGAL RILIS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelPlatform.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPlatformMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelPlatform);

        jLabel8.setText("Platform");

        jLabel9.setText("Tanngal Rilis");

        namaPlatformField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaPlatformFieldActionPerformed(evt);
            }
        });

        jLabel10.setText("Kode Platform");

        insertPlatformButton.setText("Insert");
        insertPlatformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertPlatformButtonActionPerformed(evt);
            }
        });

        updatePlatformButton.setText("Update");
        updatePlatformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePlatformButtonActionPerformed(evt);
            }
        });

        deletePlatformButton.setText("Delete");
        deletePlatformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePlatformButtonActionPerformed(evt);
            }
        });

        kembaliKeMain.setText("Kembali");
        kembaliKeMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembaliKeMainActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout platformPanelLayout = new javax.swing.GroupLayout(platformPanel);
        platformPanel.setLayout(platformPanelLayout);
        platformPanelLayout.setHorizontalGroup(
            platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(platformPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, platformPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(36, 36, 36)
                        .addComponent(tglRilisField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(platformPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(55, 55, 55)
                        .addComponent(namaPlatformField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(55, 55, 55)
                .addComponent(kodePlatformLabel)
                .addGap(60, 60, 60))
            .addGroup(platformPanelLayout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(platformPanelLayout.createSequentialGroup()
                        .addComponent(insertPlatformButton)
                        .addGap(147, 147, 147)
                        .addComponent(updatePlatformButton))
                    .addComponent(kembaliKeMain))
                .addGap(147, 147, 147)
                .addComponent(deletePlatformButton)
                .addGap(94, 94, 94))
        );
        platformPanelLayout.setVerticalGroup(
            platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(platformPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(namaPlatformField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(platformPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tglRilisField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(platformPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(kodePlatformLabel))))
                .addGap(40, 40, 40)
                .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertPlatformButton)
                    .addComponent(updatePlatformButton)
                    .addComponent(deletePlatformButton))
                .addGap(18, 18, 18)
                .addComponent(kembaliKeMain)
                .addGap(0, 21, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Platform", platformPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void genreFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genreFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genreFieldActionPerformed

    private void insertGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertGameButtonActionPerformed
        // TODO add your handling code here:
        insertGameData(); //SQLSyntaxErrorException
    }//GEN-LAST:event_insertGameButtonActionPerformed

    private void updateGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateGameButtonActionPerformed
        // TODO add your handling code here:
        if("".equals(gameIDLabel.getText()) || gameIDLabel.getText() == null) {
            JOptionPane.showMessageDialog(null, "Harap pilih data yg mau di-update!");
        }
        else {
            mf_idgame = gameIDLabel.getText();
            updateGameData(mf_idgame);
        }
    }//GEN-LAST:event_updateGameButtonActionPerformed

    private void deleteGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteGameButtonActionPerformed
        // TODO add your handling code here:
        if("".equals(gameIDLabel.getText()) || gameIDLabel.getText() == null) {
            JOptionPane.showMessageDialog(null, "Harap pilih data yg mau di-delete!");
        }
        else {
            deleteGameData();
            
        }
    }//GEN-LAST:event_deleteGameButtonActionPerformed

    private void tabelGameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelGameMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2){
        fillGameData(this.tabelgame.get(tabelGame.getSelectedRow()));
        insertGameButton.setEnabled(false);
        deleteGameButton.setEnabled(true);
        updateGameButton.setEnabled(true);
        }
    }//GEN-LAST:event_tabelGameMouseClicked

    private void namaPlatformFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaPlatformFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaPlatformFieldActionPerformed

    private void addPlatformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlatformButtonActionPerformed
        // TODO add your handling code here:
        tabelplatform = new PlatformTable();
        tabelPlatform.setModel(tabelplatform);
        tabbedPane.setSelectedIndex(1);
        showPlatformData();
    }//GEN-LAST:event_addPlatformButtonActionPerformed

    private void kembaliKeMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembaliKeMainActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(0);
        showGameData();
    }//GEN-LAST:event_kembaliKeMainActionPerformed

    private void tabelPlatformMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPlatformMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2){
        fillPlatformData(this.tabelplatform.get(tabelPlatform.getSelectedRow()));
        insertPlatformButton.setEnabled(false);
        deletePlatformButton.setEnabled(true);
        updatePlatformButton.setEnabled(true);
        }
    }//GEN-LAST:event_tabelPlatformMouseClicked

    private void insertPlatformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertPlatformButtonActionPerformed
        // TODO add your handling code here:
        insertPlatformData();
    }//GEN-LAST:event_insertPlatformButtonActionPerformed

    private void updatePlatformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePlatformButtonActionPerformed
        // TODO add your handling code here:
        if("".equals(kodePlatformLabel.getText()) || kodePlatformLabel.getText() == null) {
            JOptionPane.showMessageDialog(null, "Harap pilih data yg mau di-update!");
        }
        else {
            pd_kodePlatform = kodePlatformLabel.getText();
            updatePlatformData(pd_kodePlatform);
        }
    }//GEN-LAST:event_updatePlatformButtonActionPerformed

    private void deletePlatformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePlatformButtonActionPerformed
        // TODO add your handling code here:
        if("".equals(kodePlatformLabel.getText()) || kodePlatformLabel.getText() == null) {
            JOptionPane.showMessageDialog(null, "Harap pilih data yg mau di-delete!");
        }
        else {
            deletePlatformData();
        }
    }//GEN-LAST:event_deletePlatformButtonActionPerformed

    private void gameSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameSearchFieldActionPerformed
        // TODO add your handling code here:
        System.out.println(gameSearchField.getText());
        try {
            int row = tabelGame.getRowCount();
            for(int i=0;i<row;i++){
                tabelgame.delete(0, row);
            }
            String sql = "SELECT * from DB_GAME where lower(judulgame)=lower('" + gameSearchField.getText() + "')";
            ResultSet rsShow = db_conn.script.executeQuery(sql);
            while(rsShow.next()){
                GameData d = new GameData();
                d.setIdgame(rsShow.getString("IDGAME"));
                d.setJudulgame(rsShow.getString("JUDULGAME"));
                d.setGenre(rsShow.getString("GENRE"));
                d.setRilis(rsShow.getString("RILIS"));
                d.setDeveloper(rsShow.getString("DEVELOPER"));
                d.setPlatform(rsShow.getString("PLATFORM"));
                d.setKodeplatform(rsShow.getString("KODEPLATFORM"));
                tabelgame.add(d);
            }
        }catch(Exception e){
            System.err.print(e);
        }
    }//GEN-LAST:event_gameSearchFieldActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPlatformButton;
    private javax.swing.JButton deleteGameButton;
    private javax.swing.JButton deletePlatformButton;
    private javax.swing.JTextField developerField;
    private javax.swing.JTextField gameField;
    private javax.swing.JLabel gameIDLabel;
    private javax.swing.JTextField gameSearchField;
    private javax.swing.JTextField genreField;
    private javax.swing.JButton insertGameButton;
    private javax.swing.JButton insertPlatformButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton kembaliKeMain;
    private javax.swing.JLabel kodePlatformLabel;
    private javax.swing.JTextField kodeplatformField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField namaPlatformField;
    private javax.swing.JPanel platformPanel;
    private javax.swing.JTextField rilisField;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tabelGame;
    private javax.swing.JTable tabelPlatform;
    private javax.swing.JTextField tglRilisField;
    private javax.swing.JButton updateGameButton;
    private javax.swing.JButton updatePlatformButton;
    // End of variables declaration//GEN-END:variables
}
