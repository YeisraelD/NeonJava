package com.stakingpool.main;

import com.stakingpool.core.BlockchainProvider;
import com.stakingpool.auth.AccountManager;
import org.web3j.protocol.Web3j;

public class StakingApp {
    public static void main(String[] args) {
        System.out.println("Staking Pool Client Starting...");

        // Use the blockchain provider
        Web3j web3j = BlockchainProvider.getWeb3j();

        try {
            // Get the latest block number
            System.out.println("Latest block number: " + web3j.ethBlockNumber().send().getBlockNumber());

            // Example: Get balance of owner account
            String ownerAddress = AccountManager.owner().getAddress();
            System.out.println("Owner address: " + ownerAddress);
            System.out.println("Owner balance: " + web3j.ethGetBalance(ownerAddress, web3j.ethBlockNumber().send().getBlockNumber()).send().getBalance());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Staking Pool Client Finished.");
    }
}