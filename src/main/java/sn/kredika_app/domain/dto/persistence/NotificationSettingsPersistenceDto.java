package sn.kredika_app.domain.dto.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationSettingsPersistenceDto {
    private boolean emailEnabled;
    private boolean pushEnabled;
    private List<String> allowedChannels;
}
