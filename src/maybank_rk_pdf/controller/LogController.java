/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.controller;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import maybank_rk_pdf.model.LogModel;

/**
 *
 * @author Ratino
 */
public class LogController {
    
    protected Connection koneksi, koneksi1;
    protected Statement stmt, stmt1;
    
    LogModel log = new LogModel();
    
    public void setConnection(){
        try {
            koneksi = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/db_myb_rk?autoReconnect=true&failOverReadonly=true&maxReconnects=1000", "root", "");
            stmt =  (Statement) koneksi.createStatement();
        } catch (SQLException ex) {
            System.out.println("Error setConnection : Cek XAMPP");
            Logger.getLogger(LogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void uploadDataKanwil(String path) throws SQLException{
        setConnection();
        log.createdbKanwil(path, stmt);
    }
    
    public void uploadDateCourier(String path, Statement stmt) throws SQLException{
        log.loadCouByCourierNCS(path, stmt);
        log.loadCouByCourierPOS(path, stmt);
        log.loadDataHold(path, stmt);
    }
    
    public void uploadDataResourceKurir(String path, Statement stmt) throws SQLException{
        log.loadCouByCourierNCS(path + "NCS.txt", stmt);
        log.loadDataZipCode(path + "ZIPCODE.txt", stmt);
        log.loadDataHold(path + "HOLD.txt", stmt);
    }
       

    public Connection getKoneksi1() {
        return koneksi1;
    }

    public Statement getStmt() {
        return stmt;
    }
    
    
    
}
