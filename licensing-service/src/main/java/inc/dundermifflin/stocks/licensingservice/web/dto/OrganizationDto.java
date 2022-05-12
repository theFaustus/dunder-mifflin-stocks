package inc.dundermifflin.stocks.licensingservice.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationDto extends RepresentationModel<OrganizationDto> {
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
