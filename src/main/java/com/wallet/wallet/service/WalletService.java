package com.wallet.wallet.service;

import com.wallet.wallet.model.Wallet;
import com.wallet.wallet.model.repo.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Pattern;

@Service
public class WalletService {
    private final String BITCOIN_ADDRESS_REGEX = "^(1|3|bc)[a-zA-Z0-9]{25,34}$";
    private final Pattern BITCOIN_ADDRESS_PATTERN = Pattern.compile(BITCOIN_ADDRESS_REGEX);
    private final String API_URL = "http://api.coinlayer.com/live?access_key=44828e0798ae4579226045c37093d39d";

    WalletRepository walletRepo;

    public WalletService(WalletRepository repo) {
        this.walletRepo = repo;
    }

    public Wallet createNewWallet(){
        Wallet wallet = new Wallet();
        wallet.setAddress(generateValidBitcoinAddress());
        wallet.setBitcoinValue(getBitcoinValue());
        wallet.setBitcoins(4.23);
        wallet.setWalletValue(wallet.getBitcoinValue()*wallet.getBitcoins());
        return walletRepo.save(wallet);
    }

    public double getBitcoinValue() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        return rates.get("BTC");
    }


    private String generateValidBitcoinAddress() {
        String address;
        do {
            address = generateRandomBitcoinAddress();
        }
        while (!isValidBitcoinAddress(address));
        return address;
    }

    private String generateRandomBitcoinAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append("1");
        for (int i = 1; i < 34; i++) {
            char c = (char) ('a' + (int) (Math.random() * 26));
            sb.append(c);
        }
        return sb.toString();
    }

    private boolean isValidBitcoinAddress(String address) {
        return BITCOIN_ADDRESS_PATTERN.matcher(address).matches();
    }
}