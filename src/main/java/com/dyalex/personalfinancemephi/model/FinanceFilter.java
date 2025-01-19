package com.dyalex.personalfinancemephi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceFilter {
    private List<Wallet> wallets;
    private List<Category> categories;
}
