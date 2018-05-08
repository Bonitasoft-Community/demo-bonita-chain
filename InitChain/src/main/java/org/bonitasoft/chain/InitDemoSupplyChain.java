
package org.bonitasoft.chain;

import java.util.HashMap;

import com.chain.api.MockHsm;
import com.chain.http.Client;
import com.chain.signing.HsmSigner;

public class InitDemoSupplyChain {

    public static void main(String[] args) throws Exception {
        
        ChainUtils utils = ChainUtils.getInstance();
        Client client = new Client();

        // Create Keys
        MockHsm.Key janRipleyKey = utils.createKey(client, "janRipleyKey");
        MockHsm.Key johnDoeKey = utils.createKey(client, "johnDoeKey");
        MockHsm.Key bonitaStoreKey = utils.createKey(client, "bonitaStoreKey");
        MockHsm.Key bonitaProductionKey = utils.createKey(client, "bonitaProductionKey");

        MockHsm.Key bonitaWhiteCarKey = utils.createKey(client, "bonitaWhiteCarKey");
        MockHsm.Key bonitaYellowCarKey = utils.createKey(client, "bonitaYellowCarKey");

        MockHsm.Key bonitaCoinKey = utils.createKey(client, "bonitaCoinKey");

        HsmSigner.addKey(janRipleyKey, MockHsm.getSignerClient(client));
         HsmSigner.addKey(johnDoeKey, MockHsm.getSignerClient(client));
         HsmSigner.addKey(bonitaStoreKey, MockHsm.getSignerClient(client));
         HsmSigner.addKey(bonitaProductionKey, MockHsm.getSignerClient(client));
         HsmSigner.addKey(bonitaWhiteCarKey, MockHsm.getSignerClient(client));
         HsmSigner.addKey(bonitaYellowCarKey, MockHsm.getSignerClient(client));
         HsmSigner.addKey(bonitaCoinKey, MockHsm.getSignerClient(client));

        // Create accounts
        utils.createAccount(client, "johnDoe", johnDoeKey, new HashMap<String, String>() {
            {
                put("first_name", "John");
                put("last_name", "Doe");
                put("type", "client");
                put("address", "3400 Brown Bear Drive, Riverside, 92503 CA");
                put("phone_number", "248-344-9629");
                put("mail", "john.doe@mail.com");
            }
        });
        utils.createAccount(client, "janRipley", janRipleyKey, new HashMap<String, String>() {
            {
                put("first_name", "Jan");
                put("last_name", "Ripley");
                put("type", "client");
                put("address", "3584 Frank Avenue, Portland, 97205 PA");
                put("phone_number", "740-329-3454");
                put("mail", "jan.ripley@mail.com");
            }
        });

        utils.createAccount(client, "bonitaStore", bonitaStoreKey, new HashMap<String, String>() {
            {
                put("brand", "Bonita");
                put("type", "vendor");
                put("address", "4166 Half and Half Drive, Fresno, 93721 CA");
            }
        });
        utils.createAccount(client, "bonitaProduction", bonitaProductionKey,
                new HashMap<String, String>() {
                    {
                        put("name", "Bonita Car Production");
                        put("type", "production");
                        put("address", "113 Drainer Avenue, Fort Walton Beach, 32548 FL");
                    }
                });

        // Create assets
        utils.createAsset(client, "bonitaCoin", bonitaCoinKey, null);
        utils.createAsset(client, "bonitaWhiteCar", bonitaWhiteCarKey,
                new HashMap<String, String>() {
                    {
                        put("brand", "Bonita");
                        put("color", "White");
                        put("price", "50000");
                    }
                });
        utils.createAsset(client, "bonitaYellowCar", bonitaYellowCarKey,
                new HashMap<String, String>() {
                    {
                        put("brand", "Bonita");
                        put("color", "Yellow");
                        put("price", "55000");
                    }
                });

        // Issue asset
        utils.issueNewAsset(client, "bonitaStore", "bonitaYellowCar", 2);
      //  utils.issueNewAsset(client, "bonitaStore", "bonitaWhiteCar", 0);

        utils.issueNewAsset(client, "johnDoe", "bonitaCoin", 60000);
        utils.issueNewAsset(client, "janRipley", "bonitaCoin", 40000);

        
        System.out.println("Chain init with success");
        
       
        System.exit(0);
        // utils.issueNewAsset(client, "bonitaProduction", "car", 10);

    }

}
