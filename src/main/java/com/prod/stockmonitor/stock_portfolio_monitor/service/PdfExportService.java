package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfExportService {

    public ByteArrayOutputStream generatePortfolioPdf(Portfolio portfolio) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();


        document.add(new Paragraph("Portfolio Summary for: " + portfolio.getPortfolioName()));

        if (portfolio.getUser() != null && portfolio.getUser().getFullName() != null) {
            document.add(new Paragraph("User: " + portfolio.getUser().getFullName()));
        } else {
            document.add(new Paragraph("User: N/A"));
        }
        document.add(new Paragraph("Date: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Stock Holdings:"));
        if (portfolio.getStockHoldings() != null && !portfolio.getStockHoldings().isEmpty()) {
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] stockHeaders = {"Symbol", "Name", "Quantity", "Buy Price", "Current Price", "Gain/Loss"};
            for (String header : stockHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                table.addCell(cell);
            }

            for (StockHolding holding : portfolio.getStockHoldings()) {
                table.addCell(holding.getStockSymbol());
                table.addCell(holding.getStockName());
                table.addCell(String.format("%.2f", holding.getQuantity()));
                table.addCell(String.format("%.2f", holding.getBuyPrice()));

                table.addCell(String.format("%.2f", holding.getCurrentPrice() != null ? holding.getCurrentPrice() : 0.0));
                table.addCell(String.format("%.2f", holding.getGainLoss() != null ? holding.getGainLoss() : 0.0));
            }
            document.add(table);
        } else {
            document.add(new Paragraph("No stock holdings found for this portfolio."));
        }

        document.close();
        return outputStream;
    }
}