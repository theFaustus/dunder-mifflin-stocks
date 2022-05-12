package inc.dundermifflin.stocks.organizationservice.web;

import inc.dundermifflin.stocks.organizationservice.service.OrganizationService;
import inc.dundermifflin.stocks.organizationservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.organizationservice.web.error.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable("organizationId") String organizationId) {
        OrganizationDto organization = organizationService.findById(organizationId);
        organization.add(linkTo(methodOn(OrganizationController.class)
                        .getOrganization(organizationId))
                        .withSelfRel(),
                linkTo(methodOn(OrganizationController.class)
                        .createOrganization(organization, null))
                        .withRel("createOrganization"),
                linkTo(methodOn(OrganizationController.class)
                        .updateOrganization(organizationId, organization, null))
                        .withRel("updateOrganization"),
                linkTo(methodOn(OrganizationController.class)
                        .deleteOrganization(organizationId, null))
                        .withRel("deleteOrganization"));
        return ResponseEntity.ok(organization);
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<SuccessResponse> updateOrganization(@PathVariable("organizationId") String organizationId, @RequestBody OrganizationDto request,
                                                         @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(organizationService.updateOrganization(request, organizationId, locale));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createOrganization(@RequestBody OrganizationDto request, @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(organizationService.createOrganization(request, locale));
    }

    @DeleteMapping(value = "/{organizationId}")
    public ResponseEntity<SuccessResponse> deleteOrganization(@PathVariable("organizationId") String organizationId,
                                                         @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(organizationService.deleteOrganization(organizationId, locale));
    }
}
