package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelExportService {

    public ByteArrayOutputStream generatePortfolioExcel(Portfolio portfolio) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Portfolio Summary");

            Row headerRow1 = sheet.createRow(0);
            headerRow1.createCell(0).setCellValue("Portfolio Name:");
            headerRow1.createCell(1).setCellValue(portfolio.getPortfolioName());

            Row headerRow2 = sheet.createRow(1);
            headerRow2.createCell(0).setCellValue("User:");

            headerRow2.createCell(1).setCellValue(portfolio.getUser() != null ? portfolio.getUser().getFullName() : "N/A");

            Row headerRow3 = sheet.createRow(2);
            headerRow3.createCell(0).setCellValue("Date:");
            headerRow3.createCell(1).setCellValue(LocalDate.now().format(DateTimeFormatter.ISO_DATE));


            int rowNum = 5;
            Row stockHeaderRow = sheet.createRow(rowNum++);
            String[] stockHeaders = {"Symbol", "Name", "Quantity", "Buy Price", "Current Price", "Gain/Loss"};
            for (int i = 0; i < stockHeaders.length; i++) {
                stockHeaderRow.createCell(i).setCellValue(stockHeaders[i]);
            }

            if (portfolio.getStockHoldings() != null) {
                for (StockHolding holding : portfolio.getStockHoldings()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(holding.getStockSymbol());
                    row.createCell(1).setCellValue(holding.getStockName());
                    row.createCell(2).setCellValue(holding.getQuantity());
                    row.createCell(3).setCellValue(holding.getBuyPrice());

                    Cell currentPriceCell = row.createCell(4);
                    if (holding.getCurrentPrice() != null) {
                        currentPriceCell.setCellValue(holding.getCurrentPrice());
                    } else {
                        currentPriceCell.setCellValue(0.0);
                    }


                    Cell gainLossCell = row.createCell(5);
                    if (holding.getGainLoss() != null) {
                        gainLossCell.setCellValue(holding.getGainLoss());
                    } else {
                        gainLossCell.setCellValue(0.0);
                    }
                }
            } else {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("No stock holdings found for this portfolio.");
            }

            for(int i = 0; i < stockHeaders.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream;

        } catch (IOException e) {

            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage(), e);
        }
    }
}