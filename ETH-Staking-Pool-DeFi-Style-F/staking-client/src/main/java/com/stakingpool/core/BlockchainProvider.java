package java.com.stakingpool.core;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

public class BlockchainProvider {

    public static Web3j getWeb3j() {
        return Web3j.build(new HttpService("http://127.0.0.1:8545"));
    }

    public static DefaultGasProvider getGasProvider() {
        return new DefaultGasProvider();
    }
}