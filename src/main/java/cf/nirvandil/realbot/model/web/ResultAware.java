package cf.nirvandil.realbot.model.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
abstract class ResultAware {
    @JsonProperty("Result")
    private String result;
    @JsonProperty("ErrorText")
    private String errorText;

    @JsonIgnore
    public boolean isSuccess() {
        return result.equalsIgnoreCase("ok") && errorText == null;
    }
}
