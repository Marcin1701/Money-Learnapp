package polsl.moneysandbox.api.answer.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;
import polsl.moneysandbox.api.answer.response.AnswersSummary;
import com.itextpdf.text.pdf.PdfWriter;
import polsl.moneysandbox.model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfReportService {

    public static ByteArrayInputStream generatePdfReport(AnswersSummary answersSummary, User user) throws DocumentException, IOException {
        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font helvetica16 = new Font(helvetica,16);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        PdfPTable table = new PdfPTable(new float[]{2.5f, 1f});
        table.setWidthPercentage(90f);

        document.addAuthor("Szkatułkowy Ambaras");
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Poniżej znajduje się podsumowanie Twoich wyników:", helvetica16));
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        table.addCell(insertCell("Opis Aktywności", Element.ALIGN_CENTER, 1, helvetica16));
        table.addCell(insertCell("Wynik Aktywności", Element.ALIGN_CENTER, 1, helvetica16));

        table.addCell(insertCell("", Element.ALIGN_LEFT, 4, helvetica16));

        table.addCell(insertCell("Wszystkich odpowiedzi", Element.ALIGN_CENTER, 1, helvetica16));
        table.addCell(insertCell(answersSummary.getAllAnswers().toString(), Element.ALIGN_CENTER, 1, helvetica16));

        float percentage = 100f * answersSummary.getAllCorrectQuestions() / answersSummary.getAllQuestions();

        table.addCell(insertCell("Wynik Procentowy", Element.ALIGN_CENTER, 1, helvetica16));
        table.addCell(insertCell(String.format("%.2f", percentage) + "%", Element.ALIGN_CENTER, 1, helvetica16));

        table.addCell(insertCell("Wszystkich Pytań", Element.ALIGN_CENTER, 1, helvetica16));
        table.addCell(insertCell(answersSummary.getAllQuestions().toString(), Element.ALIGN_CENTER, 1, helvetica16));

        table.addCell(insertCell("Wszystkich Poprawnych Pytań", Element.ALIGN_CENTER, 1, helvetica16));
        table.addCell(insertCell(answersSummary.getAllCorrectQuestions().toString(), Element.ALIGN_CENTER, 1, helvetica16));

        table.addCell(insertCell("Wszystkich Błędnych Pytań", Element.ALIGN_CENTER, 1, helvetica16));
        table.addCell(insertCell(answersSummary.getAllWrongQuestions().toString(), Element.ALIGN_CENTER, 1, helvetica16));

        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Widoczne wyniki dotyczą: " + user.getLogin(), helvetica16));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Imię: " + user.getFirstName(), helvetica16));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Nazwisko: " + user.getLastName(), helvetica16));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Data utworzenia konta: " + user.getCreationDate().toString(), helvetica16));
        document.add(Chunk.NEWLINE);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static PdfPCell insertCell(String text, int align, int colspan, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        return cell;
    }
}
