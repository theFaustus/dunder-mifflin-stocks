package inc.dundermifflin.stocks.licensingservice.web.dto;

import inc.dundermifflin.stocks.licensingservice.model.Organization;
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

    public static OrganizationDto from(Organization organization){
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setId(organization.getId());
        organizationDto.setName(organization.getName());
        organizationDto.setContactName(organization.getContactName());
        organizationDto.setContactEmail(organization.getContactEmail());
        organizationDto.setContactPhone(organization.getContactPhone());
        organizationDto.setCreatedAt(organization.getCreatedAt());
        organizationDto.setUpdatedAt(organization.getUpdatedAt());
        return organizationDto;
    }
}
