package io.openliberty.guides.system;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class SystemLivenessCheck implements HealthCheck {
	
	public static String LIVENESS_CHECK = SystemResource.class.getSimpleName() + " Liveness Check" ;
	

	@Override
	public HealthCheckResponse call() {
		MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
		long memoryUsed = memory.getHeapMemoryUsage().getUsed();
		long memoryMax = memory.getHeapMemoryUsage().getMax();
		
		return HealthCheckResponse.named(LIVENESS_CHECK)
				.withData("memory used", memoryUsed )
				.withData("memory max", memoryMax )
				.state( memoryUsed < memoryMax * 0.9 )
				.build();
		
	}

}
