package com.apssouza.mytrade.feed.price;

public class MemoryPriceDao implements PriceDao {

    private PriceDao priceDao;

    public MemoryPriceDao(PriceDao priceDao) {
        this.priceDao = priceDao;
    }
}