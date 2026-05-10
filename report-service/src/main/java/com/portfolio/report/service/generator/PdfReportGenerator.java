package com.portfolio.report.service.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.portfolio.report.dto.HoldingReportDto;
import com.portfolio.report.dto.ReportDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component("pdfReportGenerator")
@Slf4j
public class PdfReportGenerator implements ReportGenerator {

    @Override
    public byte[] generate(ReportDataDto data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10);

            document.add(new Paragraph("Portfolio Summary Report", titleFont));
            document.add(new Paragraph("Portfolio: " + data.getPortfolioName(), headerFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Total Invested: " + data.getTotalInvestedValue(), bodyFont));
            document.add(new Paragraph("Total Current Value: " + data.getTotalCurrentValue(), bodyFont));
            document.add(new Paragraph("Total Gain/Loss: " + data.getTotalGainLoss()
                    + " (" + data.getTotalGainLossPercent() + "%)", bodyFont));
            document.add(Chunk.NEWLINE);

            if (data.getHoldings() != null && !data.getHoldings().isEmpty()) {
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);

                String[] headers = {"Symbol", "Qty", "Buy Price", "Current Price",
                        "Current Value", "Gain/Loss", "Gain/Loss %"};
                for (String h : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);
                }

                for (HoldingReportDto h : data.getHoldings()) {
                    table.addCell(new Phrase(h.getStockSymbol(), bodyFont));
                    table.addCell(new Phrase(h.getQuantity().toString(), bodyFont));
                    table.addCell(new Phrase(h.getBuyPrice().toString(), bodyFont));
                    table.addCell(new Phrase(h.getCurrentPrice().toString(), bodyFont));
                    table.addCell(new Phrase(h.getCurrentValue().toString(), bodyFont));
                    table.addCell(new Phrase(h.getGainLoss().toString(), bodyFont));
                    table.addCell(new Phrase(h.getGainLossPercent().toString() + "%", bodyFont));
                }

                document.add(table);
            }

            document.close();
            log.info("PDF report generated for portfolio: {}", data.getPortfolioId());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Failed to generate PDF report", e);
            throw new RuntimeException("PDF generation failed: " + e.getMessage());
        }
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }
}