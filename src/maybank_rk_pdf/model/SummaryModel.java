/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public void createSumByKanwil(String path, Statement stmt){
        try {
            ResultSet hasilQuery = null;
            hasilQuery = stmt.executeQuery("SELECT s1 AS KANWIL, COUNT(DISTINCT id_customer) AS NASABAH, COUNT(seq_page) AS HALAMAN, COUNT(s6) AMPLOP FROM t_log GROUP BY s1, ORDER BY s1");
            
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
    
    private static CellStyle getHeaderStyle(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;        
    }
}
