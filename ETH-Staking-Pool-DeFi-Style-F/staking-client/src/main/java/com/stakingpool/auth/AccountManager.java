package com.stakingpool.auth;

import org.web3j.crypto.Credentials;

/**
 * Manages test accounts and their private keys for interacting with the local Anvil node.
 * Warning: These are hardcoded Anvil test keys. Do not use in production.
 */
public class AccountManager {

    // Anvil private keys
    public static Credentials owner() {
        return Credentials.create("0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80");
    }

    public static Credentials alice() {
        return Credentials.create("0x59c6995e998f97a5a0044966f0945389dc9e86dae88c7a8412f4603b6b78690d");
    }

    public static Credentials bob() {
        return Credentials.create("0x5de4111afa1a4b94908f83103eb1f1706367c2e68ca870fc3fb9a804cdab365a");
    }
}