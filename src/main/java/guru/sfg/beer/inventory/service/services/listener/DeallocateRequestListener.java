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
public class DeallocateRequestListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.DEALLOCATE_ORDER_REQUEST_QUEUE)
    public void listen(BeerOrderDto beerOrderDto){
        ActionResult.ActionResultBuilder actionResultBuilder = ActionResult.builder();
        actionResultBuilder.id(beerOrderDto.getId());
        Boolean result = true;
        try{
            allocationService.deallocateOrder(beerOrderDto);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            result = false;
        }

        log.debug("Order Id: "+beerOrderDto.getId());
        jmsTemplate.convertAndSend(JMSConfig.DEALLOCATE_ORDER_RESPONSE_QUEUE, actionResultBuilder.isSuccessful(result).build());
    }
}
