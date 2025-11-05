package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Addition {
    @JsonProperty("additional_info")
    private String additionalInfo;

    @JsonProperty("additional_number")
    private Integer additionalNumber;

    @JsonProperty("id")
    private Integer id;
}