package sn.kredika_app.domain.dto.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataPersistenceDto {
    private String color;       // Ex: "#FF0000"
    private String icon;        // Ex: "check-circle"
    private String theme;       // Ex: "dark", "light"
    private Integer priority;   // Ex: 1, 2, 3
}
