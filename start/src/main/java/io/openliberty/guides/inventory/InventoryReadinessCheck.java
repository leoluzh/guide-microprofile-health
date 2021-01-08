package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {

	public static final String READINESS_CHECK = InventoryResource.class.getSimpleName() + " Readiness Check" ;
	
	@Inject
	private InventoryConfig config;
	
	public boolean isReady() {
		if( config.isInMaintenance() ) {
			return false;
		}
		try {
			String URL = InventoryUtils.buildUrl("http", "localhost", config.getPortNumber() , "/system/properties" );
			Client client = ClientBuilder.newClient();
			Response response = client.target( URL ).request(MediaType.APPLICATION_JSON_TYPE).get();
			if( response.getStatus() != 200 ) {
				return false;
			}
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	@Override
	public HealthCheckResponse call() {
		if( !isReady() ) {
			return HealthCheckResponse.down(READINESS_CHECK);
		}
		return HealthCheckResponse.up(READINESS_CHECK);
	}

}
