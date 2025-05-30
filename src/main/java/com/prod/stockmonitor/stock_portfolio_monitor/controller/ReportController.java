package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioSummaryDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/portfolio-summary/{portfolioId}")
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary(@PathVariable Long portfolioId) {
        try {
            PortfolioSummaryDTO summary = reportService.getPortfolioSummary(portfolioId);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/export/{portfolioId}")
    public ResponseEntity<byte[]> exportPortfolioSummary(
            @PathVariable Long portfolioId,
            @RequestParam String type) {
        try {
            byte[] fileBytes;
            String fileName;
            String contentType;

            if ("excel".equalsIgnoreCase(type)) {
                fileBytes = reportService.exportPortfolioSummaryToExcel(portfolioId);
                fileName = "portfolio_summary_" + portfolioId + ".xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if ("pdf".equalsIgnoreCase(type)) {
                fileBytes = reportService.exportPortfolioSummaryToPdf(portfolioId);
                fileName = "portfolio_summary_" + portfolioId + ".pdf";
                contentType = "application/pdf";
            } else {
                return ResponseEntity.badRequest().body("Invalid export type. Must be 'excel' or 'pdf'.".getBytes());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.parseMediaType(contentType));

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage().getBytes());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes());
        }
    }
}