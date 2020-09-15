package guru.sfg.beer.inventory.service.services;

import guru.sfg.beer.inventory.service.config.JMSConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrewingServiceListner {

    private final BeerInventoryRepository beerInventoryRepository;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.NEW_INVENTORY_QUEUE)
    @Transactional
    public void listen(BeerDto beerDto){

        BeerInventory beerInventory = BeerInventory.builder()
                .beerId(beerDto.getId())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .upc(beerDto.getUpc())
                .build();
        beerInventoryRepository.save(beerInventory);

        log.debug("Beer Inventory saved: "+beerInventory);
    }
}
