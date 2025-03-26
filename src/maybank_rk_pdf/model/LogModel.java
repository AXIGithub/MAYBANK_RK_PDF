/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ratino
 */
public class LogModel {
    
    private ArrayList<Integer> idLog            = new ArrayList<Integer>(10);
    private ArrayList<String>barcode            = new ArrayList<String>(10);
    private ArrayList<String>idCustomer         = new ArrayList<String>(10);
    private ArrayList<String>name1              = new ArrayList<String>(10);
    private ArrayList<String>name2              = new ArrayList<String>(10);
    private ArrayList<String>name3              = new ArrayList<String>(10);
    private ArrayList<String>address1           = new ArrayList<String>(10);
    private ArrayList<String>address2           = new ArrayList<String>(10);
    private ArrayList<String>address3           = new ArrayList<String>(10);
    private ArrayList<String>address4           = new ArrayList<String>(10);
    private ArrayList<String>address5           = new ArrayList<String>(10);
    private ArrayList<String>address6           = new ArrayList<String>(10);
    private ArrayList<Integer>b1                = new ArrayList<Integer>(10);
    private ArrayList<Integer>b2                = new ArrayList<Integer>(10);
    private ArrayList<Integer>b3                = new ArrayList<Integer>(10);
//    private ArrayList<Integer>b4                = new ArrayList<Integer>(10);
    private ArrayList<String>b4                = new ArrayList<String>(10);
    private ArrayList<Integer>b5                = new ArrayList<Integer>(10);
    private ArrayList<Integer>b6                = new ArrayList<Integer>(10);
    private ArrayList<Integer>s1                = new ArrayList<Integer>(10);
    private ArrayList<Integer>s2                = new ArrayList<Integer>(10);
    private ArrayList<Integer>s3                = new ArrayList<Integer>(10);
    private ArrayList<Integer>s4                = new ArrayList<Integer>(10);
    private ArrayList<Integer>s5                = new ArrayList<Integer>(10);
    private ArrayList<Integer>s6                = new ArrayList<Integer>(10);
    private ArrayList<String>productName        = new ArrayList<String>(10);
    private ArrayList<String>courierName        = new ArrayList<String>(10);
    private ArrayList<Integer>seqPage            = new ArrayList<Integer>(10);
    private ArrayList<Integer>seqCustomer        = new ArrayList<Integer>(10);
    private ArrayList<Integer>seqEnvelope        = new ArrayList<Integer>(10);
    private ArrayList<String>ss1                = new ArrayList<String>(10);
    private ArrayList<String>ss2                = new ArrayList<String>(10);
    private ArrayList<String>ss3                = new ArrayList<String>(10);
    private ArrayList<String>ss4                = new ArrayList<String>(10);
    private ArrayList<String>ss5                = new ArrayList<String>(10);
    private ArrayList<String>ss6                = new ArrayList<String>(10);
    
    public void createdbKanwil(String pathDataKanwil, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXISTS t_kanwil");
        stmt.executeUpdate("CREATE TABLE t_kanwil"+
                            "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, kode_cabang VARCHAR(10) NOT NULL, kode_cabang_induk VARCHAR(10) NOT NULL," +
                            "cabang VARCHAR(100) NOT NULL, kanwil VARCHAR(100) NOT NULL) ENGINE = MYISAM");
        stmt.executeUpdate("LOAD DATA INFILE '" + pathDataKanwil + "' INTO TABLE t_kanwil(kode_cabang, kode_cabang_induk, cabang, kanwil)");
    }
    
