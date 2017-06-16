package org.openmrs.module.referenceapplication.definition;

import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.referenceapplication.definition.constants.MetadataConstants;
import org.openmrs.module.reporting.common.Birthdate;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.PropertyConverter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PersonToPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PreferredIdentifierDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.SqlPatientDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jude on 6/16/2017.
 */
@Component
public class PatientDataLibrary extends BaseDefinitionLibrary<PatientDataDefinition> {

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

		return convert(new SqlPatientDataDefinition(), ObjectUtil.toMap("startDate=startDate,endDate=endDate"), null);
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

	private String getDateCreatedDefinitionSQLQuery(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT patient_id, date_created ")
				.append("FROM patient ")
				.append("WHERE date_created >= :startDate ")
				.append("AND date_created <= :endDate");

		return sb.toString();
	}
}
