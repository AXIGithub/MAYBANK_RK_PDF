/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ratino
 */
public class P01Model {
    
    
    public void createP01Xls(String path, Statement stmt){
        Map<String, BufferedWriter> fileWriters = new HashMap<>();
        try {
            
            ResultSet hasilQuery = null;
            String query = "SELECT courier_name AS NFILE, name2 AS KCAB, name2 AS CABANG, COUNT(id_customer) AS JUMLAH_LEMBAR, SUM(s6) AS JUMLAH_AMPLOP, s1 AS KANWIL " +
                    "FROM t_log " +
                    "GROUP BY courier_name, name2, s1 " +
                    "ORDER BY s1, courier_name, name2";
            System.out.println("Query p01 : " + query);
            hasilQuery = stmt.executeQuery(query);
            while(hasilQuery.next()){
                String courierName = hasilQuery.getString("NFILE");
                String kcab = hasilQuery.getString("KCAB");
                String cabang = hasilQuery.getString("CABANG");
                int jumlahLembar = hasilQuery.getInt("JUMLAH_LEMBAR");
                int jumlahAmplop = hasilQuery.getInt("JUMLAH_AMPLOP");
                int kanwil = hasilQuery.getInt("KANWIL");
                
                String fileName = "P01_RK_KANWIL" + kanwil + ".XLS";
                BufferedWriter writer = fileWriters.get(fileName);
                
                if (writer == null) {
                    writer = new BufferedWriter(new FileWriter(path + fileName));
                    fileWriters.put(fileName, writer);

                    // Tulis header file
                    writer.write("NFILE\tKCAB\tCABANG\tJUMLAH\tLEMBAR\tAMPLOP");
                    writer.newLine();
                }
                
                writer.write(courierName + "\t" +
                             kcab + "\t" +
                             cabang + "\t" +
                             jumlahLembar + "\t" +
                             jumlahLembar + "\t" +
                             jumlahAmplop);
                writer.newLine();
                
            }
            System.out.println("File P01 XLS berhasil dibuat!");
            
        } catch (SQLException | IOException ex) {
            Logger.getLogger(P01Model.class.getName()).log(Level.SEVERE, null, ex);        
        } finally {
            for(BufferedWriter writer : fileWriters.values()){
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(P01Model.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
   
        
    }
}
