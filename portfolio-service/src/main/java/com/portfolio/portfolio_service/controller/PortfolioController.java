package com.portfolio.portfolio_service.controller;

import com.portfolio.portfolio_service.dto.CreatePortfolioRequestDto;
import com.portfolio.portfolio_service.dto.PortfolioSummaryDto;
import com.portfolio.portfolio_service.entity.Portfolio;
import com.portfolio.portfolio_service.service.PortfolioService;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")

@Tag(
        name = "Portfolio APIs",
        description = "Manage portfolios and portfolio summaries"
)

public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(
            PortfolioService portfolioService
    ) {

        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(
            @Valid
            @RequestBody
            CreatePortfolioRequestDto requestDto
    ) {

        return ResponseEntity.ok(
                portfolioService.createPortfolio(
                        requestDto
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {

        return ResponseEntity.ok(
                portfolioService.getAllPortfolios(1L)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                portfolioService.getPortfolioById(id)
        );
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<PortfolioSummaryDto>
    getPortfolioSummary(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                portfolioService.getPortfolioSummary(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(
            @PathVariable Long id,

            @Valid
            @RequestBody
            CreatePortfolioRequestDto requestDto
    ) {

        return ResponseEntity.ok(
                portfolioService.updatePortfolio(
                        id,
                        requestDto
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(
            @PathVariable Long id
    ) {

        portfolioService.deletePortfolio(id);

        return ResponseEntity.ok(
                "Portfolio deleted successfully"
        );
    }
}