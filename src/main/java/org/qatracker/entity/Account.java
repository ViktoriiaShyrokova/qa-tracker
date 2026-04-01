package org.qatracker.entity;

import org.qatracker.enums.AccountStatus;

import java.math.BigDecimal;

public class Account {

    private Long id;
    private AccountStatus status;
    private int zone;
    private BigDecimal balance;
    private static Long counter= 1L;

    public Account() {
        this.status = AccountStatus.ACTIVE;
        this.zone = 1;
        this.balance = BigDecimal.ZERO;
        this.id = counter++;
    }

    public Account(Long id, AccountStatus status, int zone, BigDecimal balance) {
        this.id = id;
        this.status = status;
        this.zone = zone;
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public int getZone() {
        return zone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getId() {
        return id;
    }
}
