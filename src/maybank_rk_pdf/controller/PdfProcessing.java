/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import maybank_rk_pdf.model.LogModel;

/**
 *
 * @author Ratino
 */
public class PdfProcessing {
    public void runProcesing(Directory dirPdf,String DirectoryInput, String parentInput, String type, Statement stmt) throws IOException, SQLException{
        getLogOnePdf(dirPdf, DirectoryInput, parentInput, "Rekening Koran", type, stmt);
        uploadLogToDb(DirectoryInput + "Log All.txt", stmt);
                
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
    
    public void uploadLogToDb(String pathLog, Statement stmt) throws SQLException{
        LogModel logModel = new LogModel();
        logModel.createTable(stmt);
        logModel.loadLogData(pathLog, stmt);
        logModel.setKodeKanwil(stmt);
        logModel.setKurirByKanwil(stmt);
    }
    
    
}
