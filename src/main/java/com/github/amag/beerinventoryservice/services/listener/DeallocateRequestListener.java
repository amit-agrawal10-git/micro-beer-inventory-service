package com.github.amag.beerinventoryservice.services.listener;

import com.github.amag.beerinventoryservice.config.JMSConfig;
import com.github.amag.beerinventoryservice.services.AllocationService;
import com.github.amag.model.ActionResult;
import com.github.amag.model.BeerOrderDto;
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
