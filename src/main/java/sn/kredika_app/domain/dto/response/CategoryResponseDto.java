package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.kredika_app.domain.dto.simple.CategorySimpleDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private String slug;
    private String imageUrl;
    private CategorySimpleDto parent;
    private List<CategorySimpleDto> children;
    private Integer productCount;
}
