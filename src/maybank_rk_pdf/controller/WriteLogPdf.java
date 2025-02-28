
package maybank_rk_pdf.controller;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ratino
 */
public class WriteLogPdf {
    private PathDirectory pd = new PathDirectory();
    private ArrayList<String> seq = new ArrayList<String>();
    private String totalHal = new String();
    private String tNoCard = new String();
    private String kurir = new String(); 
    private String noRekening = new String();
    String typeDoc = "", publicOneline="", tPublicOneLine = "Ratino";
    private BufferedWriter bwLogAll, bwFinal;
    String ListKartu="",Fpage="",Barcode="",FkodePos="",Fkurir="",Kurir="",GrupKurir="",
           Area="",Tgl_surat="",Name="",Addr1="",Addr2="",Addr3="",Addr4="",Addr5="",Addr6="",
           ZipCode="",Provinsi="",Kota="",Policy="",Cycle="",NoCIF="",JnsKartu="",JnsGKartu="",
           JnsAmp="",AchBank="",Brosur="",B1="",B2="",B3="",B4="",B5="",B6="",BlockCode="",JumTrans="",
           Tottagihan="",Rkk="",Qcredit="",Fsample="",jumPage="",jumamp="",Jumampall="",SeqPage="",
           SeqCus="",Fseqenv="",SeqEnv="",PathInt="",SeqBox="",Box="",JnsAmplop2="",JumPage2="", kdCabang="",
            jnsAmp="";
    int seqC=0,seqE=0,seqP=0;
    
    
    
    public ArrayList<String> textParserOnePdf(String inputFile, String fileName, String DirectoryInput, String product, String tanggal) throws IOException{
        TextModification txt = new TextModification();
        getDao dao = new getDao();
        typeDoc = ""; tPublicOneLine = "Ratino";
        String addr1="",addr2="",addr3="",addr4="",addr5="";
        String tBarcode = "";
        
        kdCabang = getBranchCode(fileName);
        noRekening = getNoRekening(fileName);
        
        Path path = Paths.get(inputFile);
        Path parentPath = path.getParent();
        String parentPathString = parentPath.toString();
 
        initialLogs(DirectoryInput); // Create file .log
                
        ArrayList<String>logComponent  = initialLogComponent();
        
        StringBuffer text=new StringBuffer() ;
        String resultText="";
        PdfReader reader;
        String productType = "";

        reader = new PdfReader(inputFile);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(inputFile.substring(0,inputFile.length()-4)+".LOG"));
        TextExtractionStrategy strategy;

        productType = ""+reader.getNumberOfPages();
        
        typeDoc = product;
        totalHal = ""+reader.getNumberOfPages();
        System.out.println("PDF File : "  + inputFile);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            seqP++;
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());            
            text.append(strategy.getResultantText());
            String currentText = "" + strategy.getResultantText().trim();

            resultText=text.toString();
            resultText = resultText.replaceAll("-\n", "");
            //out.println("--> "+resultText);                                                           

            StringTokenizer stringTokenizer=new StringTokenizer(resultText, "\n");    
            String strLine = "";
            while (stringTokenizer.hasMoreTokens()){                                               
                strLine = stringTokenizer.nextToken();                                  
//                if(strLine.toUpperCase().indexOf("KEPADA YTH")>-1){ // True // Return indexOf -1 adalah false
                if(strLine.toUpperCase().indexOf("KEPADA YTH") == -1){ // False
                    seqC++;
//                    strLine = stringTokenizer.nextToken(); //Next Line
                    int j = 1;
                    for (int k = 1; k <= 5; k++) {
                        logComponent.set(k+1, ""); //addr1 - 5 (clear)
                    }
                    while (stringTokenizer.hasMoreTokens()){                        
                        if (j==1 && logComponent.get(j).isEmpty()) logComponent.set(j, strLine); //1 = nama
                            
                        strLine = stringTokenizer.nextToken();
                        if (!strLine.trim().equals("")&& !strLine.trim().contains("NA")){
                            j++;
//                            System.out.println("j "+j);
                            
                            if(strLine.contains("CIF") ){                                
                                logComponent.set(11,extractValueAfterColun(strLine)); // CIF
                            } else if(strLine.contains("Account")){
                                logComponent.set(12,extractValueAfterColun(strLine));
                            } else {
                                String pattern = "\\d{5}"; // regex untuk mencari 5 karakter angka // Kode Pos
                                // membuat objek pattern dengan regex yang sudah ditentukan
                                Pattern p = Pattern.compile(pattern);
                                // mencari pattern pada string strLine
                                Matcher m1 = p.matcher(strLine);
                                if (m1.find()){
                                    String postCode = m1.group();                                  
                                        logComponent.set(10, postCode); // Tulis Post Code
                                }
                            }
                                     
                            if (j==1 && logComponent.get(j).isEmpty()) logComponent.set(j, strLine); //1 = nama
                             else
                                if(logComponent.get(j).isEmpty() & !strLine.contains("CIF") & 
                                   !strLine.contains("No. Rekening"))
                                    
                                    logComponent.set(j, strLine); //2dst = addresS                 

                            if( (strLine.toUpperCase().indexOf("PERIODE LAPORAN")>-1) ) break;
                        }
                    }
                }

                if(strLine.toUpperCase().indexOf("PERIODE LAPORAN")>-1){
                    //polis = strLine.substring(15,24).trim();
                    break;
                }
            }
   
            String barcode = "BARCODE"; //+logComponent.get(0);
            barcode = barcode.replace("*", "");
            String nama = ""+logComponent.get(1);
            if (logComponent.get(6).isEmpty() == true) logComponent.set(6,logComponent.get(5));
            if (logComponent.get(2).isEmpty() == false) addr1 = ""+logComponent.get(2);
            if (logComponent.get(3).isEmpty() == false) addr2 = ""+logComponent.get(3);
            if (logComponent.get(4).isEmpty() == false) addr3 = ""+logComponent.get(4);
            if (logComponent.get(5).isEmpty() == false) addr4 = ""+logComponent.get(5);
            if (logComponent.get(6).isEmpty() == false) addr5 = ""+logComponent.get(6); //
            
