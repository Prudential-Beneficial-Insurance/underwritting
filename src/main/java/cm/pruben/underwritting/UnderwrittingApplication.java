package cm.pruben.underwritting;

import cm.pruben.underwritting.config.DataSourceConfig;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;

@SpringBootApplication
public class UnderwrittingApplication {


    public static void main(String[] args) throws IOException, DocumentException, SQLException, ClassNotFoundException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(UnderwrittingApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);

        //generatecheckrequisition();

        //SpringApplication.run(UnderwrittingApplication.class, args);
        statecom();
        generatecontract();
        generatecontractwithoutheader();
    }

    public static void statecom() throws SQLException, ClassNotFoundException, IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("cadun10.txt"));
        String value;

        File file = new File("sortie.txt");
        PrintStream ps = new PrintStream(file);

        while ((value = bufferedReader.readLine()) != null && value.length()!=1) {
            System.setOut(ps);
            System.out.println(value.substring(10));

        }
    }

    public static void generatecontract() throws IOException, DocumentException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("sortie.txt"));
        Document pdfDoc = new Document(PageSize.A4,45,5,5,-5);
        FileOutputStream fileOutputStream = new FileOutputStream("CONTRAT UND.pdf");
        PdfWriter.getInstance(pdfDoc, fileOutputStream)
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        Font myfont = FontFactory.getFont("Courier",9);
        myfont.setStyle(Font.NORMAL);
        String value;

        int inc=0;
        while ((value = bufferedReader.readLine()) != null) {

            if(value.startsWith("1")){
                inc = 1;
                continue;
            }
            if(inc<6){
                inc++;
                continue;
            }
            if (inc == 6){
                pdfDoc.newPage();

                Font headerfont = FontFactory.getFont("Courier",8);
                headerfont.setStyle(Font.NORMAL);
                PdfPTable HeaderTable = new PdfPTable(3);
                Image Logo = Image.getInstance("logo.png");

                PdfPCell cellLogo= new PdfPCell(Logo, true);
                cellLogo.setBorder(Rectangle.NO_BORDER);
                cellLogo.setFixedHeight(30);
                HeaderTable.addCell(cellLogo).setRowspan(4);

                Paragraph paraphimmeuble = new Paragraph(String.format("Immeuble PruBeneficial Insurance"),headerfont);
                Paragraph paraphboulevard = new Paragraph(String.format("1944 Boulevard de la République"),headerfont);
                Paragraph paraphdouala = new Paragraph(String.format("BP 2328 Douala, Cameroun "),headerfont);
                Paragraph paraphinfos = new Paragraph(String.format("E : infos@prubeneficial.cm"),headerfont);
                HeaderTable.addCell(new PdfPCell(paraphimmeuble)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphboulevard)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphdouala)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphinfos)).setBorder(Rectangle.NO_BORDER);

                Paragraph paraphcli = new Paragraph(String.format("E : clientele@prubeneficial.cm"),headerfont);
                Paragraph paraphnumber1 = new Paragraph(String.format("T : (237) 233 42 42 36 / 233 42 76 91 "),headerfont);
                Paragraph paraphnumber2= new Paragraph(String.format("F : (237) 233 42 77 54 "),headerfont);
                Paragraph paraphemail = new Paragraph(String.format("www.prubeneficial.cm"),headerfont);
                HeaderTable.addCell(new PdfPCell(paraphcli)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphnumber1)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphnumber2)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphemail)).setBorder(Rectangle.NO_BORDER);

                HeaderTable.setWidthPercentage(100);
                pdfDoc.add(HeaderTable);
            }if(value.contains("Le Contractant                  L'Assur")){

                //System.out.println(value.substring(10));
                Paragraph para = new Paragraph( String.format("%s", value.substring(9).replace("{","é").replace("}","è").replace("@","à") +"\n"),myfont);
                para.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(para);

                Font headerfont = FontFactory.getFont("Courier",8);
                headerfont.setStyle(Font.NORMAL);
                Paragraph pentre = new Paragraph( String.format("Entreprise régie par le code des Assurances de la CIMA"),headerfont);
                pentre.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(pentre);
                Paragraph psociety = new Paragraph( String.format("Société Anonyme avec conseil d’Administration - capital social: 6 380 000 000 FCFA"),headerfont);
                psociety.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(psociety);
                Paragraph pstat = new Paragraph( String.format("R.C N° 014.253 Douala – Statistique 1006101 M - N° Cont. M119400001270C"),headerfont);
                pstat.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(pstat);

                inc++;
                continue;
            }
            if(!value.isEmpty()){
                Paragraph para = new Paragraph( String.format("%s", value.substring(9).replace("{","é").replace("}","è").replace("@","à") +"\n"),myfont);
                pdfDoc.add(para);
                inc++;
            }
            if(value.isEmpty()){
                Paragraph para = new Paragraph( String.format("%s", value +"\n"),myfont);
                pdfDoc.add(para);
                inc++;
            }
            /*Paragraph para = new Paragraph( String.format("%s", value +"\n"),myfont);
            //para.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
            pdfDoc.add(para);
            inc++;*/
        }
        pdfDoc.close();
        bufferedReader.close();
    }
    public static void generatecontractwithoutheader() throws IOException, DocumentException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("sortie.txt"));
        Document pdfDoc = new Document(PageSize.A4,45,5,60,-5);
        FileOutputStream fileOutputStream = new FileOutputStream("CONTRAT UND SANS ENTETE.pdf");
        PdfWriter.getInstance(pdfDoc, fileOutputStream)
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        Font myfont = FontFactory.getFont("Courier",9);
        myfont.setStyle(Font.NORMAL);
        String value;

        int inc=0;
        while ((value = bufferedReader.readLine()) != null) {

            if(value.startsWith("1")){
                inc = 1;
                continue;
            }
            if(inc<6){
                inc++;
                continue;
            }
            if (inc == 6){
                pdfDoc.newPage();

                /*Font headerfont = FontFactory.getFont("Courier",8);
                headerfont.setStyle(Font.NORMAL);
                PdfPTable HeaderTable = new PdfPTable(3);
                Image Logo = Image.getInstance("logo.png");

                PdfPCell cellLogo= new PdfPCell(Logo, true);
                cellLogo.setBorder(Rectangle.NO_BORDER);
                cellLogo.setFixedHeight(30);
                HeaderTable.addCell(cellLogo).setRowspan(4);

                Paragraph paraphimmeuble = new Paragraph(String.format("Immeuble PruBeneficial Insurance"),headerfont);
                Paragraph paraphboulevard = new Paragraph(String.format("1944 Boulevard de la République"),headerfont);
                Paragraph paraphdouala = new Paragraph(String.format("BP 2328 Douala, Cameroun "),headerfont);
                Paragraph paraphinfos = new Paragraph(String.format("E : infos@prubeneficial.cm"),headerfont);
                HeaderTable.addCell(new PdfPCell(paraphimmeuble)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphboulevard)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphdouala)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphinfos)).setBorder(Rectangle.NO_BORDER);

                Paragraph paraphcli = new Paragraph(String.format("E : clientele@prubeneficial.cm"),headerfont);
                Paragraph paraphnumber1 = new Paragraph(String.format("T : (237) 233 42 42 36 / 233 42 76 91 "),headerfont);
                Paragraph paraphnumber2= new Paragraph(String.format("F : (237) 233 42 77 54 "),headerfont);
                Paragraph paraphemail = new Paragraph(String.format("www.prubeneficial.cm"),headerfont);
                HeaderTable.addCell(new PdfPCell(paraphcli)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphnumber1)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphnumber2)).setBorder(Rectangle.NO_BORDER);
                HeaderTable.addCell(new PdfPCell(paraphemail)).setBorder(Rectangle.NO_BORDER);

                HeaderTable.setWidthPercentage(100);
                pdfDoc.add(HeaderTable);*/
            }if(value.contains("Le Contractant                  L'Assur")){

                //System.out.println(value.substring(10));
                Paragraph para = new Paragraph( String.format("%s", value.substring(9).replace("{","é").replace("}","è").replace("@","à") +"\n"),myfont);
                para.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(para);

                /*Font headerfont = FontFactory.getFont("Courier",8);
                headerfont.setStyle(Font.NORMAL);
                Paragraph pentre = new Paragraph( String.format("Entreprise régie par le code des Assurances de la CIMA"),headerfont);
                pentre.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(pentre);
                Paragraph psociety = new Paragraph( String.format("Société Anonyme avec conseil d’Administration - capital social: 6 380 000 000 FCFA"),headerfont);
                psociety.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(psociety);
                Paragraph pstat = new Paragraph( String.format("R.C N° 014.253 Douala – Statistique 1006101 M - N° Cont. M119400001270C"),headerfont);
                pstat.setAlignment(Element.ALIGN_LEFT);
                pdfDoc.add(pstat);*/

                inc++;
                continue;
            }
            if(!value.isEmpty()){
                Paragraph para = new Paragraph( String.format("%s", value.substring(9).replace("{","é").replace("}","è").replace("@","à") +"\n"),myfont);
                pdfDoc.add(para);
                inc++;
            }
            if(value.isEmpty()){
                Paragraph para = new Paragraph( String.format("%s", value +"\n"),myfont);
                pdfDoc.add(para);
                inc++;
            }
            /*Paragraph para = new Paragraph( String.format("%s", value +"\n"),myfont);
            //para.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
            pdfDoc.add(para);
            inc++;*/
        }
        pdfDoc.close();
        bufferedReader.close();
    }

    public static void generatecheckrequisition() throws IOException, DocumentException {
        FileInputStream file = new FileInputStream(new File("lldea.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Font myfont = FontFactory.getFont("Arial",12);
        myfont.setStyle(Font.NORMAL);

        Document pdfDoc = new Document(PageSize.A4,45,5,80,-5);
        FileOutputStream fileOutputStream = new FileOutputStream("CHECK REQUISITION.pdf");
        PdfWriter.getInstance(pdfDoc, fileOutputStream).setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        Font headerfont = FontFactory.getFont("Arial",10);
        headerfont.setStyle(Font.NORMAL);
        int c=0;
        for(Row row : sheet){
            c++;
            if(c==1) continue;
            //if(c==11) break;
            pdfDoc.newPage();

            double amnt = row.getCell(28).getNumericCellValue();
            int amtvl = (int) amnt;

            NumberFormat formatter = null;
            formatter=java.text.NumberFormat.getInstance(java.util.Locale.FRENCH);
            formatter = new DecimalFormat("##,###.## ");

            Paragraph chkreq = new Paragraph("CHEQUE REQUISITION",myfont);
            chkreq.setAlignment(Element.ALIGN_CENTER);
            pdfDoc.add(chkreq);

            PdfPTable infosTable = new PdfPTable(4);

            Paragraph paradate = new Paragraph(String.format("DATE..........:"),headerfont);
            Paragraph paradatev = new Paragraph(String.format("   31/05/2023"),headerfont);
            Paragraph paravide1 = new Paragraph(String.format("             "),headerfont);
            Paragraph paravide2 = new Paragraph(String.format("             "),headerfont);
            infosTable.addCell(new PdfPCell(paradate)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(paradatev)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(paravide1)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(paravide2)).setBorder(Rectangle.NO_BORDER);


            Paragraph parapayable = new Paragraph(String.format("PAYABLE TO....:"),headerfont);
            Paragraph parapayablev = new Paragraph(String.format("   "+row.getCell(2)),headerfont);
            PdfPCell cell = new PdfPCell(parapayablev);
            //cell.setHorizontalAlignment(10);
            cell.setColspan(2);
            Paragraph paraamountvp = new Paragraph(String.format("               "),headerfont);//montant
            //Paragraph paradescriptionvp = new Paragraph(String.format("           "),headerfont);
            infosTable.addCell(new PdfPCell(parapayable)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(cell).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(paraamountvp)).setBorder(Rectangle.NO_BORDER);
            //infosTable.addCell(new PdfPCell(paradescriptionvp)).setBorder(Rectangle.NO_BORDER);

            Paragraph amount = new Paragraph(String.format("AMOUNT........:"),headerfont);
            Paragraph amountv = new Paragraph(String.format("      "+amtvl,headerfont));
            Paragraph bank = new Paragraph(String.format("BANK..........:"),headerfont);//montant
            Paragraph bankv = new Paragraph(String.format("   "+row.getCell(38)),headerfont);
            infosTable.addCell(new PdfPCell(amount)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(amountv)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(bank)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(bankv)).setBorder(Rectangle.NO_BORDER);

            Paragraph dsc = new Paragraph(String.format("DESCRITION..........:"),headerfont);
            Paragraph dscv = new Paragraph(String.format("              "),headerfont);
            Paragraph dscv1 = new Paragraph(String.format("              "),headerfont);
            Paragraph dscv2 = new Paragraph(String.format("              "),headerfont);
            infosTable.addCell(new PdfPCell(dsc)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(dscv)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(dscv1)).setBorder(Rectangle.NO_BORDER);
            infosTable.addCell(new PdfPCell(dscv2)).setBorder(Rectangle.NO_BORDER);

            infosTable.setWidthPercentage(100);

            pdfDoc.add(infosTable);

            Paragraph commis = new Paragraph("Commissions for the month of MAY 2023",myfont);
            commis.setIndentationLeft(100.0f);
            pdfDoc.add(commis);

            Paragraph agence = new Paragraph(row.getCell(8).toString()+" AGENCY",myfont);
            agence.setIndentationLeft(100.0f);
            pdfDoc.add(agence);

            PdfPTable requestTable = new PdfPTable(4);

            Paragraph reqby = new Paragraph(String.format("REQUESTED BY.......:"),headerfont);
            Paragraph reqbyv = new Paragraph(String.format("       "),headerfont);
            Paragraph dptm = new Paragraph(String.format("DEPT :"),headerfont);
            Paragraph mark = new Paragraph(String.format("MARKETING"),headerfont);
            requestTable.addCell(new PdfPCell(reqby)).setBorder(Rectangle.NO_BORDER);
            requestTable.addCell(new PdfPCell(reqbyv)).setBorder(Rectangle.NO_BORDER);
            requestTable.addCell(new PdfPCell(dptm)).setBorder(Rectangle.NO_BORDER);
            requestTable.addCell(new PdfPCell(mark)).setBorder(Rectangle.NO_BORDER);


            Paragraph appby = new Paragraph(String.format("APPROVED BY....:"),headerfont);
            Paragraph appbyv = new Paragraph(String.format("               "),headerfont);
            Paragraph apdept = new Paragraph(String.format("DEPT"),headerfont);//montant
            Paragraph acctng = new Paragraph(String.format("ACCOUNTING"),headerfont);
            requestTable.addCell(new PdfPCell(appby)).setBorder(Rectangle.NO_BORDER);
            requestTable.addCell(new PdfPCell(appbyv)).setBorder(Rectangle.NO_BORDER);
            requestTable.addCell(new PdfPCell(apdept)).setBorder(Rectangle.NO_BORDER);
            requestTable.addCell(new PdfPCell(acctng)).setBorder(Rectangle.NO_BORDER);

            requestTable.setWidthPercentage(100);

            pdfDoc.add(requestTable);

            Paragraph line1 = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------",myfont);
            line1.setAlignment(Element.ALIGN_LEFT);
            pdfDoc.add(line1);

            Paragraph recby = new Paragraph("RECEIVED BY  :");
            recby.setAlignment(Element.ALIGN_LEFT);
            pdfDoc.add(recby);

            Paragraph line2 = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------",myfont);
            line2.setAlignment(Element.ALIGN_LEFT);
            pdfDoc.add(line2);

            Paragraph facc = new Paragraph("FOR ACCOUNTING USE ONLY  :");
            facc.setAlignment(Element.ALIGN_LEFT);
            pdfDoc.add(facc);

            PdfPTable accTable = new PdfPTable(2);

            Paragraph chk = new Paragraph(String.format("CHEQUE No.......:"),headerfont);
            Paragraph chkv = new Paragraph(String.format("       "),headerfont);
            Paragraph debit = new Paragraph(String.format("DEBIT A/C No :"),headerfont);
            Paragraph debitv = new Paragraph(String.format("   "+row.getCell(1)),headerfont);
            Paragraph voucher = new Paragraph(String.format("VOUCHER :"),headerfont);
            Paragraph voucherv = new Paragraph(String.format("          "),headerfont);
            accTable.addCell(new PdfPCell(chk)).setBorder(Rectangle.NO_BORDER);
            accTable.addCell(new PdfPCell(chkv)).setBorder(Rectangle.NO_BORDER);
            accTable.addCell(new PdfPCell(debit)).setBorder(Rectangle.NO_BORDER);
            accTable.addCell(new PdfPCell(debitv)).setBorder(Rectangle.NO_BORDER);
            accTable.addCell(new PdfPCell(voucher)).setBorder(Rectangle.NO_BORDER);
            accTable.addCell(new PdfPCell(voucherv)).setBorder(Rectangle.NO_BORDER);
            pdfDoc.add(accTable);

        }
        pdfDoc.close();
    }

}
