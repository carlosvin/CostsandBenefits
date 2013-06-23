package com.accounting.costs_benefits.provider;

import com.accounting.costs_benefits.domain.Transaction;

import java.util.Set;

/**
 * Created by carlos on 9/06/13.
 */
public interface TransactionListener {

    public void update(Transaction transaction);
    public void update(Set<Transaction> transactionsTmp);

}
