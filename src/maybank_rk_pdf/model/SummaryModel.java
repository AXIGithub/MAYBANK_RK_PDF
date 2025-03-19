/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import maybank_rk_pdf.controller.TextModification;
import java.util.logging.Level;
import java.util.logging.Logger;
import maybank_rk_pdf.controller.TextModification;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ratino
 */
public class SummaryModel {


    public void createSumByKanwil(String path, String cycle, Statement stmt){
        try {
            ResultSet hasilQuery = null;
            hasilQuery = stmt.executeQuery("SELECT s1 AS KANWIL, COUNT(DISTINCT id_customer) AS NASABAH, COUNT(seq_page) AS HALAMAN, SUM(s6) AMPLOP FROM t_log GROUP BY s1 ORDER BY s1");
           
            Workbook workbook = new XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Summary Data");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"KANWIL", "NASABAH", "HALAMAN", "AMPLOP"};
            for(int i = 0; i < columns.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }
            
            //Write
            int rowNum = 1;
            while(hasilQuery.next()){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(hasilQuery.getString("KANWIL"));
                row.createCell(1).setCellValue(hasilQuery.getString("NASABAH"));
                row.createCell(2).setCellValue(hasilQuery.getString("HALAMAN"));
                row.createCell(3).setCellValue(hasilQuery.getString("AMPLOP"));
            }
            
            for(int i = 0; i < columns.length; i++){
                sheet.autoSizeColumn(i);
            }
            
            try(FileOutputStream fos = new FileOutputStream(path + "Sum_by_Kanwil.xls")){
                workbook.write(fos);
            } catch (IOException ex) {
                Logger.getLogger(SummaryModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Create Excel berhasil!!");          
            
        } catch (SQLException ex) {
            Logger.getLogger(SummaryModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void createLogByKanwil(String path, String kategori, Statement stmt) {
    try {
        TextModification txt = new TextModification();

        ResultSet hasilQuery = null;

        // Query untuk mengambil data
        String query = "SELECT barcode, courier_name, s1 AS KANWIL, id_customer, name1, address1, address2, address3, address4, address5, address6, s6 " +
                       "FROM t_log " +
                       "WHERE courier_name IN ('BLOK', 'HOLD', 'NCS', 'NCSB', 'NCSG', 'POS', 'POSB', 'SAP', 'SAPB', 'SAPG') AND s6 = 1 " +
                       "ORDER BY courier_name, s1, id_customer";
        hasilQuery = stmt.executeQuery(query);

        // Map untuk menyimpan BufferedWriter untuk setiap file
        Map<String, BufferedWriter> fileWriters = new HashMap<>();

        while (hasilQuery.next()) {
            String courierName = hasilQuery.getString("courier_name");
            String kanwil = hasilQuery.getString("KANWIL");

            // Nama file
            String fileName = kategori.toUpperCase() + "_MASTER_" + courierName + "_RK_KANWIL_" + kanwil + ".txt";

            // Dapatkan BufferedWriter untuk file ini
            BufferedWriter bw1 = fileWriters.get(fileName);

            // Jika BufferedWriter belum ada, buat baru
            if (bw1 == null) {
                bw1 = new BufferedWriter(new FileWriter(path + fileName));
                fileWriters.put(fileName, bw1);                
            }
 
            // Tulis data ke file
            bw1.write(txt.padRStr(hasilQuery.getString("barcode"), 14, ' ') +
                      txt.padRStr(hasilQuery.getString("name1"), 40, ' ') +
                      txt.padRStr(hasilQuery.getString("address1"), 40, ' ') +
                      txt.padRStr(hasilQuery.getString("address2"), 40, ' ') +
                      txt.padRStr(hasilQuery.getString("address6"), 9, ' ') + // Kode Pos
                      txt.padRStr(hasilQuery.getString("s6"), 1, ' ') +       // Jumlah Amplop
                      txt.padRStr(hasilQuery.getString("courier_name"), 8, ' ')); // Kurir
            bw1.newLine();
        }

        // Tutup semua BufferedWriter
        for (BufferedWriter bw : fileWriters.values()) {
            bw.flush();
            bw.close();
        }

        System.out.println("File berhasil dibuat!");

    } catch (SQLException ex) {
        System.out.println("Error SQL in createLogByKanwil");
        ex.printStackTrace();
    } catch (IOException ex) {
        System.out.println("Error File Writer in createLogByKanwil");
        ex.printStackTrace();
    }
}
    
    
    public void createSummary(String path, String cycle, String product, Statement stmt){
        try {
            TextModification text = new TextModification();
            ResultSet hasilQuery = null;
            String query = "SELECT 'Jumlah Lembar', COUNT(*) FROM t_log UNION ALL "+
                    "SELECT 'Jumlah Amplop Kecil', COUNT(*) FROM t_log WHERE ss2 LIKE 'A%' AND S6 = 1 UNION ALL "+
                    "SELECT 'Jumlah Amplop Besar', COUNT(*) FROM t_log WHERE ss2 LIKE 'B%' AND S6 = 1 UNION ALL "+
                    "SELECT 'Kurir BLOK', COUNT(*) FROM t_log WHERE courier_name = 'BLOK' UNION ALL "+
                    "SELECT 'Kurir HOLD', COUNT(*) FROM t_log WHERE courier_name = 'HOLD' UNION ALL "+
                    "SELECT 'Kurir NCS', COUNT(*) FROM t_log WHERE courier_name = 'NCS' UNION ALL "+
                    "SELECT 'Kurir NCSB', COUNT(*) FROM t_log WHERE courier_name = 'NCSB' UNION ALL "+
                    "SELECT 'Kurir NCSG', COUNT(*) FROM t_log WHERE courier_name = 'NCSG' UNION ALL "+
                    "SELECT 'Kurir POS', COUNT(*) FROM t_log WHERE courier_name = 'POS' UNION ALL "+
                    "SELECT 'Kurir POSB', COUNT(*) FROM t_log WHERE courier_name = 'POSB' UNION ALL "+
                    "SELECT 'Kurir SAP', COUNT(*) FROM t_log WHERE courier_name = 'SAP' UNION ALL "+
                    "SELECT 'Kurir SAPB', COUNT(*) FROM t_log WHERE courier_name = 'SAPB' UNION ALL "+
                    "SELECT 'Kurir SAPG', COUNT(*) FROM t_log WHERE courier_name = 'SAPG'";
            System.out.println(query);
            hasilQuery = stmt.executeQuery(query);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "Summary_"+cycle+" .txt"));
            writer.write("Summary may_rk_" + cycle + "\r\n");
            writer.write("Cycle : "+cycle + "\r\n");
            writer.write("Produk : " + product + "\r\n");
            writer.newLine();  
            while(hasilQuery.next()){
                String category = hasilQuery.getString(1);
                int jml = hasilQuery.getInt(2);
                if(category.equals("Kurir BLOK")){
                    writer.newLine();
                    writer.write("Alokasi Kurir");
                    writer.newLine();
                }
                writer.write(text.padRStr("- " + category , 28, ' ') +  ": " + jml);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (SQLException ex) {
            Logger.getLogger(SummaryModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SummaryModel.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    
    private static CellStyle getHeaderStyle(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;        
    }
}
