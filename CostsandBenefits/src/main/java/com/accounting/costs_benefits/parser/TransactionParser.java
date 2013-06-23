package com.accounting.costs_benefits.parser;

/**
 * Created by carlos on 23/06/13.
 */
public interface TransactionParser {

    public void parse(String textToParse);

    public String getTitle();
    public Long getTimestamp();
    public String getDescription();
    public Double getQuantity();
}
