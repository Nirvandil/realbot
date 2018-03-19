package cf.nirvandil.realbot.model.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomerData extends ResultAware {
    private Double balance;
}
