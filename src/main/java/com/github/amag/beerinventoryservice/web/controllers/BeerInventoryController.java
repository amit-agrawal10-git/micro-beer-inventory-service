package com.github.amag.beerinventoryservice.web.controllers;

import com.github.amag.beerinventoryservice.repositories.BeerInventoryRepository;
import com.github.amag.beerinventoryservice.web.mappers.BeerInventoryMapper;
import com.github.amag.model.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jt on 2019-05-31.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerInventoryController {

    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerInventoryMapper beerInventoryMapper;

    @GetMapping("api/v1/beer/{beerUpc}/inventory")
    List<BeerInventoryDto> listBeersByUpc(@PathVariable String beerUpc){
        log.debug("Finding Inventory for beerUpc:" + beerUpc);

        return beerInventoryRepository.findAllByUpc(beerUpc)
                .stream()
                .map(beerInventoryMapper::beerInventoryToBeerInventoryDto)
                .collect(Collectors.toList());
    }
}
