package com.dionChar.publicagencies.catalogue.repository.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.dionChar.publicagencies.catalogue.dto.search.OrganizationMapSearchRequestDTO;
import com.dionChar.publicagencies.catalogue.dto.search.OrganizationSearchCriteriaDTO;
import com.dionChar.publicagencies.catalogue.model.Organization;
import com.dionChar.publicagencies.catalogue.model.enums.PublicSectorStatus;

public class OrganizationSpecification {

	// Η κλάση έχει private constructor γιατί είναι utility class με static methods.
	private OrganizationSpecification() {
		// private constructor για να μην γίνει new
	}

	public static Specification<Organization> nameContains(String name) {
		return (root, query, cb) -> {
			if (name == null || name.trim().isEmpty()) {
				return null;
			}
			return cb.like(cb.upper(root.get("name")), "%" + name.trim().toUpperCase() + "%");
		};
	}

	public static Specification<Organization> hasLegalFormId(Long legalFormId) {
		return (root, query, cb) -> {
			if (legalFormId == null) {
				return null;
			}
			return cb.equal(root.get("legalForm").get("id"), legalFormId);
		};
	}

	public static Specification<Organization> hasPublicSectorStatus(PublicSectorStatus status) {
		return (root, query, cb) -> {
			if (status == null) {
				return null;
			}
			return cb.equal(root.get("publicSectorStatus"), status);
		};
	}

	public static Specification<Organization> hasAnyMinistryId(List<Long> ministryIds) {
		return (root, query, cb) -> {
			if (ministryIds == null || ministryIds.isEmpty()) {
				return null;
			}

			// Κάνουμε TREAT στο root προς PublicOrganization
			var publicOrg = cb.treat(root, com.dionChar.publicagencies.catalogue.model.PublicOrganization.class);

			return publicOrg.get("supervisingMinistry").get("id").in(ministryIds);
		};
	}

	public static Specification<Organization> hasAnyLocalAuthorityId(List<Long> localAuthorityIds) {
		return (root, query, cb) -> {
			if (localAuthorityIds == null || localAuthorityIds.isEmpty()) {
				return null;
			}

			var localOrg = cb.treat(root, com.dionChar.publicagencies.catalogue.model.LocalOrganization.class);

			// JOIN στο Set<LocalAuthority> supervisingLocalAuthorities
			var join = localOrg.join("supervisingLocalAuthorities");

			return join.get("id").in(localAuthorityIds);
		};
	}
	
	public static Specification<Organization> buildSearchSpec(OrganizationSearchCriteriaDTO criteria) {
	    Specification<Organization> spec = Specification.where(null);

	    spec = spec.and(nameContains(criteria.getName()));
	    spec = spec.and(hasLegalFormId(criteria.getLegalFormId()));
	    spec = spec.and(hasPublicSectorStatus(criteria.getPublicSectorStatus()));
	    spec = spec.and(hasAnyMinistryId(criteria.getMinistryIds()));
	    spec = spec.and(hasAnyLocalAuthorityId(criteria.getLocalAuthorityIds()));

	    return spec;
	}
	
	public static Specification<Organization> buildMapSearchSpec(OrganizationMapSearchRequestDTO criteria){
		Specification<Organization> spec = Specification.where(null);

	    spec = spec.and(nameContains(criteria.getName()));
	    spec = spec.and(hasLegalFormId(criteria.getLegalFormId()));
	    spec = spec.and(hasPublicSectorStatus(criteria.getPublicSectorStatus()));
	    spec = spec.and(hasAnyMinistryId(criteria.getMinistryIds()));
	    spec = spec.and(hasAnyLocalAuthorityId(criteria.getLocalAuthorityIds()));

	    return spec;
	}
	

}
