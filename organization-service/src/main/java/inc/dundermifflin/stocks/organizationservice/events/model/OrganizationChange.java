package inc.dundermifflin.stocks.organizationservice.events.model;

import lombok.*;

@Data
@AllArgsConstructor
public class OrganizationChange {
	private String type;
	private String action;
	private String organizationId;
	private String correlationId;
}