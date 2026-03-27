package com.stakingpool.main;

import com.stakingpool.core.BlockchainProvider;
import com.stakingpool.auth.AccountManager;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import java.math.BigInteger;

/**
 * Main application entry point for the Ethereum Staking Client.
 * Demonstrates basic blockchain interaction and balance checking.
 */
public class StakingApp {
    public static void main(String[] args) {
        System.out.println("Staking Pool Client Starting...");
        System.out.println("Connecting to Anvil local node at http://127.0.0.1:8545 ...");

        // Use the blockchain provider
        Web3j web3j = BlockchainProvider.getWeb3j();

        try {
            // Get the latest block number
            EthBlockNumber blockNumberResponse = web3j.ethBlockNumber().send();
            BigInteger latestBlockNumber = blockNumberResponse.getBlockNumber();
            System.out.println("Latest block number: " + latestBlockNumber);

            // Example: Get balance of owner account
            String ownerAddress = AccountManager.owner().getAddress();
            System.out.println("Owner address: " + ownerAddress);

            // Use the fetched block number
            System.out.println("Owner balance: " + web3j
                    .ethGetBalance(ownerAddress, DefaultBlockParameter.valueOf(latestBlockNumber))
                    .send().getBalance());

        } catch (Exception e) {
            System.err.println("Error connecting to the blockchain: " + e.getMessage());
            System.err.println("Make sure your local Anvil node is running on port 8545.");
        }

        System.out.println("Staking Pool Client Finished.");
    }
}