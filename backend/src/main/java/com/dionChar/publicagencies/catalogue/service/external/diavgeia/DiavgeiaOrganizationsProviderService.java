package com.dionChar.publicagencies.catalogue.service.external.diavgeia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dionChar.publicagencies.catalogue.service.external.diavgeia.DiavgeiaOrganizationFinderService.OrganizationResponse;

@Service
public class DiavgeiaOrganizationsProviderService {

	private final RestTemplate restTemplate;
	
	@Value("${diavgeia.api.url}")
    String apiUrl;
	
	
	 public DiavgeiaOrganizationsProviderService(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }
	 
	 @Cacheable("diavgeiaOrganizations")
		public DiavgeiaOrganizationFinderService.OrganizationResponse callDiavgeiaOrganizations() {
			String url = String.format("%s/organizations.json", apiUrl);

			ResponseEntity<OrganizationResponse> response = restTemplate.exchange(url, HttpMethod.GET, null,
					OrganizationResponse.class);

			OrganizationResponse body = response.getBody();

			if (body == null || body.organizations == null || body.organizations.isEmpty()) {
				throw new RuntimeException("No organizations list found");
			}
			return body;
		}
	
	
}
