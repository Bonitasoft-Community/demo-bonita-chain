
package org.bonitasoft.chain;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.chain.api.Account;
import com.chain.api.Asset;
import com.chain.api.Balance;
import com.chain.api.MockHsm;
import com.chain.api.Transaction;
import com.chain.api.Transaction.SubmitResponse;
import com.chain.exception.BuildException;
import com.chain.exception.BuildException.ActionError;
import com.chain.exception.ChainException;
import com.chain.http.Client;
import com.chain.signing.HsmSigner;

public class ChainUtils {

    private static ChainUtils instance;

    public static ChainUtils getInstance() {
        if (instance == null) {
            instance = new ChainUtils();
        }
        return instance;
    }

    private ChainUtils() {
    }

    public MockHsm.Key createKey(Client client, String alias) throws ChainException {
        return MockHsm.Key.create(client, alias);
    }

    public MockHsm.Key getKey(Client client, String alias) throws ChainException {
        MockHsm.Key.Items items =
                new MockHsm.Key.QueryBuilder().setAliases(Arrays.asList(alias)).execute(client);
        return items.next();
    }

    public Account createAccount(Client client, String alias, MockHsm.Key key,
            Map<String, String> tags) throws ChainException {
        Account.Builder accountBuilder =
                new Account.Builder().setAlias(alias).addRootXpub(key.xpub).setQuorum(1);
        if (tags != null) {
            for (Entry<String, String> e : tags.entrySet()) {
                accountBuilder.addTag(e.getKey(), e.getValue());
            }
        }
        return accountBuilder.create(client);
    }

    public Account getAccount(Client client, String alias) throws ChainException {
        Account.Items accounts = new Account.QueryBuilder().setFilter("alias=$1")
                .addFilterParameter(alias).execute(client);
        return accounts.next();
    }

    public Asset createAsset(Client client, String alias, MockHsm.Key key,
            Map<String, String> parameters) throws ChainException {
        Asset.Builder assetBuilder =
                new Asset.Builder().setAlias(alias).addRootXpub(key.xpub).setQuorum(1);
        if (parameters != null) {
            for (Entry<String, String> e : parameters.entrySet()) {
                assetBuilder.addDefinitionField(e.getKey(), e.getValue());
            }
        }
        return assetBuilder.create(client);
    }

    public Asset getAsset(Client client, String alias) throws ChainException {
        Asset.Items items = new Asset.QueryBuilder().setFilter("alias=$1").addFilterParameter(alias)
                .execute(client);
        return items.next();
    }

    public Transaction.Items listTransactions(Client client, String accountAlias)
            throws ChainException {
        return new Transaction.QueryBuilder()
                .setFilter("inputs(account_alias=$1) AND outputs(account_alias=$1)")
                .addFilterParameter(accountAlias).execute(client);

    }

    public Balance.Items getBalance(Client client, String assetAlias, String accountAlias)
            throws ChainException {
        Balance.Items items = new Balance.QueryBuilder()
                .setFilter("account_alias=$1 AND asset_alias=$2").addFilterParameter(accountAlias)
                .addFilterParameter(assetAlias).execute(client);
        return items;
    }

    public void issueNewAsset(Client client, String accountAlias, String assetAlias, int amount)
            throws ChainException {

        Transaction.Template issueTransaction = new Transaction.Builder()
                .addAction(
                        new Transaction.Action.Issue().setAssetAlias(assetAlias).setAmount(amount))
                .addAction(new Transaction.Action.ControlWithAccount().setAccountAlias(accountAlias)
                        .setAssetAlias(assetAlias).setAmount(amount))
                .build(client);

        Transaction.Template signedIssuance = HsmSigner.sign(issueTransaction);
        Transaction.submit(client, signedIssuance);
    }

    public TransactionResponse spendAsset(Client client, String accountFromAlias, String accountToAlias,
            String assetAlias, int amount) throws ChainException {
        Transaction.Template issueTransaction = null;
        try {
            issueTransaction = new Transaction.Builder()
                    .addAction(new Transaction.Action.SpendFromAccount()
                            .setAccountAlias(accountFromAlias).setAssetAlias(assetAlias)
                            .setAmount(amount))
                    .addAction(new Transaction.Action.ControlWithAccount()
                            .setAccountAlias(accountToAlias).setAssetAlias(assetAlias)
                            .setAmount(amount))
                    .build(client);
        } catch (ChainException ex) {
            if(ex instanceof BuildException) {
                BuildException buildEx = (BuildException) ex;
               return new TransactionResponse(buildEx.code , dataErrorsToString(buildEx));
            }
            else {
                throw ex;
            }
        }

        Transaction.Template signedIssuance = HsmSigner.sign(issueTransaction);

        SubmitResponse response = Transaction.submit(client, signedIssuance);
        return new TransactionResponse(response.id);
    }
    
    
      
    
    private String dataErrorsToString(BuildException buildEx) {
        
        if (buildEx.data == null || buildEx.data.actionErrors.size() == 0) {
            return buildEx.detail;
        }
        
        StringBuilder builder = new StringBuilder();
        for (ActionError error : buildEx.data.actionErrors) {
            builder.append(error.chainMessage);
        }
        return builder.toString();
            
    }

}
