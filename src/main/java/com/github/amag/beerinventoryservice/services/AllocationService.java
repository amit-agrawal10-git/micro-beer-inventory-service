package com.github.amag.beerinventoryservice.services;

import com.github.amag.model.BeerOrderDto;

public interface AllocationService {
    public Boolean allocateOrder(BeerOrderDto beerOrderDto);
    public void deallocateOrder(BeerOrderDto beerOrderDto);
}
