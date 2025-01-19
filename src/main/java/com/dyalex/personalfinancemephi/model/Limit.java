package com.dyalex.personalfinancemephi.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "limits")
public class Limit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    private BigDecimal value;

    @Transient
    private Boolean inBound;

    @Transient
    private BigDecimal balance;
}