//            dao.getBin(noCard);            
//            String jenisAmplop = dao.getJenisAmplop(); 
//            String jenisGrupKartu = dao.getJenisGrupKartu();
//            logComponent.set(10, jenisAmplop);
            
            String kurir = "SAP"; // Default //getKurir(polis,logComponent.get(6));
            NoCIF = logComponent.get(11);           
            jnsAmp = Integer.parseInt(totalHal) >= 90 ? "B" : "A";
//           
            //+txt.norm6Digit(i)
            //out.write("MYB"+product+tanggal.substring(4, 6).trim()+polis.trim()+"\t"+polis+"\t"+nama+"\t"+"-"+"\t"+"-"+"\t"+addr1+"\t"+addr2+"\t"+addr3+"\t"+addr4+"\t"+addr5+"\t"+"-"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+product+"\t"+"POS"+"\t"+ ""+seqP +"\t"+ ""+seqC +"\t"+ ""+seqE +"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\r\n");
//            out.write(barcode+"\t"+polis+"\t"+nama+"\t"+jenisGrupKartu+"\t"+"-"+"\t"+addr1+"\t"+addr2+"\t"+addr3+"\t"+addr4+"\t"+addr5+"\t"+"-"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+product+"\t"+kurir+"\t"+ ""+seqPStr +"\t"+ ""+seqCStr +"\t"+ ""+seqEStr +"\t"+jenisAmplop+"\t"+area+"\t"+totalHal+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\r\n");
            out.write(barcode+"\t"+noRekening+"\t"+nama+"\t"+kdCabang+"\t"+NoCIF+"\t"+addr1+"\t"+addr2+"\t"+addr3+"\t"+addr4+"\t"+addr5+"\t"+"-"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+product+"\t"+kurir+"\t"+ ""+"seqPStr" +"\t"+ ""+"seqCStr" +"\t"+ ""+"seqEStr" +"\t"+""+"\t"+"area"+"\t"+totalHal+"\t"+seqP+"\t"+"1"+"\t"+"0"+"\t"+"0"+"\r\n");
//            bwLogAll.write(barcode+"\t"+polis+"\t"+nama+"\t"+"jenisGrupKartu"+"\t"+"-"+"\t"+addr1+"\t"+addr2+"\t"+addr3+"\t"+addr4+"\t"+addr5+"\t"+"-"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+product+"\t"+kurir+"\t"+ seqPStr +"\t"+ seqPStr +"\t"+ seqPStr +"\t"+jenisAmplop+"\t"+area+"\t"+totalHal+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\r\n");
            bwLogAll.write(barcode+"\t"+noRekening+"\t"+nama+"\t"+kdCabang+"\t"+NoCIF+"\t"+addr1+"\t"+addr2+"\t"+addr3+"\t"+addr4+"\t"+addr5+"\t"+"-"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+"0"+"\t"+product+"\t"+kurir+"\t"+ seqP +"\t"+ 0 +"\t"+ 0 +"\t"+totalHal+"\t"+jnsAmp+"\t"+0+"\t"+"0"+"\t"+"1"+"\t"+"0"+"\t"+"0"+"\r\n");
            if (!publicOneline.equals(tPublicOneLine)) {
                bwFinal.write(publicOneline + "\r\n");
            }            
            tPublicOneLine = publicOneline;
        }
                
        
        out.flush();
        out.close();
        reader.close();
        
        bwFinal.flush();
        bwFinal.close();                
        
        closeFileLogAll();
        //copyToSort(DirectoryInput+"SORTATION"+"\\\\", DirectoryInput+product+"\\\\", kurir);

        seq.add(""+seqC);
        seq.add(""+seqE);
        seq.add(""+seqP);
//        seq.add(""+product);
//        seq.add(""+tanggal);                 
        
        return logComponent;
    }
    
    private void initialLogs(String directoryInput) throws FileNotFoundException{
        
        String logPath = pd.configurePath(directoryInput) + "\\\\"+"Log All.txt";
        String finalLogPath = pd.configurePath(directoryInput) + "\\\\"+"t_final.log";       
        
        FileOutputStream outputStream1 = new FileOutputStream(logPath,true);
        bwLogAll = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(outputStream1)));
        
        FileOutputStream outputStream2 = new FileOutputStream(finalLogPath);
        bwFinal = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(outputStream2)));
    }
    
    private ArrayList<String> initialLogComponent(){
        ArrayList<String> logCompnent = new ArrayList<>(Collections.nCopies(17, ""));
        return logCompnent;
    }
    
    public String getBranchCode(String fileName){
        String[] split = fileName.split("_");
        String kode = split[2].substring(1, 4);
        return kode;
    }
    
    public String getNoRekening(String fileName){
        String[] split = fileName.split("_");
        return split[2];
    }
    
    
    public void updateLogComponent(List<String> logComponent, int index, String value){
        logComponent.set(index, value);
    }
    
    public String extractValueAfterColun(String strLine){
        return strLine.substring(strLine.indexOf(":")+1, strLine.length()).trim();
    }
    
     public void closeFileLogAll() throws IOException{
        bwLogAll.flush();
        bwLogAll.close();
        
        //bwFinal.flush();
        //bwFinal.close();
    }
     
    public void DeleteFileIfExsit(String fileExist){
        File file = new File(fileExist);
        if(file.exists()){
            file.delete();
        }
    }
    
}
