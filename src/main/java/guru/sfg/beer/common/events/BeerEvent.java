package guru.sfg.beer.common.events;

import guru.sfg.beer.common.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BeerEvent implements Serializable  {
    static final long serialVersionUID = -895501866408672271L;
    public BeerDto beerDto;
}
