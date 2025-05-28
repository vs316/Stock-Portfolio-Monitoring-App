// ReportService.java (Corrected Code with Record Accessors)
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
import java.util.Optional;

// Apache POI Imports
import org.apache.poi.ss.usermodel.Cell;
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
    // Assuming you'll have a service for fetching real-time prices (Module 3)
    // private final StockPriceFetcherService stockPriceFetcherService;

    public ReportService(PortfolioRepository portfolioRepository,
                         StockHoldingRepository stockHoldingRepository,
                         UserRepository userRepository
            /*, StockPriceFetcherService stockPriceFetcherService*/) {
        this.portfolioRepository = portfolioRepository;
        this.stockHoldingRepository = stockHoldingRepository;
        this.userRepository = userRepository;
        // this.stockPriceFetcherService = stockPriceFetcherService;
    }

    /**
     * Generates a summary report for a given portfolio.
     * For now, this will use the existing currentPrice in StockHolding,
     * or assume currentPrice is available. In a full implementation,
     * it would fetch real-time prices.
     *
     * @param portfolioId The ID of the portfolio to summarize.
     * @return PortfolioSummaryDTO containing the summary.
     * @throws RuntimeException if the portfolio or user is not found.
     */
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
            // For now, using existing currentPrice or assuming it's set.
            // In a real scenario, you'd integrate with StockPriceFetcherService here.
            Double currentPrice = holding.getCurrentPrice() != null ? holding.getCurrentPrice() : holding.getBuyPrice(); // Fallback
            if (currentPrice == null) {
                // If currentPrice is null, it means it hasn't been fetched yet.
                // For reporting, you might want to fetch it or skip this holding, or use buyPrice.
                // For this example, we'll use buyPrice if currentPrice is null.
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
                user.getFullName(), // Assuming fullName is preferred for display
                totalInvestedValue,
                currentMarketValue,
                totalGainLoss,
                totalGainLossPercentage,
                LocalDate.now(), // As of current date
                stockSummaries
        );
    }

    /**
     * Exports the portfolio summary to an Excel file.
     *
     * @param portfolioId The ID of the portfolio to export.
     * @return A byte array representing the Excel file.
     * @throws RuntimeException if portfolio not found or IOException during file generation.
     */
    public byte[] exportPortfolioSummaryToExcel(Long portfolioId) {
        PortfolioSummaryDTO summary = getPortfolioSummary(portfolioId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Portfolio Summary");

            // Header Row for Portfolio Summary
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


            // Stock Holdings Header
            int rowNum = 8; // Start stock holdings a few rows after summary
            Row stockHeaderRow = sheet.createRow(rowNum++);
            String[] stockHeaders = {"Holding ID", "Symbol", "Name", "Quantity", "Buy Price", "Current Price", "Gain/Loss", "Gain/Loss %"};
            for (int i = 0; i < stockHeaders.length; i++) {
                stockHeaderRow.createCell(i).setCellValue(stockHeaders[i]);
            }

            // Stock Holdings Data
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

            // Auto-size columns for better readability
            for(int i = 0; i < stockHeaders.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage(), e);
        }
    }

    /**
     * Exports the portfolio summary to a PDF file.
     *
     * @param portfolioId The ID of the portfolio to export.
     * @return A byte array representing the PDF file.
     * @throws RuntimeException if portfolio not found or DocumentException/IOException during file generation.
     */
    public byte[] exportPortfolioSummaryToPdf(Long portfolioId) {
        PortfolioSummaryDTO summary = getPortfolioSummary(portfolioId);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add Portfolio Summary
            document.add(new Paragraph("Portfolio Summary for " + summary.portfolioName()));
            document.add(new Paragraph("As Of Date: " + summary.asOfDate()));
            document.add(new Paragraph("Total Invested Value: " + String.format("%.2f", summary.totalInvestedValue())));
            document.add(new Paragraph("Current Market Value: " + String.format("%.2f", summary.currentMarketValue())));
            document.add(new Paragraph("Total Gain/Loss: " + String.format("%.2f", summary.totalGainLoss())));
            document.add(new Paragraph("Total Gain/Loss Percentage: " + String.format("%.2f", summary.totalGainLossPercentage()) + "%"));
            document.add(new Paragraph("\n")); // Add a line break


            // Add Stock Holdings Table
            document.add(new Paragraph("Stock Holdings:"));
            PdfPTable table = new PdfPTable(8); // 8 columns as per StockHoldingSummaryDTO
            table.setWidthPercentage(100); // Table will fill 100% of page width
            table.setSpacingBefore(10f); // Space before table
            table.setSpacingAfter(10f); // Space after table

            // Add table headers
            String[] stockHeaders = {"Holding ID", "Symbol", "Name", "Quantity", "Buy Price", "Current Price", "Gain/Loss", "Gain/Loss %"};
            for (String header : stockHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                table.addCell(cell);
            }

            // Add stock holding data
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