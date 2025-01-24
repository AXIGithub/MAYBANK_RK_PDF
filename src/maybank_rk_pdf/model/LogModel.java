/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ratino
 */
public class LogModel {
    
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
                    "seq_envelope INT NOT NULL, ss1 CHAR(30) NOT NULL, ss2 CHAR(30) NOT NULL, ss3 CHAR(30) NOT NULL, ss4 CHAR(30) NOT NULL, ss5 CHAR(30) NOT NULL, ss6 CHAR(30) NOT NULL) ENGINE = MYISAM");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index1 (product_name,name2)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index2 (product_name)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index3 (courier_name,product_name)");
            stmt.executeUpdate("ALTER TABLE t_log ADD INDEX tb_log_index4 (seq_envelope, product_name, name2)");
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
    
    public void setKodeKanwil(Statement stmt) throws SQLException{
        // Kode Kanwil di set pada kolom s1
        stmt.executeUpdate("UPDATE t_log JOIN t_kanwil ON t_log.name2 = t_kanwil.kode_cabang SET t_log.s1 = t_kanwil.kanwil");
    }
    
    
}
