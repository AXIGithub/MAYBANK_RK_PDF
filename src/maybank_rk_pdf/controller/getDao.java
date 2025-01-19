/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maybank_rk_pdf.controller;

import javax.swing.JOptionPane;

/**
 *
 * @author Ratino
 */
public class getDao {
    
    private String jenisKartu = new String();
    private String jenisGrupKartu = new String();
    private String jenisAmplop = new String();
    
    /**
     * The getBin method is responsible for determining the type of credit card based on the first few digits of the card number
     * @param noCard 
     */
    
    public void getBin(String noCard) {
        //String jenisKartu = "", jenisGrupKartu = "", jenisAmplop = "";
        setJenisKartu(""); setJenisKartu(""); setJenisAmplop("");
        if(noCard.substring(0, 6).contains("356285")){
            jenisKartu = "JCB Platinum"; jenisGrupKartu = "JCB"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 6).contains("404776")){
            jenisKartu = "Visa Infinite"; jenisGrupKartu = "INFINITE"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 6).contains("420183")){
            jenisKartu = "Visa Classic BAG"; jenisGrupKartu = "REGULAR"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 8).contains("42410300")){
            jenisKartu = "Visa Gold BAG"; jenisGrupKartu = "REGULAR"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 6).contains("493828")){
            jenisKartu = "Visa Classic"; jenisGrupKartu = "VISA CLASSIC"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 8).contains("42601320")){
            jenisKartu = "Visa Platinum Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 6).contains("493828")){
            jenisKartu = "Visa Classic"; jenisGrupKartu = "VISA CLASSIC"; jenisAmplop = "PLATINUM";
        } else if(noCard.substring(0, 8).contains("42601320")) {
            jenisKartu = "Visa Platinum Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("426013")) {
            jenisKartu = "Visa Platinum"; jenisGrupKartu = "PLATINUM"; jenisAmplop = "PLATINUM";
        } else if (noCard.equals("44237320")) {
            jenisKartu = "Visa Classic Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("442373")) {
            jenisKartu = "Visa Classic"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 8).contains("44237420")) {
            jenisKartu = "Visa Gold Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("442374")) {
            jenisKartu = "Visa Gold"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("456781")) {
            jenisKartu = "Visa Gold Corporate"; jenisGrupKartu = "CORPORATE"; jenisAmplop = "CORPORATE";
        } else if (noCard.substring(0, 6).contains("464987")) {
            jenisKartu = "Visa Platinum Corporate"; jenisGrupKartu = "CORPORATE"; jenisAmplop = "CORPORATE";
        } else if (noCard.substring(0, 6).contains("542449")) {
            jenisKartu = "Master Classic Old"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("540160")) {
            jenisKartu = "Master Classic Old"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("515595")) {
            jenisKartu = "Master New Jobber(White Card)"; jenisGrupKartu = "WHITE CARD"; jenisAmplop = "PLATINUM";
        } else if (noCard.substring(0, 6).contains("520037")) {
            jenisKartu = "MC2 (Angry Bird)"; jenisGrupKartu = "MC2"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 8).contains("54529869")) {
            jenisKartu = "Master Classic Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("545298")) {
            jenisKartu = "Master Classic"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 8).contains("54529969")) {
            jenisKartu = "Master Gold Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("545299")) {
            jenisKartu = "Master Gold"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 8).contains("55200868")) {
            jenisKartu = "Master Platinum Lion"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("552008")) {
            jenisKartu = "Master Platinum"; jenisGrupKartu = "PLATINUM"; jenisAmplop = "PLATINUM";
        } else if (noCard.substring(0, 6).contains("887970")) {
            jenisKartu = "SINERGI"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("515595")) {
            jenisKartu = "Poin Reward"; jenisGrupKartu = "REGULAR"; jenisAmplop = "REGULAR";
        } else if (noCard.substring(0, 6).contains("886638")) {
            jenisKartu = "Maybank Purchase"; jenisGrupKartu = "PURCHASE"; jenisAmplop = "PURCHASE";
        } else if (noCard.substring(0, 6).contains("356284")) {
            jenisKartu = "JCB Classic"; jenisGrupKartu = "GOLD"; jenisAmplop = "GOLD";
        } else if (noCard.substring(0, 6).contains("493829")) {
            jenisKartu = "Visa Gold Lama"; jenisGrupKartu = "GOLD"; jenisAmplop = "GOLD";
        } else {
            JOptionPane.showMessageDialog(null, "unknown card number!\n" + noCard + "\r\n" +"Hubungi RM!! " + "\r\nIni Jenis kartu Apa? jenis Amplop apa?");
        }
    }

    public String getJenisAmplop() {
        return jenisAmplop;
    }

    public void setJenisAmplop(String jenisAmplop) {
        this.jenisAmplop = jenisAmplop;
    }
           
    public String getJenisKartu() {
        return jenisKartu;
    }

    public void setJenisKartu(String jenisKartu) {
        this.jenisKartu = jenisKartu;
    }

    public String getJenisGrupKartu() {
        return jenisGrupKartu;
    }

    public void setJenisGrupKartu(String jenisGrupKartu) {
        this.jenisGrupKartu = jenisGrupKartu;
    }
    
    
    
    
    
}
