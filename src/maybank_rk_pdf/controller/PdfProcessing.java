/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
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
    Boolean isFirstPage = false;
    String kurir = "", jnsAmplop = "";
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
            
            String barcode = logModel.getIdCustomer().get(i);
            kurir = logModel.getCourierName().get(i);
            jnsAmplop = logModel.getSs2().get(i);

            PdfReader reader = new PdfReader(inputDir + pdfFileName);
            int numberOfPages = reader.getNumberOfPages();
            
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, arrayOutputStream);
             
            for(int j=1; j <= numberOfPages; j++){
                barcode = barcode + txt.norm6Digit(j);
                isFirstPage = j == 1 ? true : false;
                PdfContentByte canvas = stamper.getOverContent(j);
                addTextToPage(isFirstPage, canvas, barcode + "/A:" + txt.norm6Digit(j) + "/" +  kurir + "|" + jnsAmplop, barcode , 50 , 655);
            }
            
            stamper.close();
            reader.close();
            
            PdfReader pdfReader2 = new PdfReader(arrayOutputStream.toByteArray());
            for (int j = 1; j <= pdfReader2.getNumberOfPages(); j++) {
                copy.addPage(copy.getImportedPage(pdfReader2, j));
            }

            pdfReader2.close();
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
    
    private void addTextToPage(Boolean isFirstPage, PdfContentByte canvas, String text, String barcodeText, float x, float y) {
        try {
            
            if(isFirstPage){
                Barcode128 barcode = new Barcode128();
                barcode.setCode(barcodeText);
                barcode.setCodeType(Barcode128.CODE128);
                barcode.setFont(null);
                Image barcodeImage = barcode.createImageWithBarcode(canvas, null, null);
                barcodeImage.setAbsolutePosition(x, y);
                barcodeImage.scaleToFit(140,20);
                canvas.addImage(barcodeImage);
            } 
            
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED);
            canvas.beginText();
            canvas.setFontAndSize(bf, 6);
            canvas.setTextMatrix(x,y-10);
            canvas.showText(text);
            canvas.endText();
            
           
            
        } catch (DocumentException ex) {
            Logger.getLogger(PdfProcessing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfProcessing.class.getName()).log(Level.SEVERE, null, ex);
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