    public void createTable(Statement stmt){
        try {
            stmt.executeUpdate("DROP TABLE IF EXISTS t_log");
            stmt.executeUpdate("CREATE TABLE t_log(id_log INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,barcode VARCHAR(100) NOT NULL, id_customer VARCHAR(100) NOT NULL, " +
                    "name1 VARCHAR(100) NOT NULL, name2 VARCHAR(100) NOT NULL, name3 VARCHAR(100) NOT NULL, address1 VARCHAR(100) NOT NULL, address2 VARCHAR(100) NOT NULL," +
                    "address3 VARCHAR(100) NOT NULL, address4 VARCHAR(100) NOT NULL, address5 VARCHAR(100) NOT NULL, address6 VARCHAR(100) NOT NULL, b1 INT NOT NULL, " +
                    "b2 INT NOT NULL, b3 INT NOT NULL, b4 CHAR(30) NOT NULL, b5 INT NOT NULL, b6 INT NOT NULL, s1 INT NOT NULL, s2 INT NOT NULL, s3 INT NOT NULL, s4 INT NOT NULL, " +
                    "s5 INT NOT NULL, s6 INT NOT NULL, product_name VARCHAR(100) NOT NULL, courier_name VARCHAR(100) NOT NULL, seq_page INT NOT NULL, seq_customer INT NOT NULL, " +
                    "seq_envelope INT NOT NULL, ss1 CHAR(30) NOT NULL, ss2 VARCHAR(5) NOT NULL, ss3 VARCHAR(100) NOT NULL, ss4 CHAR(30) NOT NULL, ss5 CHAR(30) NOT NULL, ss6 CHAR(30) NOT NULL) ENGINE = MYISAM");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index1 (product_name,name2)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index2 (product_name)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index3 (courier_name,product_name)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index4 (seq_envelope, product_name, name2)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index5 (id_customer)");
        } catch (SQLException ex) {
            Logger.getLogger(LogModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectTableForCustomer(Statement stmt){
        try {
            ResultSet hasilQuery = null;
            hasilQuery = stmt.executeQuery("SELECT * FROM t_log WHERE s6 = '1' ORDER BY ss2, courier_name, ss1, id_customer DESC");
            while(hasilQuery.next()){
                idCustomer.add(hasilQuery.getString("id_customer"));
                name1.add(hasilQuery.getString("name1")); // Nama Customer
                name2.add(hasilQuery.getString("name2")); // Kode Cabang
                name3.add(hasilQuery.getString("name3")); //CIF
                address1.add(hasilQuery.getString("address1"));
                address2.add(hasilQuery.getString("address2"));
                address3.add(hasilQuery.getString("address3"));
                address4.add(hasilQuery.getString("address4"));
                address5.add(hasilQuery.getString("address5"));
                address6.add(hasilQuery.getString("address6")); // Name File
                b1.add(hasilQuery.getInt("b1"));
                b2.add(hasilQuery.getInt("b2"));
                b3.add(hasilQuery.getInt("b3"));
                b4.add(hasilQuery.getString("b4"));
                b5.add(hasilQuery.getInt("b5"));
                b6.add(hasilQuery.getInt("b6"));
                s1.add(hasilQuery.getInt("s1")); // Kanwil
                s2.add(hasilQuery.getInt("s2"));
                s3.add(hasilQuery.getInt("s3"));
                s4.add(hasilQuery.getInt("s4"));
                s5.add(hasilQuery.getInt("s5"));
                s6.add(hasilQuery.getInt("s6"));
                
                productName.add(hasilQuery.getString("product_name"));
                courierName.add(hasilQuery.getString("courier_name"));
                ss1.add(hasilQuery.getString("ss1")); // Total Halaman
                ss2.add(hasilQuery.getString("ss2")); // Jenis Amplop
                ss3.add(hasilQuery.getString("ss3"));  // Nama Cabang
                ss4.add(hasilQuery.getString("ss4"));
                ss5.add(hasilQuery.getString("ss5"));
                ss6.add(hasilQuery.getString("ss6"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(LogModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadDataHold(String pathCourier, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXISTS t_hold");
        stmt.executeUpdate("CREATE TABLE t_hold"+
                           "(No VARCHAR(100) NOT NULL, no_rekening VARCHAR(100) NOT NULL, Name VARCHAR(100) NOT NULL, "
                           + "addr1 VARCHAR(100), addr2 VARCHAR(100), addr3 VARCHAR(100), addr4 VARCHAR(100), addr5 VARCHAR(100), post_code VARCHAR(20)) ENGINE = MYISAM");
        stmt.executeUpdate("LOAD DATA INFILE '" + pathCourier + "' INTO TABLE t_hold");
        
    }
    
    public void loadCouByCourierNCS(String path, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXISTS t_ncs");
        stmt.executeUpdate("CREATE TABLE t_ncs "
                           +"(no VARCHAR(5) NOT NULL, no_rekening VARCHAR(100) NOT NULL, nama VARCHAR(100) NOT NULL) ENGINE = MYISAM");
        stmt.executeUpdate("ALTER TABLE t_ncs ADD INDEX idx_no_rekening (no_rekening)");
        stmt.executeUpdate("LOAD DATA INFILE '" + path + "' INTO TABLE t_ncs");
    }
    
    public void loadCouByCourierPOS(String path, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXISTS t_pos");
        stmt.executeUpdate("CREATE TABLE t_pos "
                           +"(no VARCHAR(5) NOT NULL, no_rekening VARCHAR(100) NOT NULL, nama VARCHAR(100) NOT NULL) ENGINE = MYISAM");
        stmt.executeUpdate("LOAD DATA INFILE '" + path + "' INTO TABLE t_pos");
    }
    
    public void loadDataZipCode(String path, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXISTS t_courier_zip_code");
        stmt.executeUpdate("CREATE TABLE t_courier_zip_code"
                            + "(no INT NOT NULL AUTO_INCREMENT PRIMARY KEY, awal VARCHAR(100) NOT NULL, "
                            + "akhir VARCHAR(100) NOT NULL, kurir VARCHAR(100) NOT NULL) ENGINE = MYISAM");
        System.out.println("LOAD DATA INFILE " + path + "' INTO TABLE t_courier_zip_code");
        stmt.executeUpdate("LOAD DATA INFILE '" + path.replace("\\", "/") + "' INTO TABLE t_courier_zip_code (awal, akhir, kurir)");
    }
    
    public void loadLogData(String path, Statement stmt) throws SQLException{
        System.out.println("LOAD DATA INFILE '" + path + "' INTO TABLE t_log");
        stmt.executeUpdate("LOAD DATA INFILE '" + path + "' INTO TABLE t_log FIELDS TERMINATED BY '\\t' LINES TERMINATED BY '\\n' " +
                   "(barcode, id_customer, name1, name2, name3, address1, address2, address3, address4, address5, address6, " +
                   "b1, b2, b3, b4, b5, b6, s1, s2, s3, s4, s5, s6, product_name, courier_name, seq_page, seq_customer, seq_envelope, " +
                   "ss1, ss2, ss3, ss4, ss5, ss6)");
    }
    
    public void setKodeKanwil(Statement stmt){
        try {
            // Kode Kanwil di set pada kolom s1
            System.out.println("Set Kode Kanwil");
//        stmt.executeUpdate("UPDATE t_log JOIN t_kanwil ON t_log.name2 = t_kanwil.kode_cabang SET t_log.s1 = t_kanwil.kanwil");
            stmt.executeUpdate("UPDATE t_log SET s1 = (SELECT kanwil FROM t_kanwil WHERE t_kanwil.kode_cabang = t_log.name2) WHERE EXISTS (SELECT 1 FROM t_kanwil WHERE t_kanwil.kode_cabang = t_log.name2)");
            System.out.println("Done");
        } catch (SQLException ex) {
            System.out.println("Set Kode Kanwil Error!!");
            Logger.getLogger(LogModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setNmCabang(Statement stmt){
        try {
            System.out.println("Set Nama Cabang");
            String Query = "UPDATE t_log SET ss3 = (SELECT cabang FROM t_kanwil WHERE t_kanwil.kode_cabang = t_log.name2) WHERE EXISTS (SELECT 1 FROM t_kanwil WHERE t_kanwil.kode_cabang = t_log.name2)";
            System.out.println(Query);
            stmt.executeUpdate(Query);
        } catch (SQLException ex) {
            Logger.getLogger(LogModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setKurirNCSByNoRek(Statement stmt) throws SQLException{
        stmt.executeUpdate("UPDATE t_log JOIN t_ncs ON t_log.id_customer = t_ncs.no_rekening SET t_log.courier_name = 'NCS'");
    }
    
    public void setKurirByKanwil(Statement stmt){
        try {
            //        stmt.executeUpdate("UPDATE t_log SET courier_name = 'POS' WHERE s1 IN (3,6)");
//        stmt.executeUpdate("UPDATE t_log SET courier_name = 'NCS' WHERE s1 NOT IN (3,6)");
        // Cara 2
        String quiery = "UPDATE t_log SET courier_name = CASE " +
                "WHEN s1 IN (3,6) THEN 'POS'"+
                "ELSE 'NCS' END";
        int totalRow = stmt.executeUpdate(quiery);
        System.out.println("Update rows : " + totalRow);
        } catch (SQLException ex) {
            Logger.getLogger(LogModel.class.getName()).log(Level.SEVERE, "Error update courier", ex);
        }
    }

    public ArrayList<Integer> getIdLog() {
        return idLog;
    }

    public void setIdLog(ArrayList<Integer> idLog) {
        this.idLog = idLog;
    }

    public ArrayList<String> getBarcode() {
        return barcode;
    }

    public void setBarcode(ArrayList<String> barcode) {
        this.barcode = barcode;
    }

    public ArrayList<String> getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(ArrayList<String> idCustomer) {
        this.idCustomer = idCustomer;
    }

    public ArrayList<String> getName1() {
        return name1;
    }

    public void setName1(ArrayList<String> name1) {
        this.name1 = name1;
    }

    public ArrayList<String> getName2() {
        return name2;
    }

    public void setName2(ArrayList<String> name2) {
        this.name2 = name2;
    }

    public ArrayList<String> getName3() {
        return name3;
    }

    public void setName3(ArrayList<String> name3) {
        this.name3 = name3;
    }

    public ArrayList<String> getAddress1() {
        return address1;
    }

    public void setAddress1(ArrayList<String> address1) {
        this.address1 = address1;
    }

    public ArrayList<String> getAddress2() {
        return address2;
    }

    public void setAddress2(ArrayList<String> address2) {
        this.address2 = address2;
    }

    public ArrayList<String> getAddress3() {
        return address3;
    }

    public void setAddress3(ArrayList<String> address3) {
        this.address3 = address3;
    }

    public ArrayList<String> getAddress4() {
        return address4;
    }

    public void setAddress4(ArrayList<String> address4) {
        this.address4 = address4;
    }

    public ArrayList<String> getAddress5() {
        return address5;
    }

    public void setAddress5(ArrayList<String> address5) {
        this.address5 = address5;
    }

    public ArrayList<String> getAddress6() {
        return address6;
    }

    public void setAddress6(ArrayList<String> address6) {
        this.address6 = address6;
    }

    public ArrayList<Integer> getB1() {
        return b1;
    }

    public void setB1(ArrayList<Integer> b1) {
        this.b1 = b1;
    }

    public ArrayList<Integer> getB2() {
        return b2;
    }

    public void setB2(ArrayList<Integer> b2) {
        this.b2 = b2;
    }

    public ArrayList<Integer> getB3() {
        return b3;
    }

    public void setB3(ArrayList<Integer> b3) {
        this.b3 = b3;
    }

    public ArrayList<String> getB4() {
        return b4;
    }

    public void setB4(ArrayList<String> b4) {
        this.b4 = b4;
    }

    public ArrayList<Integer> getB5() {
        return b5;
    }

    public void setB5(ArrayList<Integer> b5) {
        this.b5 = b5;
    }

    public ArrayList<Integer> getB6() {
        return b6;
    }

    public void setB6(ArrayList<Integer> b6) {
        this.b6 = b6;
    }

    public ArrayList<Integer> getS1() {
        return s1;
    }

    public void setS1(ArrayList<Integer> s1) {
        this.s1 = s1;
    }

    public ArrayList<Integer> getS2() {
        return s2;
    }

    public void setS2(ArrayList<Integer> s2) {
        this.s2 = s2;
    }

    public ArrayList<Integer> getS3() {
        return s3;
    }

    public void setS3(ArrayList<Integer> s3) {
        this.s3 = s3;
    }

    public ArrayList<Integer> getS4() {
        return s4;
    }

    public void setS4(ArrayList<Integer> s4) {
        this.s4 = s4;
    }

    public ArrayList<Integer> getS5() {
        return s5;
    }

    public void setS5(ArrayList<Integer> s5) {
        this.s5 = s5;
    }

    public ArrayList<Integer> getS6() {
        return s6;
    }

    public void setS6(ArrayList<Integer> s6) {
        this.s6 = s6;
    }

    
    public ArrayList<String> getProductName() {
        return productName;
    }

    public void setProductName(ArrayList<String> productName) {
        this.productName = productName;
    }

    public ArrayList<String> getCourierName() {
        return courierName;
    }

    public void setCourierName(ArrayList<String> courierName) {
        this.courierName = courierName;
    }

    public ArrayList<Integer> getSeqPage() {
        return seqPage;
    }

    public void setSeqPage(ArrayList<Integer> seqPage) {
        this.seqPage = seqPage;
    }

    public ArrayList<Integer> getSeqCustomer() {
        return seqCustomer;
    }

    public void setSeqCustomer(ArrayList<Integer> seqCustomer) {
        this.seqCustomer = seqCustomer;
    }

    public ArrayList<Integer> getSeqEnvelope() {
        return seqEnvelope;
    }

    public void setSeqEnvelope(ArrayList<Integer> seqEnvelope) {
        this.seqEnvelope = seqEnvelope;
    }

    public ArrayList<String> getSs1() {
        return ss1;
    }

    public ArrayList<String> getSs2() {
        return ss2;
    }

    public ArrayList<String> getSs3() {
        return ss3;
    }

    public ArrayList<String> getSs4() {
        return ss4;
    }

    public ArrayList<String> getSs5() {
        return ss5;
    }

    public ArrayList<String> getSs6() {
        return ss6;
    }
    
    
    
    
    
}
