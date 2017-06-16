package org.openmrs.module.referenceapplication.definition;

import org.openmrs.Program;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.AllPatientsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.MappedParametersCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.ProgramEnrollmentCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jude on 6/16/2017.
 */
@Component
public class CohortDefinitionProvider extends BaseDefinitionLibrary<CohortDefinition> {

	@Override
	public Class<? super CohortDefinition> getDefinitionType() {
		return CohortDefinition.class;
	}

	@Override
	public String getKeyPrefix() {
		return null;
	}

	private String getPatientCreatedQuery(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.patient_id ")
				.append("FROM patient p ")
				.append("WHERE p.date_created >= :startDate")
				.append("AND p.date_created <= :endDate");

		return sb.toString();
	}

	@DocumentedDefinition(value = "activePatientRegistrations")
	public CohortDefinition getActivePatientRegistrations() {

		//By default it will return only active patients
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setQuery(getPatientCreatedQuery());
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date for registrations", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End Date for registrations", Date.class));

		return convert(sqlCohortDefinition, ObjectUtil.toMap("startDate=startDate,endDate=endDate"));
	}

	public CohortDefinition convert(CohortDefinition cd, Map<String, String> renamedParameters) {
		return new MappedParametersCohortDefinition(cd, renamedParameters);
	}
}
