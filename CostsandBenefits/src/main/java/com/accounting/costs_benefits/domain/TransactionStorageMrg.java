package com.accounting.costs_benefits.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by carlos on 23/06/13.
 */
public class TransactionStorageMrg {

    private final Map<Long, Set<Transaction>> transactions;
    private final Transaction aux;
    private final Set<Transaction> lastUpdated;

    public TransactionStorageMrg(){
        aux = new Transaction();
        transactions = new HashMap<Long, Set<Transaction>>();
        lastUpdated = new HashSet<Transaction>();
    }

    public void create(String title, Long date, Double qty) {
        aux.setTitle(title);
        aux.setTimestamp(date);
        aux.setQuantity(qty);

        Set<Transaction> trs = transactions.get(date);
        if (trs == null){
            trs = new HashSet<Transaction>();
            transactions.put(date, trs);
        }

        if (!trs.contains(aux)){
            Transaction trTmp = new Transaction(aux);
            if (trs.add(trTmp)){
                lastUpdated.add(trTmp);
            }
        }
    }

    public Set<Transaction> getLastUpdated(){
        return lastUpdated;
    }

    public void clearLastUpdated(){
        lastUpdated.clear();
    }

    public Map<Long, Set<Transaction>>  getAll(){
        return Collections.unmodifiableMap(transactions);
    }
}
