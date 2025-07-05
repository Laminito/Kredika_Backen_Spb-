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
public class UserPreferencesPersistenceDto {
    private String language;   // "fr", "en", etc.
    private boolean darkMode;
    private String timezone;   // "Europe/Paris"
    private NotificationSettingsPersistenceDto notifications;
}
