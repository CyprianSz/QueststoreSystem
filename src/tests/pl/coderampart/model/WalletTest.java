package pl.coderampart.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void test1WalletConstructor(){
        Wallet wallet = new Wallet();
        assertNotNull(wallet);
    }

    @Test
    void test2WalletConstructor(){
        Wallet wallet = new Wallet("id", 5, 5);
        assertNotNull(wallet);
    }

}