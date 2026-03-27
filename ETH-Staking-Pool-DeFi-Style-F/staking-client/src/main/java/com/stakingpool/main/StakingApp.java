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
            org.web3j.protocol.core.methods.response.EthBlockNumber blockNumberResponse = web3j.ethBlockNumber().send();
            java.math.BigInteger latestBlockNumber = blockNumberResponse.getBlockNumber();
            System.out.println("Latest block number: " + latestBlockNumber);

            // Example: Get balance of owner account
            String ownerAddress = AccountManager.owner().getAddress();
            System.out.println("Owner address: " + ownerAddress);

            // Use the fetched block number
            System.out.println("Owner balance: " + web3j
                    .ethGetBalance(ownerAddress,
                            org.web3j.protocol.core.DefaultBlockParameter.valueOf(latestBlockNumber))
                    .send().getBalance());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Staking Pool Client Finished.");
    }
}