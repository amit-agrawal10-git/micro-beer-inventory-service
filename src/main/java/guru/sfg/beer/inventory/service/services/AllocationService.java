package guru.sfg.beer.inventory.service.services;

import guru.sfg.brewery.model.BeerOrderDto;

public interface AllocationService {
    public Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
