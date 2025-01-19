/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ratin
 */
public class Connection {
    
    protected com.mysql.jdbc.Connection koneksi, koneksi1;
    protected Statement stmt, stmt1;
    
    
    public void setConnection() throws SQLException{
        koneksi = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost/db_myb_rk?autoReconnect=true&failOverReadonly=true&maxReconnects=1000", "root", "");
        stmt =  (Statement) koneksi.createStatement();
    }

    public com.mysql.jdbc.Connection getKoneksi() {
        return koneksi;
    }

    public void setKoneksi(com.mysql.jdbc.Connection koneksi) {
        this.koneksi = koneksi;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
    
    
    
}
