package guru.sfg.beer.inventory.service.services.listener;

import guru.sfg.beer.inventory.service.config.JMSConfig;
import guru.sfg.beer.inventory.service.services.AllocationService;
import guru.sfg.brewery.model.ActionResult;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.events.AllocationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AllocateRequestListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_REQUEST_QUEUE)
    public void listen(BeerOrderDto beerOrderDto){
        AllocationResult.AllocationResultBuilder allocationResultBuilder = AllocationResult.builder();
        allocationResultBuilder.beerOrderDto(beerOrderDto);
        Boolean errorOccurred = false;
        try{
            Boolean allocationResult = allocationService.allocateOrder(beerOrderDto);
                allocationResultBuilder.partialAllocation(allocationResult);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            errorOccurred = true;
        } finally {
            allocationResultBuilder.errorOccurred(errorOccurred);
        }

        log.debug("Order Id: "+beerOrderDto.getId());
        jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, allocationResultBuilder.build());
    }
}
