package it.ncc.BackAndNcc.mail;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class PdfGenerator {

    public static void generateHtmlPdf(String filePath, String html) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Converti HTML in PDF
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(html));

            document.close();
            System.out.println("PDF generato con successo: " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

}
