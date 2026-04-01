package org.qatracker.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.qatracker.entity.Account;
import org.qatracker.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTest {

    @Test
    public void shouldCreateAccountWithDefaultValues(){
        Account account = new Account();

        assertNotNull(account,"Account should not be null");
        assertEquals(AccountStatus.ACTIVE, account.getStatus(), "Default status should be active");
        assertEquals(1, account.getZone(),"Default zone should be 1");
        assertEquals(BigDecimal.ZERO, account.getBalance(),"Default balance should be 0.00");
    }
    static Stream<Arguments> accountArgumentsProvider(){
    return Stream.of(
            Arguments.of(101L,AccountStatus.ACTIVE,2,BigDecimal.valueOf(100.0))
    );
    }

    @ParameterizedTest
    @MethodSource("accountArgumentsProvider")
    public void shouldCreateCustomAccount(Long id, AccountStatus status,int zone,BigDecimal balance){
        Account account = new Account(id,status,zone,balance);
        assertEquals(id,account.getId(),"id should be " + id+ ", got " + account.getId());
        assertEquals(status,account.getStatus(),"status should be " + status+ ", got " + account.getStatus());
        assertEquals(zone,account.getZone(),"zone should be " + zone+ ", got " + account.getZone());
        assertEquals(balance,account.getBalance(),"balance should be " + balance+ ", got " + account.getBalance());

    }
}
