package it.io.openliberty.guides.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import javax.json.JsonArray;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.openliberty.guides.inventory.InventoryLivenessCheck;
import io.openliberty.guides.inventory.InventoryReadinessCheck;
import io.openliberty.guides.system.SystemLivenessCheck;
import io.openliberty.guides.system.SystemReadinessCheck;

public class HealthIT {

	private JsonArray servicesStates;
	private static HashMap<String,String> endpointData;
	
	
	private String HEALTH_ENDPOINT = "health" ;
	private String READINESS_ENDPOINT = "health/ready" ;
	private String LIVENESS_ENDPOINT = "health/live" ;
	
	@BeforeEach
	public void setup() {
		endpointData = new HashMap<>();
	}
	
	@AfterEach
	public void teardown() {
		HealthITUtil.cleanUp();
	}
	
	@Test
	public void testIfServicesAreUp() {

		endpointData.put( SystemLivenessCheck.LIVENESS_CHECK , "UP" );
		endpointData.put( SystemReadinessCheck.READINESS_CHECK , "UP" );
		endpointData.put( InventoryLivenessCheck.LIVENESS_CHECK, "UP" );
		endpointData.put( InventoryReadinessCheck.READINESS_CHECK, "UP" );

		servicesStates = HealthITUtil.connectToHealthEnpoint(200, HEALTH_ENDPOINT);
		checkStates(endpointData,servicesStates);
	}
	
	@Test
	public void testReadiness() {
		
		endpointData.put( SystemReadinessCheck.READINESS_CHECK , "UP" );
		endpointData.put( InventoryReadinessCheck.READINESS_CHECK, "UP" );
		
		servicesStates = HealthITUtil.connectToHealthEnpoint(200, READINESS_ENDPOINT);
		checkStates(endpointData,servicesStates);
	}
	
	@Test
	public void testLiveness() {
		
		endpointData.put( SystemLivenessCheck.LIVENESS_CHECK , "UP" );
		endpointData.put( InventoryLivenessCheck.LIVENESS_CHECK, "UP" );

		servicesStates = HealthITUtil.connectToHealthEnpoint(200, LIVENESS_ENDPOINT);
		checkStates(endpointData,servicesStates);
	}
	
	@Test
	public void testIfInventoryServicesAreDown() {
		
		endpointData.put( SystemLivenessCheck.LIVENESS_CHECK , "UP" );
		endpointData.put( SystemReadinessCheck.READINESS_CHECK , "UP" );
		endpointData.put( InventoryLivenessCheck.LIVENESS_CHECK, "UP" );
		endpointData.put( InventoryReadinessCheck.READINESS_CHECK, "UP" );
		
		servicesStates = HealthITUtil.connectToHealthEnpoint(200, HEALTH_ENDPOINT);
		checkStates(endpointData,servicesStates);

		endpointData.put( InventoryReadinessCheck.READINESS_CHECK, "DOWN" );
		HealthITUtil.changeInventoryProperty(
				HealthITUtil.INV_MAINTENANCE_FALSE , 
				HealthITUtil.INV_MAINTENANCE_TRUE );
		
		servicesStates = HealthITUtil.connectToHealthEnpoint(503, HEALTH_ENDPOINT);
		checkStates(endpointData, servicesStates);
		
		
	}
	
	
	
	private void checkStates( HashMap<String,String> testData , JsonArray serviceStates ) {
		testData.forEach( ( service , expectedState ) -> {
			assertEquals( expectedState , HealthITUtil.getActualState(service, serviceStates) , 
					String.format("The state of %s service is not matching",service) );
		});
	}
	
}
