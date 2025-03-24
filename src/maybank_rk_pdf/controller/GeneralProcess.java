/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.controller;

import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import maybank_rk_pdf.model.Connection;
import maybank_rk_pdf.model.LogMaybank;
import maybank_rk_pdf.model.SummaryModel;

/**
 *
 * @author Ratino
 */
public class GeneralProcess {
    SummaryModel summaryModel = new SummaryModel();
    LogMaybank logMaybank = new LogMaybank();
    private String cycle = new String();
    private String currentDirectory = new String();
    private String readyToPrintCycle = new String();
    private String readyToPrintCycleOutput = new String();
    private String readyToPrintCycleReport = new String();
    private String readyToPrintCycleLog = new String();
    private String readyToPrintCycleLogProd = new String();
    private String readyToPrintCycleLogKurir = new String();
    private String readyToPrintCycleLogMaster = new String();
    private String readyToPrintCycleCombine = new String();
    private String readyToPrintCycleSorting = new String();
    private String readyToPrintCycleLogScan = new String();
    private String directoryResource = new String();
    private String directoryNormal = new String();
    private String inputDir = new String();
    private String documentType = "";
    
    protected com.mysql.jdbc.Connection koneksi, koneksi1;
    protected Statement stmt, stmt1;
    
    public GeneralProcess(String[] params){
        inputDir = params[0];
        cycle = params[1];
        documentType = params[2];
        initializeDatabaseConnection();
    }
    
     public void doInProcess() throws SQLException{
        try {
            PathDirectory pd = new PathDirectory();
            this.currentDirectory = ""+new java.io.File(".").getCanonicalPath();
            setResource();
            LogController log = new LogController();
            log.uploadDataKanwil( pd.configurePath(directoryResource + "KANWIL.txt"));
            log.uploadDataResourceKurir(pd.configurePath(directoryResource), stmt);
            createDirectory(currentDirectory, inputDir, cycle);
            Directory dirPdf = new Directory();
            PdfProcessing processing = new PdfProcessing();
            dirPdf.scanPdfFile(inputDir);
            String[] params = {readyToPrintCycleLogProd, readyToPrintCycleLogKurir, readyToPrintCycleLogMaster,
                                readyToPrintCycleOutput, readyToPrintCycleReport, readyToPrintCycleLogScan};
            processing.runProcesing(params, dirPdf, inputDir, inputDir, cycle, documentType, stmt);
            //Create Report
            summaryModel.createSumByKanwil(readyToPrintCycleReport, cycle, stmt);
            summaryModel.createSummary(readyToPrintCycleReport,cycle, documentType,stmt);
            summaryModel.createLogByKanwil(readyToPrintCycleLogKurir, documentType, stmt);
            logMaybank.createLogAllMaybank(readyToPrintCycleLogMaster, documentType, stmt);
            logMaybank.createLogKanwil(readyToPrintCycleLogMaster,documentType, stmt);
            logMaybank.createLogAllADD40(readyToPrintCycleLogMaster, documentType, stmt);
            
        } catch (IOException ex) {
            Logger.getLogger(GeneralProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     }
    
    public void createDirectory(String path, String pathInput, String cycle){
        this.currentDirectory = path;
        PathDirectory pd = new PathDirectory();
        
        readyToPrintCycle = pd.configurePath(this.currentDirectory + "\\READY TO PRINT\\" + "BILLING RK " + cycle);
        readyToPrintCycleLogProd = pd.configurePath(readyToPrintCycle + "\\LOG PRODUKSI\\");
        readyToPrintCycleLog = pd.configurePath(readyToPrintCycle + "\\LOG RK\\");
        readyToPrintCycleLogKurir = pd.configurePath(readyToPrintCycleLog  + "\\LOG KURIR\\");
        readyToPrintCycleLogMaster = pd.configurePath(readyToPrintCycleLog + "\\LOG MAYBANK\\");
        readyToPrintCycleOutput = pd.configurePath(readyToPrintCycle + "\\OUTPUT\\");
        readyToPrintCycleReport = pd.configurePath(readyToPrintCycle + "\\REPORT\\");
//        readyToPrintCycleSorting = pd.configurePath(pathInput + "\\SORTATION\\");
        readyToPrintCycleLogScan = pd.configurePath(readyToPrintCycle + "\\LOG SCAN\\");
        String[] params = {readyToPrintCycle,readyToPrintCycleLog,readyToPrintCycleLogKurir,readyToPrintCycleLogMaster,
                            readyToPrintCycleOutput,readyToPrintCycleReport,readyToPrintCycleCombine,readyToPrintCycleSorting,
                            readyToPrintCycleLogScan, readyToPrintCycleLogProd};
        pd.createDirectory(params);
        setResource();
    }
    
    public void setResource(){
        directoryResource = currentDirectory + "////" + "RESOURCES" + "////";
    }

    public void initializeDatabaseConnection(){
        try {
            Connection connection = new Connection();
            connection.setConnection();
            koneksi = connection.getKoneksi();
            stmt = connection.getStmt();
        } catch (SQLException ex) {
            Logger.getLogger(GeneralProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
