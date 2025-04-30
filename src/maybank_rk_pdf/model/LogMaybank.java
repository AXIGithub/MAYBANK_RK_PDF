/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import maybank_rk_pdf.controller.PathDirectory;
import maybank_rk_pdf.controller.TextModification;

/**
 *
 * @author Ratino
 */
public class LogMaybank {
    TextModification text = new TextModification();
    PathDirectory pd = new PathDirectory();
    
    public void createLogAllMaybank(String path, String kategori, Statement stmt){
        try {
            LocalDate date = LocalDate.now();
            LocalDate nextDay = date.plusDays(1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateNow = dateTimeFormatter.format(date);
            String nextDayFormat = dateTimeFormatter.format(nextDay);
                        
            
            String fileOut = pd.configurePath(path + kategori + "_MASTER_MAYBANK_RK_ALL" + ".txt");                        
            
            ResultSet hasilQuery = null;
            // writeln(logFile            ,padLStr(copy(kanwil,1,2),2,'0')+padRStr(copy(cabang,1,40),40,' ')+ copy(policy,2,3) +padRStr(barcode,28,' ')+padRStr(CIF,16,' ')+padRStr(policy,48,' ')+padRStr(copy(nama,1,30),30,' ')+padRStr(copy(addr1,1,30),30,' ')+padRStr(copy(addr2,1,30),30,' ')+padRStr(copy(addr3,1,30),30,' ')+padRStr(copy(addr4,1,30),30,' ')+padRStr(uppercase(kota),28,' ')+padRStr(Zipcode,234,' ')+padRStr(tanggalCPDP,10,' ')+padRStr(copy(GRUPkurir,1,5),4,' ')+padLStr(inttostr(jumpage),5,' ')+padLStr(inttostr(jumAMPall),5,' ')+padRStr(kurir,5,' ')+padRStr(pickupDate,41,' ')+'.');
            hasilQuery = stmt.executeQuery("SELECT s1 AS kanwil, ss3 AS kd_cabang, id_customer, barcode, name3 AS cif, name1, address1, address2, address3, address4, address5, address6, courier_name, ss1 FROM t_log GROUP BY id_customer ORDER BY courier_name");
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
            
            
            while(hasilQuery.next()){
                bw.write(
                        text.padRStr(text.norm2Digit(hasilQuery.getInt("kanwil")), 2, ' ') + // kanwil //padLStr(copy(kanwil,1,2),2,'0')
                        text.padRStr(hasilQuery.getString("kd_cabang"), 40, ' ') + //nama CAbang  padRStr(copy(cabang,1,40),40,' ')
                        hasilQuery.getString("id_customer").substring(2, 5) + //  No rek //copy(policy,2,3)
                        text.padRStr(hasilQuery.getString("barcode"), 28, ' ') + //  padRStr(barcode,28,' ')
                        text.padRStr(hasilQuery.getString("cif"), 16, ' ') + //padRStr(CIF,16,' ')                       
                        text.padRStr(hasilQuery.getString("id_customer"), 48, ' ') + // padRStr(policy,48,' ')t+
                        text.padRStr(hasilQuery.getString("name1"), 30, ' ') + // padRStr(copy(nama,1,30),30,' ')
                        text.padRStr(hasilQuery.getString("address1"), 30, ' ') +  // padRStr(copy(addr1,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address2"), 30, ' ') +  // padRStr(copy(addr2,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address3"), 30, ' ') + // padRStr(copy(addr3,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address4"), 30, ' ') +  // padRStr(copy(addr4,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address5"), 28, ' ') +  // padRStr(uppercase(kota),28,' ')+padRStr(Zipcode,234,' ')+padRStr(tanggalCPDP,10,' ')+padRStr(copy(GRUPkurir,1,5),4,' ')+padLStr(inttostr(jumpage),5,' ')+padLStr(inttostr(jumAMPall),5,' ')+padRStr(kurir,5,' ')+padRStr(pickupDate,41,' ')+'.');
                        text.padRStr(hasilQuery.getString("address6"), 234, ' ') +  // padRStr(Zipcode,234,' ')+
                        text.padRStr(dateNow,10, ' ') +                             // padRStr(tanggalCPDP,10,' ')+
                        text.padRStr("MAIL",4,' ') +     // padRStr(copy(GRUPkurir,1,5),4,' ')+
                        text.padLStr(hasilQuery.getString("ss1"), 5, ' ') +  // padLStr(inttostr(jumpage),5,' ')+
                        text.padLStr("0",5,' ') +   // padLStr(inttostr(jumAMPall),5,' ')+
                        text.padRStr(hasilQuery.getString("courier_name"), 5, ' ') +  // padRStr(kurir,5,' ')+      
                        text.padRStr(nextDayFormat,41,' ') +        // padRStr(pickupDate,41,' ')+'.');
                                ".\r\n"
                );
            }
            
            bw.flush();
            bw.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createLogAllADD40(String path, String kategory, Statement stmt){
        try {
            //writeln(logFileMaybankAdd40,padLStr(copy(kanwil,1,2),2,'0')+padRStr(copy(cabang,1,40),40,' ')+ copy(policy,2,3) +padRStr(barcode,28,' ')+padRStr(CIF,16,' ')+padRStr(policy,48,' ')+padRStr(copy(nama,1,30),30,' ')+padRStr(copy(addr1,1,40),40,' ')+padRStr(copy(addr2,1,40),40,' ')+padRStr(copy(addr3,1,40),40,' ')+padRStr(copy(addr4,1,40),40,' ')+padRStr(uppercase(kota),28,' ')+padRStr(Zipcode,234,' ')+padRStr(tanggalCPDP,10,' ')+padRStr(copy(GRUPkurir,1,5),4,' ')+padLStr(inttostr(jumpage),5,' ')+padLStr(inttostr(jumAMPall),5,' ')+padRStr(kurir,5,' ')+padRStr(pickupDate,41,' ')+'.');//+copy(kurir,1,3)+copy(kurir,1,3)+copy(kurir,1,3)+padRStr(kurir,12,' '));
            LocalDate date = LocalDate.now();
            LocalDate nextDay = date.plusDays(1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateNow = dateTimeFormatter.format(date);
            String nextDayFormat = dateTimeFormatter.format(nextDay);
            
            ResultSet hasilQuery = null;
            Map<String, BufferedWriter> fileWriters = new HashMap<>();
            
            String query = "SELECT barcode, id_customer, ss3 AS kd_cabang,  name3 AS cif, courier_name, name1, address1, address2, address3, address4, address5, address6, s1 AS KANWIL, courier_name, ss1 " +
                    "FROM t_log " +
                    "GROUP BY id_customer ORDER BY courier_name";
            
            hasilQuery = stmt.executeQuery(query);
            while(hasilQuery.next()){
                String kanwil = hasilQuery.getString("KANWIL");
                String fileName = kategory +"_MASTER_MAYBANK_RK_KANWIL" + kanwil + "_ADD40.txt";
                BufferedWriter writer = fileWriters.get(fileName);
                
                if (writer == null) {
                    writer = new BufferedWriter(new FileWriter(path + fileName));
                    fileWriters.put(fileName, writer);
                }
                
                String data = 
                        text.padRStr(text.norm2Digit(hasilQuery.getInt("kanwil")), 2, ' ') + // kanwil //padLStr(copy(kanwil,1,2),2,'0')
                        text.padRStr(hasilQuery.getString("kd_cabang"), 40, ' ') + //nama CAbang  padRStr(copy(cabang,1,40),40,' ')
                        hasilQuery.getString("id_customer").substring(2, 5) + //  No rek //copy(policy,2,3)
                        text.padRStr(hasilQuery.getString("barcode"), 28, ' ') + //  padRStr(barcode,28,' ')
                        text.padRStr(hasilQuery.getString("cif"), 16, ' ') + //padRStr(CIF,16,' ')                       
                        text.padRStr(hasilQuery.getString("id_customer"), 48, ' ') + // padRStr(policy,48,' ')t+
                        text.padRStr(hasilQuery.getString("name1"), 30, ' ') + // padRStr(copy(nama,1,30),30,' ')
                        text.padRStr(hasilQuery.getString("address1"), 30, ' ') +  // padRStr(copy(addr1,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address2"), 30, ' ') +  // padRStr(copy(addr2,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address3"), 30, ' ') + // padRStr(copy(addr3,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address4"), 30, ' ') +  // padRStr(copy(addr4,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address5"), 28, ' ') +  // padRStr(uppercase(kota),28,' ')+padRStr(Zipcode,234,' ')+padRStr(tanggalCPDP,10,' ')+padRStr(copy(GRUPkurir,1,5),4,' ')+padLStr(inttostr(jumpage),5,' ')+padLStr(inttostr(jumAMPall),5,' ')+padRStr(kurir,5,' ')+padRStr(pickupDate,41,' ')+'.');
                        text.padRStr(hasilQuery.getString("address6"), 234, ' ') +  // padRStr(Zipcode,234,' ')+
                        text.padRStr(dateNow,10, ' ') +                             // padRStr(tanggalCPDP,10,' ')+
                        text.padRStr("MAIL",4,' ') +     // padRStr(copy(GRUPkurir,1,5),4,' ')+
                        text.padLStr(hasilQuery.getString("ss1"), 5, ' ') +  // padLStr(inttostr(jumpage),5,' ')+
                        text.padLStr("0",5,' ') +   // padLStr(inttostr(jumAMPall),5,' ')+
                        text.padRStr(hasilQuery.getString("courier_name"), 5, ' ') +  // padRStr(kurir,5,' ')+      
                        text.padRStr(nextDayFormat,41,' ') +        // padRStr(pickupDate,41,' ')+'.');
                        ".\r\n";
                
                writer.write(data);
                        
            }
            for(BufferedWriter writer : fileWriters.values()){
                if(writer != null){
                    writer.flush();
                    writer.close();
                }
            }
            System.out.println("File Log Kanwil berhasil dibuat");
        } catch (SQLException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void createLogKanwil(String path, String kategory, Statement stmt){
        try {
            //writeln(logFileMaybank     ,padLStr(copy(kanwil,1,2),2,'0')+padRStr(copy(cabang,1,40),40,' ')+ copy(policy,2,3) +padRStr(barcode,28,' ')+padRStr(CIF,16,' ')+padRStr(policy,48,' ')+padRStr(copy(nama,1,30),30,' ')+padRStr(copy(addr1,1,30),30,' ')+padRStr(copy(addr2,1,30),30,' ')+padRStr(copy(addr3,1,30),30,' ')+padRStr(copy(addr4,1,30),30,' ')+padRStr(uppercase(kota),28,' ')+padRStr(Zipcode,234,' ')+padRStr(tanggalCPDP,10,' ')+padRStr(copy(GRUPkurir,1,5),4,' ')+padLStr(inttostr(jumpage),5,' ')+padLStr(inttostr(jumAMPall),5,' ')+padRStr(kurir,5,' ')+padRStr(pickupDate,41,' ')+'.');//+copy(kurir,1,3)+copy(kurir,1,3)+copy(kurir,1,3)+padRStr(kurir,12,' '));
            LocalDate date = LocalDate.now();
            LocalDate nextDay = date.plusDays(1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateNow = dateTimeFormatter.format(date);
            String nextDayFormat = dateTimeFormatter.format(nextDay);
            
            ResultSet hasilQuery = null;
            Map<String, BufferedWriter> fileWriters = new HashMap<>();
            
            String query = "SELECT barcode, id_customer, ss3 AS cabang,  name3 AS cif, courier_name, name1, address1, address2, address3, address4, address5, address6, s1 AS KANWIL, courier_name, ss1 " +
                    "FROM t_log " +
                    "GROUP BY id_customer ORDER BY courier_name";
            
            hasilQuery = stmt.executeQuery(query);
            while(hasilQuery.next()){
                String kanwil = hasilQuery.getString("KANWIL");
                String fileName = kategory +"_MASTER_MAYBANK_RK_KANWIL" + kanwil + ".txt";
                BufferedWriter writer = fileWriters.get(fileName);
                
                if (writer == null) {
                    writer = new BufferedWriter(new FileWriter(path + fileName));
                    fileWriters.put(fileName, writer);
                }
                
                String data = 
                        text.padRStr(text.norm2Digit(hasilQuery.getInt("kanwil")), 2, ' ') + // kanwil //padLStr(copy(kanwil,1,2),2,'0')
                        text.padRStr(hasilQuery.getString("cabang"), 40, ' ') + //nama CAbang  padRStr(copy(cabang,1,40),40,' ')
                        hasilQuery.getString("id_customer").substring(2, 5) + //  No rek //copy(policy,2,3)
                        text.padRStr(hasilQuery.getString("barcode"), 28, ' ') + //  padRStr(barcode,28,' ')
                        text.padRStr(hasilQuery.getString("cif"), 16, ' ') + //padRStr(CIF,16,' ')                       
                        text.padRStr(hasilQuery.getString("id_customer"), 48, ' ') + // padRStr(policy,48,' ')t+
                        text.padRStr(hasilQuery.getString("name1"), 30, ' ') + // padRStr(copy(nama,1,30),30,' ')
                        text.padRStr(hasilQuery.getString("address1"), 30, ' ') +  // padRStr(copy(addr1,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address2"), 30, ' ') +  // padRStr(copy(addr2,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address3"), 30, ' ') + // padRStr(copy(addr3,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address4"), 30, ' ') +  // padRStr(copy(addr4,1,30),30,' ')+
                        text.padRStr(hasilQuery.getString("address5"), 28, ' ') +  // padRStr(uppercase(kota),28,' ')+padRStr(Zipcode,234,' ')+padRStr(tanggalCPDP,10,' ')+padRStr(copy(GRUPkurir,1,5),4,' ')+padLStr(inttostr(jumpage),5,' ')+padLStr(inttostr(jumAMPall),5,' ')+padRStr(kurir,5,' ')+padRStr(pickupDate,41,' ')+'.');
                        text.padRStr(hasilQuery.getString("address6"), 234, ' ') +  // padRStr(Zipcode,234,' ')+
                        text.padRStr(dateNow,10, ' ') +                             // padRStr(tanggalCPDP,10,' ')+
                        text.padRStr("MAIL",4,' ') +     // padRStr(copy(GRUPkurir,1,5),4,' ')+
                        text.padLStr(hasilQuery.getString("ss1"), 5, ' ') +  // padLStr(inttostr(jumpage),5,' ')+
                        text.padLStr("0",5,' ') +   // padLStr(inttostr(jumAMPall),5,' ')+
                        text.padRStr(hasilQuery.getString("courier_name"), 5, ' ') +  // padRStr(kurir,5,' ')+      
                        text.padRStr(nextDayFormat,41,' ') +        // padRStr(pickupDate,41,' ')+'.');
                                ".\r\n";
                
                writer.write(data);
                        
            }
            for(BufferedWriter writer : fileWriters.values()){
                if(writer != null){
                    writer.flush();
                    writer.close();
                }
            }
            System.out.println("File Log Kanwil berhasil dibuat");
        } catch (SQLException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void createLogScan(String path, Statement stmt){
        try {
            String FileName = "LOG SCAN MAYBANK RK.LOG";
            ResultSet hasilQuery = null;
            int seqNum = 0;
            String query = "SELECT barcode, name1, product_name, courier_name " +
                    "FROM t_log " +
                    "WHERE s6 = '1' ORDER BY ss2, courier_name, ss1, seq_envelope ASC";
            System.out.println("Query log scan : " + query);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(path  + FileName));
            
            hasilQuery = stmt.executeQuery(query);
            while(hasilQuery.next()){
                seqNum++;
                String data = 
                        text.norm6Digit(seqNum) + "\t" +
                        hasilQuery.getString("barcode") + "\t" +
                        hasilQuery.getString("name1") + "\t" +
                        hasilQuery.getString("product_name") + "\t" +
                        hasilQuery.getString("courier_name") + "\t" + "\r\n";
                writer.write(data);
            }
            writer.flush();
            writer.close();
        } catch (SQLException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogMaybank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
