package io.openliberty.guides.system;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class SystemReadinessCheck implements HealthCheck {
	
	private static final String READINESS_CHECK = SystemResource.class.getSimpleName() + " Readiness Check" ;
	
	@Override
	public HealthCheckResponse call() {
		if( ! "defaultServer".equals( System.getProperty("wlp.server.name") ) ) {
			return HealthCheckResponse.down(READINESS_CHECK);
		}
		return HealthCheckResponse.up(READINESS_CHECK);		
	}

}
