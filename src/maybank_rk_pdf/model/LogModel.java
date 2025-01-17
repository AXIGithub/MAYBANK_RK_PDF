/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import java.sql.SQLException;
import java.sql.Statement;

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
    
    public void loadDataHold(String pathCourier, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXISTS t_hold");
        stmt.executeUpdate("CREATE TABLE t_hold"+
                           ")no INT NOT NULL, no_rekening VARCHAR(100) NOT NULL, Name VARCHAR(100) NOT NULL, "
                           + "addr1 VARCHAR(100), addr2 VARCHAR(100), addr3 VARCHAR(100), addr4 VARCHAR(100), addr5 VARCHAR(100), post_code VARCHAR(5)) ENGINE = MYISAM");
        stmt.executeUpdate("LOAD DATA INFILE '" + pathCourier + "' INTO TABLE t_hold");
        
    }
    
    public void loadCouByCourierNCS(String path, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXIST t_ncs");
        stmt.executeUpdate("CREATE TABLE t_ncs "
                           +"(no VARCHAR(5) NOT NULL, no_rekening VARCHAR(100) NOT NULL, nama VARCHAR(100) NOT NULL) ENGINE = MYISAM");
        stmt.executeUpdate("LOAD DATA INFILE '" + path + "' INTO TABLE t_ncs");
    }
    
    public void loadCouByCourierPOS(String path, Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE IF EXIST t_pos");
        stmt.executeUpdate("CREATE TABLE t_pos "
                           +"(no VARCHAR(5) NOT NULL, no_rekening VARCHAR(100) NOT NULL, nama VARCHAR(100) NOT NULL) ENGINE = MYISAM");
        stmt.executeUpdate("LOAD DATA INFILE '" + path + "' INTO TABLE t_pos");
    }
    
    
    
    public void loadLogData(String path, Statement stmt){
        
    }
    
    
}
