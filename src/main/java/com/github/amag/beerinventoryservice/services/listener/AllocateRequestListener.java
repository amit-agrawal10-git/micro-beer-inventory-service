package com.github.amag.beerinventoryservice.services.listener;

import com.github.amag.beerinventoryservice.config.JMSConfig;
import com.github.amag.beerinventoryservice.services.AllocationService;
import com.github.amag.model.events.AllocationResult;
import com.github.amag.model.BeerOrderDto;
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
