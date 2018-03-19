package cf.nirvandil.realbot.model.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FindIdResponse extends ResultAware {
    @JsonProperty("Id")
    private Integer id;
}
