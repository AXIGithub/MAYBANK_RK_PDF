/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import maybank_rk_pdf.model.LogModel;

/**
 *
 * @author Ratino
 */
public class PdfProcessing {
    
    private String dirOutput, dirLogKurir, dirLogMaster, dirLogProd, dirReport, dirLogScan;
    public void runProcesing(String[] params, Directory dirPdf,String DirectoryInput, String parentInput, String type, Statement stmt) throws IOException, SQLException{
        dirLogProd = params[0];
        dirLogKurir = params[1];
        dirLogMaster = params[2];
        dirOutput = params[3];
        dirReport = params[4];
        dirLogScan = params[5];
        getLogOnePdf(dirPdf, DirectoryInput, parentInput, "Rekening Koran", type, stmt);
        uploadLogToDb(DirectoryInput + "Log All.txt", stmt);
//        barcodeInjector(DirectoryInput,stmt);
        barcodeInjector2(DirectoryInput,stmt);
                
    }
    
    public ArrayList<String> getLogOnePdf(Directory dirPdf, String DirectoryInput, String parentInput, String prodByCb, String typeProduct, Statement stmt) throws IOException{
        WriteLogPdf logPdf = new WriteLogPdf();
        logPdf.DeleteFileIfExsit(parentInput + "Log All.txt");
        ArrayList<String> logComponent = new ArrayList<String>(10);
        logComponent.add(""); //Jml Page
        logComponent.add(""); // Nama
        logComponent.add(""); //addr1
        logComponent.add(""); //addr2
        logComponent.add(""); //addr3
        logComponent.add(""); //addr4
        logComponent.add(""); //addr5
        logComponent.add("");logComponent.add("");logComponent.add("");logComponent.add("");logComponent.add("");
        for(int i = 0; i < dirPdf.getFileName().size(); i++){
            System.out.println("File Name " + dirPdf.getFileName().get(i));
            String pdf = dirPdf.getFileName().get(i);
            String namaFile = "" + DirectoryInput + "\\\\"+dirPdf.getFileName().get(i);
            logComponent = logPdf.textParserOnePdf(namaFile, pdf, DirectoryInput, prodByCb, "ddmmyyyy");
        }
//        System.exit(0);
        return null;
    }
    
    public void barcodeInjector(String inputDir, Statement stmt){
    Document document = new Document(PageSize.A4);
    PdfWriter writerNew = null;

    try {
        TextModification txt = new TextModification();
        LocalDateTime dt = LocalDateTime.now();
        String dateTime = dt.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

//        String dirOutput = dirOutput; // Ubah dengan direktori tujuan
        LogModel logModel = new LogModel();
        logModel.selectTableForCustomer(stmt);
        System.out.println("Total Customer : " + logModel.getIdCustomer().size());

        File outputFile = new File(Paths.get(dirOutput, dateTime + "-000001-" + logModel.getCourierName().get(0) + "-Print.pdf").toString());
        writerNew = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();

        PdfContentByte canvas = writerNew.getDirectContent();

        for (int i = 0; i < logModel.getIdCustomer().size(); i++) {
            String pdfFileName = logModel.getAddress6().get(i);
            System.out.println("Combine ke " + i + ". " + pdfFileName);
            PdfReader reader = new PdfReader(inputDir + pdfFileName);

            int numberOfPages = reader.getNumberOfPages();
            for (int j = 1; j <= numberOfPages; j++) {
                PdfImportedPage page = writerNew.getImportedPage(reader, j);
                canvas.addTemplate(page, 0, 0);
                document.newPage();
            }

            reader.close();
        }

        System.out.println("Berhasil dicombine");
        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
    
    
    public void barcodeInjector2(String inputDir, Statement stmt) {
    Document document = new Document();
    PdfCopy copy = null;

    try {
        TextModification txt = new TextModification();
        LocalDateTime dt = LocalDateTime.now();
        String dateTime = dt.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

       
        File outputFile = new File(Paths.get(dirOutput, dateTime + "-000001-Print.pdf").toString());
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        // Membuka dokumen dan menggunakan PdfCopy
        FileOutputStream fos = new FileOutputStream(outputFile);
        copy = new PdfCopy(document, fos);
        document.open();

        LogModel logModel = new LogModel();
        logModel.selectTableForCustomer(stmt);
        System.out.println("Total Customer: " + logModel.getIdCustomer().size());

        for (int i = 0; i < logModel.getIdCustomer().size(); i++) {
            String pdfFileName = logModel.getAddress6().get(i);
            System.out.println("Combine ke " + i + ": " + pdfFileName);

            PdfReader reader = new PdfReader(inputDir + pdfFileName);
            int numberOfPages = reader.getNumberOfPages();

            for (int j = 1; j <= numberOfPages; j++) {
                copy.addPage(copy.getImportedPage(reader, j));
            }

            reader.close();
        }

        System.out.println("Berhasil dicombine ke: " + outputFile.getAbsolutePath());

        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
    
    public void uploadLogToDb(String pathLog, Statement stmt) throws SQLException{
        LogModel logModel = new LogModel();
        logModel.createTable(stmt);
        logModel.loadLogData(pathLog, stmt);
        logModel.setKodeKanwil(stmt);
        logModel.setKurirByKanwil(stmt);
    }
    
  
    
    
    
}
