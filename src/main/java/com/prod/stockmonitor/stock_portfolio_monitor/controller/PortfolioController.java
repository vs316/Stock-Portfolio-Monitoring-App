package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.service.ExcelExportService; // Import ExcelExportService
import com.prod.stockmonitor.stock_portfolio_monitor.service.PdfExportService;   // Import PdfExportService
import com.prod.stockmonitor.stock_portfolio_monitor.service.PortfolioService;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.PortfolioRepository; // Import PortfolioRepository
import org.springframework.beans.factory.annotation.Autowired; // For @Autowired
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.itextpdf.text.DocumentException; // For PDF error handling

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    // Autowire repositories and services needed for export
    @Autowired
    private PortfolioRepository portfolioRepository; // Needed to fetch portfolio by ID
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private PdfExportService pdfExportService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/createPortfolio")
    public ResponseEntity<String> createPortfolio(@RequestBody PortfolioRequest request) {
        String message = portfolioService.savePortfolio(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long portfolioId) {
        return portfolioService.getPortfolioById(portfolioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.noContent().build();
    }

    // --- NEW EXPORT ENDPOINTS ---

    @GetMapping("/{portfolioId}/export/excel")
    public ResponseEntity<byte[]> exportPortfolioToExcel(@PathVariable Long portfolioId) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);

        if (portfolioOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Portfolio portfolio = portfolioOptional.get();

        try {
            ByteArrayOutputStream excelStream = excelExportService.generatePortfolioExcel(portfolio);
            byte[] excelBytes = excelStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Or MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            String filename = "portfolio_" + portfolioId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{portfolioId}/export/pdf")
    public ResponseEntity<byte[]> exportPortfolioToPdf(@PathVariable Long portfolioId) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(portfolioId);

        if (portfolioOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Portfolio portfolio = portfolioOptional.get();

        try {
            ByteArrayOutputStream pdfStream = pdfExportService.generatePortfolioPdf(portfolio);
            byte[] pdfBytes = pdfStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "portfolio_" + portfolioId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DocumentException | IOException e) {
            e.printStackTrace(); // Log the exception for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}