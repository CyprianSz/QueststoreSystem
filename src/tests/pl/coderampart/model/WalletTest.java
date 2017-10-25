package pl.coderampart.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setup(){
        this.wallet = new Wallet("id", 100, 100);
    }

    @Test
    void test1WalletConstructor(){
        Wallet wallet = new Wallet();
        assertNotNull(wallet);
    }

    @Test
    void test2WalletConstructor(){
        assertNotNull(wallet);
    }

    @Test
    void testWalletGetId(){
        assertEquals("id", wallet.getID());
    }

    @Test
    void testWalletGetBallance(){
        assertEquals(Integer.valueOf(100), wallet.getBalance());
    }

    @Test
    void testWalletGetEarned(){
        assertEquals(Integer.valueOf(100), wallet.getEarnedCoins());
    }

    @Test
    void testWalletSetBalance(){
        wallet.setBalance(110);
        assertEquals(Integer.valueOf(110), wallet.getBalance());
    }

    @Test
    void testWalletSetEarnedCoins(){
        wallet.setEarnedCoins(110);
        assertEquals(Integer.valueOf(110), wallet.getEarnedCoins());
    }

}