package com.portfolio.portfolio_service.controller;

import com.portfolio.portfolio_service.dto.HoldingRequestDto;
import com.portfolio.portfolio_service.dto.HoldingResponseDto;
import com.portfolio.portfolio_service.entity.Holding;
import com.portfolio.portfolio_service.service.HoldingService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(
            HoldingService holdingService
    ) {

        this.holdingService = holdingService;
    }

    @PostMapping
    public ResponseEntity<Holding> addHolding(
            @Valid
            @RequestBody
            HoldingRequestDto requestDto
    ) {

        return ResponseEntity.ok(
                holdingService.addHolding(
                        requestDto
                )
        );
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Holding>> getHoldingsByPortfolio(
            @PathVariable Long portfolioId
    ) {

        return ResponseEntity.ok(
                holdingService.getHoldingsByPortfolio(
                        portfolioId
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Holding> getHoldingById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                holdingService.getHoldingById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Holding> updateHolding(
            @PathVariable Long id,

            @Valid
            @RequestBody
            HoldingRequestDto requestDto
    ) {

        return ResponseEntity.ok(
                holdingService.updateHolding(
                        id,
                        requestDto
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHolding(
            @PathVariable Long id
    ) {

        holdingService.deleteHolding(id);

        return ResponseEntity.ok(
                "Holding deleted successfully"
        );
    }

    @GetMapping("/{id}/gainloss")
    public ResponseEntity<HoldingResponseDto>
    getHoldingGainLoss(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                holdingService.getHoldingWithGainLoss(id)
        );
    }

    @GetMapping("/portfolio/{portfolioId}/gainloss")
    public ResponseEntity<List<HoldingResponseDto>>
    getPortfolioGainLoss(
            @PathVariable Long portfolioId
    ) {

        return ResponseEntity.ok(
                holdingService.getHoldingsWithGainLoss(
                        portfolioId
                )
        );
    }
}