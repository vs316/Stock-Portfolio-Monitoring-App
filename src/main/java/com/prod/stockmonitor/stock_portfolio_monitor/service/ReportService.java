package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioSummaryDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.StockHoldingSummaryDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.PortfolioRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.StockHoldingRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// iText Imports
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Service
public class ReportService {

    private final PortfolioRepository portfolioRepository;
    private final StockHoldingRepository stockHoldingRepository;
    private final UserRepository userRepository;


    public ReportService(PortfolioRepository portfolioRepository,
                         StockHoldingRepository stockHoldingRepository,
                         UserRepository userRepository
        ) {
        this.portfolioRepository = portfolioRepository;
        this.stockHoldingRepository = stockHoldingRepository;
        this.userRepository = userRepository;

    }


    public PortfolioSummaryDTO getPortfolioSummary(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        UserClass user = userRepository.findById(portfolio.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found for portfolio ID: " + portfolioId));

        List<StockHolding> holdings = stockHoldingRepository.findByPortfolioId(portfolioId);

        Double totalInvestedValue = 0.0;
        Double currentMarketValue = 0.0;
        List<StockHoldingSummaryDTO> stockSummaries = new ArrayList<>();

        for (StockHolding holding : holdings) {

            Double currentPrice = holding.getCurrentPrice() != null ? holding.getCurrentPrice() : holding.getBuyPrice(); // Fallback
            if (currentPrice == null) {

                currentPrice = holding.getBuyPrice();
            }


            Double investedValue = holding.getQuantity() * holding.getBuyPrice();
            Double marketValue = holding.getQuantity() * currentPrice;
            Double gainLoss = marketValue - investedValue;
            Double gainLossPercentage = (investedValue != 0) ? (gainLoss / investedValue) * 100 : 0.0;

            totalInvestedValue += investedValue;
            currentMarketValue += marketValue;

            StockHoldingSummaryDTO stockSummary = new StockHoldingSummaryDTO(
                    holding.getId(),
                    holding.getStockSymbol(),
                    holding.getStockName(),
                    holding.getQuantity(),
                    holding.getBuyPrice(),
                    currentPrice,
                    gainLoss,
                    gainLossPercentage
            );
            stockSummaries.add(stockSummary);
        }

        Double totalGainLoss = currentMarketValue - totalInvestedValue;
        Double totalGainLossPercentage = (totalInvestedValue != 0) ? (totalGainLoss / totalInvestedValue) * 100 : 0.0;

        return new PortfolioSummaryDTO(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                user.getId(),
                user.getFullName(),
                totalInvestedValue,
                currentMarketValue,
                totalGainLoss,
                totalGainLossPercentage,
                LocalDate.now(),
                stockSummaries
        );
    }


    public byte[] exportPortfolioSummaryToExcel(Long portfolioId) {
        PortfolioSummaryDTO summary = getPortfolioSummary(portfolioId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Portfolio Summary");

            Row headerRow1 = sheet.createRow(0);
            headerRow1.createCell(0).setCellValue("Portfolio Name:");
            headerRow1.createCell(1).setCellValue(summary.portfolioName());

            Row headerRow2 = sheet.createRow(1);
            headerRow2.createCell(0).setCellValue("As Of Date:");
            headerRow2.createCell(1).setCellValue(summary.asOfDate().toString());

            Row headerRow3 = sheet.createRow(2);
            headerRow3.createCell(0).setCellValue("Total Invested Value:");
            headerRow3.createCell(1).setCellValue(summary.totalInvestedValue());

            Row headerRow4 = sheet.createRow(3);
            headerRow4.createCell(0).setCellValue("Current Market Value:");
            headerRow4.createCell(1).setCellValue(summary.currentMarketValue());

            Row headerRow5 = sheet.createRow(4);
            headerRow5.createCell(0).setCellValue("Total Gain/Loss:");
            headerRow5.createCell(1).setCellValue(summary.totalGainLoss());

            Row headerRow6 = sheet.createRow(5);
            headerRow6.createCell(0).setCellValue("Total Gain/Loss Percentage:");
            headerRow6.createCell(1).setCellValue(summary.totalGainLossPercentage() + "%");


            int rowNum = 8;
            Row stockHeaderRow = sheet.createRow(rowNum++);
            String[] stockHeaders = {"Holding ID", "Symbol", "Name", "Quantity", "Buy Price", "Current Price", "Gain/Loss", "Gain/Loss %"};
            for (int i = 0; i < stockHeaders.length; i++) {
                stockHeaderRow.createCell(i).setCellValue(stockHeaders[i]);
            }

            for (StockHoldingSummaryDTO stock : summary.stockHoldings()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.holdingId());
                row.createCell(1).setCellValue(stock.stockSymbol());
                row.createCell(2).setCellValue(stock.stockName());
                row.createCell(3).setCellValue(stock.quantity());
                row.createCell(4).setCellValue(stock.buyPrice());
                row.createCell(5).setCellValue(stock.currentPrice());
                row.createCell(6).setCellValue(stock.gainLoss());
                row.createCell(7).setCellValue(stock.gainLossPercentage() + "%");
            }

            for(int i = 0; i < stockHeaders.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage(), e);
        }
    }


    public byte[] exportPortfolioSummaryToPdf(Long portfolioId) {
        PortfolioSummaryDTO summary = getPortfolioSummary(portfolioId);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Portfolio Summary for " + summary.portfolioName()));
            document.add(new Paragraph("As Of Date: " + summary.asOfDate()));
            document.add(new Paragraph("Total Invested Value: " + String.format("%.2f", summary.totalInvestedValue())));
            document.add(new Paragraph("Current Market Value: " + String.format("%.2f", summary.currentMarketValue())));
            document.add(new Paragraph("Total Gain/Loss: " + String.format("%.2f", summary.totalGainLoss())));
            document.add(new Paragraph("Total Gain/Loss Percentage: " + String.format("%.2f", summary.totalGainLossPercentage()) + "%"));
            document.add(new Paragraph("\n"));


            document.add(new Paragraph("Stock Holdings:"));
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] stockHeaders = {"Holding ID", "Symbol", "Name", "Quantity", "Buy Price", "Current Price", "Gain/Loss", "Gain/Loss %"};
            for (String header : stockHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                table.addCell(cell);
            }

            for (StockHoldingSummaryDTO stock : summary.stockHoldings()) {
                table.addCell(String.valueOf(stock.holdingId()));
                table.addCell(stock.stockSymbol());
                table.addCell(stock.stockName());
                table.addCell(String.format("%.2f", stock.quantity()));
                table.addCell(String.format("%.2f", stock.buyPrice()));
                table.addCell(String.format("%.2f", stock.currentPrice()));
                table.addCell(String.format("%.2f", stock.gainLoss()));
                table.addCell(String.format("%.2f", stock.gainLossPercentage()) + "%");
            }
            document.add(table);

            document.close();
            return outputStream.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failed to generate PDF report: " + e.getMessage(), e);
        }
    }
}