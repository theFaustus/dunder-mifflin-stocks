package inc.dundermifflin.stocks.organizationservice.web;

import inc.dundermifflin.stocks.organizationservice.service.OrganizationService;
import inc.dundermifflin.stocks.organizationservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.organizationservice.web.error.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Locale;

import static inc.dundermifflin.stocks.organizationservice.model.Role.ADMIN;
import static inc.dundermifflin.stocks.organizationservice.model.Role.USER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @RolesAllowed({ADMIN, USER})
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

    @RolesAllowed({ADMIN})
    @PutMapping("/{organizationId}")
    public ResponseEntity<SuccessResponse> updateOrganization(@PathVariable("organizationId") String organizationId, @RequestBody OrganizationDto request,
                                                              @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(organizationService.updateOrganization(request, organizationId, locale));
    }

    @RolesAllowed({ADMIN, USER})
    @PostMapping
    public ResponseEntity<SuccessResponse> createOrganization(@RequestBody OrganizationDto request, @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(organizationService.createOrganization(request, locale));
    }

    @RolesAllowed({ADMIN})
    @DeleteMapping(value = "/{organizationId}")
    public ResponseEntity<SuccessResponse> deleteOrganization(@PathVariable("organizationId") String organizationId,
                                                              @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(organizationService.deleteOrganization(organizationId, locale));
    }
}
