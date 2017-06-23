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

import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.referenceapplication.reporting.definition.util.MetadataConstants;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.PropertyConverter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PersonToPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PreferredIdentifierDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.SqlPatientDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Component
public class PatientDataDefinitionLibrary extends BaseDefinitionLibrary<PatientDataDefinition> {

	@Override
	public Class<? super PatientDataDefinition> getDefinitionType() {
		return null;
	}

	@Override
	public String getKeyPrefix() {
		return null;
	}

	@DocumentedDefinition("date_created")
	public PatientDataDefinition getDateCreated() {
		SqlPatientDataDefinition sqlPatientDataDefinition = new SqlPatientDataDefinition();
		sqlPatientDataDefinition.addParameter(new Parameter("startDate", "Start Date for date_created", Date.class));
		sqlPatientDataDefinition.addParameter(new Parameter("endDate", "End Date for date_created", Date.class));
		sqlPatientDataDefinition.setSql(getDateCreatedDefinitionSQLQuery());

		return convert(sqlPatientDataDefinition, ObjectUtil.toMap("startDate=startDate,endDate=endDate"), null);
	}

	private String getDateCreatedDefinitionSQLQuery(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT patient_id, date_created ")
				.append("FROM patient ")
				.append("WHERE date_created >= :startDate ")
				.append("AND date_created <= :endDate");

		return sb.toString();
	}

	@DocumentedDefinition("openmrsId")
	public PatientDataDefinition getOpenmrsId() {
		PreferredIdentifierDataDefinition def = new PreferredIdentifierDataDefinition();
		def.setIdentifierType(MetadataUtils.existing(PatientIdentifierType.class, MetadataConstants.OPENMRS_ID));
		return convert(def, new PropertyConverter(PatientIdentifier.class, "identifier"));
	}

	public PatientDataDefinition convert(PatientDataDefinition pdd, Map<String, String> renamedParameters, DataConverter converter) {
		ConvertedPatientDataDefinition convertedDefinition = new ConvertedPatientDataDefinition();
		convertedDefinition.setDefinitionToConvert(ParameterizableUtil.copyAndMap(pdd, convertedDefinition, renamedParameters));
		if (converter != null) {
			convertedDefinition.setConverters(Arrays.asList(converter));
		}
		return convertedDefinition;
	}

	public PatientDataDefinition convert(PatientDataDefinition pdd, DataConverter converter) {
		return convert(pdd, null, converter);
	}

	public PatientDataDefinition convert(PersonDataDefinition pdd, Map<String, String> renamedParameters, DataConverter converter) {
		return convert(new PersonToPatientDataDefinition(pdd), renamedParameters, converter);
	}

	public PatientDataDefinition convert(PersonDataDefinition pdd, DataConverter converter) {
		return convert(pdd, null, converter);
	}

}
