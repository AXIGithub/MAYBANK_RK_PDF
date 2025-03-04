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
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private int seqP, seqA, SeqC;
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
        
        LogModel logModel = new LogModel();
        logModel.createTable(stmt);
        logModel.loadLogData(dirLogProd + "LOG PRODUKSI.LOG", stmt);
                
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
    
        
    public void barcodeInjector2(String inputDir, Statement stmt) {
        Document document = new Document();
        PdfCopy copy = null;
        Boolean isFirstPage = false;
        String kurir = "", jnsAmplop = "", logString="", barcode="";
        seqP=0; seqA=0; SeqC=0;
        try {
            TextModification txt = new TextModification();
            LocalDateTime dt = LocalDateTime.now();
            String dateTime = dt.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

            //Create file 
            FileOutputStream outputStream = new FileOutputStream(dirLogProd + "LOG PRODUKSI.LOG");
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dataOutputStream));


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

                String idCustomer = logModel.getIdCustomer().get(i);
                kurir = logModel.getCourierName().get(i);
                jnsAmplop = logModel.getSs2().get(i);
                
                seqA++;
                SeqC++;

                PdfReader reader = new PdfReader(inputDir + pdfFileName);
                int numberOfPages = reader.getNumberOfPages();

                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                PdfStamper stamper = new PdfStamper(reader, arrayOutputStream);

                for(int j=1; j <= numberOfPages; j++){
                    seqP++;
                    barcode = idCustomer + txt.norm6Digit(j);
                    isFirstPage = j == 1 ? true : false;
                    PdfContentByte canvas = stamper.getOverContent(j);
                    addTextToPage(isFirstPage, canvas, barcode + "/A:" + txt.norm6Digit(j) + "/" +  kurir + "|" + jnsAmplop, barcode , 50 , 655);
                    
                    if(j==1){
                        logString = barcode + "\t" + idCustomer + "\t" + logModel.getName1().get(i) + "\t" + logModel.getName2().get(i) + "\t" + logModel.getName3().get(i) + "\t" +
                                logModel.getAddress1().get(i) + "\t" + logModel.getAddress2().get(i) + "\t" + logModel.getAddress3().get(i) + "\t" + logModel.getAddress4().get(i) + "\t" +
                                logModel.getAddress5().get(i) + "\t" + "-" + "\t" + logModel.getB1().get(i) + "\t" + logModel.getB2().get(i) + "\t" + logModel.getB3().get(i) + "\t" +
                                logModel.getB4().get(i) + "\t" + logModel.getB5().get(i) + "\t" + logModel.getB6().get(i) + "\t" + logModel.getS1().get(i) + "\t" + logModel.getS2().get(i) + "\t" +
                                logModel.getS3().get(i) + "\t" +logModel.getS4().get(i) + "\t" + logModel.getS5().get(i) + "\t" + logModel.getS6().get(i) + "\t"  + logModel.getProductName().get(i) + "\t" +
                                logModel.getCourierName().get(i) + "\t" + seqP + "\t" + SeqC + "\t" + seqA + "\t" + logModel.getSs1().get(i) + "\t" + logModel.getSs2().get(i) + "\t" +
                                logModel.getSs3().get(i) + "\t" + logModel.getSs4().get(i) + "\t" + logModel.getSs5().get(i) + "\t" + logModel.getSs6().get(i) + "\r\n" ;
                    } else {
                        logString = barcode + "\t" + idCustomer + "\t" + logModel.getName1().get(i) + "\t" + logModel.getName2().get(i) + "\t" + logModel.getName3().get(i) + "\t" +
                                logModel.getAddress1().get(i) + "\t" + logModel.getAddress2().get(i) + "\t" + logModel.getAddress3().get(i) + "\t" + logModel.getAddress4().get(i) + "\t" +
                                logModel.getAddress5().get(i) + "\t" + "-" + "\t" + logModel.getB1().get(i) + "\t" + logModel.getB2().get(i) + "\t" + logModel.getB3().get(i) + "\t" +
                                logModel.getB4().get(i) + "\t" + logModel.getB5().get(i) + "\t" + logModel.getB6().get(i) + "\t" + logModel.getS1().get(i) + "\t" + logModel.getS2().get(i) + "\t" +
                                logModel.getS3().get(i) + "\t" +logModel.getS4().get(i) + "\t" + logModel.getS5().get(i) + "\t" + "0" + "\t"  + logModel.getProductName().get(i) + "\t" +
                                logModel.getCourierName().get(i) + "\t" + seqP + "\t" + SeqC + "\t" + seqA + "\t" + logModel.getSs1().get(i) + "\t" + logModel.getSs2().get(i) + "\t" +
                                logModel.getSs3().get(i) + "\t" + logModel.getSs4().get(i) + "\t" + logModel.getSs5().get(i) + "\t" + logModel.getSs6().get(i) + "\r\n" ;
                    }
                    
                    bw.write(logString);
                }
                
                

                stamper.close();
                reader.close();

                //Combine
                PdfReader pdfReader2 = new PdfReader(arrayOutputStream.toByteArray());
                for (int j = 1; j <= pdfReader2.getNumberOfPages(); j++) {
                    copy.addPage(copy.getImportedPage(pdfReader2, j));
                }

                pdfReader2.close();
            }
            
            bw.flush();
            bw.close();

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
            
           
            
        } catch (DocumentException | IOException ex) {
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
