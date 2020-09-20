package com.github.amag.beerinventoryservice.services;

import com.github.amag.beerinventoryservice.config.JMSConfig;
import com.github.amag.beerinventoryservice.domain.BeerInventory;
import com.github.amag.beerinventoryservice.repositories.BeerInventoryRepository;
import com.github.amag.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrewingServiceListener {

    private final BeerInventoryRepository beerInventoryRepository;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.NEW_INVENTORY_QUEUE)
    @Transactional
    public void listen(BeerDto beerDto){

        BeerInventory beerInventory = BeerInventory.builder()
                .quantityOnHand(beerDto.getQuantityOnHand())
                .upc(beerDto.getUpc())
                .build();
        beerInventoryRepository.save(beerInventory);

        log.debug("Beer Inventory saved: "+beerInventory);
    }
}
