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
import maybank_rk_pdf.model.LogModel;

/**
 *
 * @author Ratino
 */
public class LogController {
    
    protected Connection koneksi, koneksi1;
    protected Statement stmt, stmt1;
    
    LogModel log = new LogModel();
    
    public void setConnection() throws SQLException{
        koneksi = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/db_myb_rk?autoReconnect=true&failOverReadonly=true&maxReconnects=1000", "root", "");
        stmt =  (Statement) koneksi.createStatement();
    }
    
    public void uploadDataKanwil(String path) throws SQLException{
        setConnection();
        log.createdbKanwil(path, stmt);
    }

    public Connection getKoneksi1() {
        return koneksi1;
    }

    public Statement getStmt() {
        return stmt;
    }
    
    
    
}
