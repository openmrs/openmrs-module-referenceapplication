/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.referenceapplication.reporting.definition.library;

import org.openmrs.module.referenceapplication.reporting.definition.util.ParameterFactory;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.MappedParametersCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CohortDefinitionLibrary extends BaseDefinitionLibrary<CohortDefinition> {

	@Override
	public Class<? super CohortDefinition> getDefinitionType() {
		return CohortDefinition.class;
	}

	@Override
	public String getKeyPrefix() {
		return null;
	}

	@DocumentedDefinition(value = "activePatientRegistrationsByDatePeriod")
	public CohortDefinition getActivePatientRegistrationsByDatePeriod() {

		//By default it will return only active patients
		SqlCohortDefinition def = new SqlCohortDefinition();
		def.setQuery(getPatientCreatedQuery());
		def.addParameter(ParameterFactory.getStartDate("Start Date for registrations"));
		def.addParameter(ParameterFactory.getEndDate("End Date for registrations"));

		return convert(def, ObjectUtil.toMap("startDate=startDate,endDate=endDate"));
	}

	private String getPatientCreatedQuery(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.patient_id ")
				.append("FROM patient p ")
				.append("WHERE p.date_created >= :startDate ")
				.append("AND p.date_created <= :endDate ");

		return sb.toString();
	}

	public CohortDefinition convert(CohortDefinition cd, Map<String, String> renamedParameters) {
		return new MappedParametersCohortDefinition(cd, renamedParameters);
	}
}
