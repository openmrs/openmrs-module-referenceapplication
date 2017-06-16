/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.referenceapplication.reports;

import org.openmrs.module.referenceapplication.definition.CohortDefinitionProvider;
import org.openmrs.module.referenceapplication.definition.PatientDataLibrary;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.library.BuiltInPatientDataLibrary;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NumberOfPatientRegistrations extends BaseReportManager {

	@Autowired
	private BuiltInPatientDataLibrary builtInPatientData;

	@Autowired
	private CohortDefinitionProvider cohortDefinitionProvider;

	@Autowired
	private PatientDataLibrary patientDataLibrary;

	public NumberOfPatientRegistrations() {
	}

	@Override
	public String getUuid() {
		return "7faf04ee-5261-11e7-b114-b2f933d5fe66";
	}

	@Override
	public String getName() {
		return "Number of Patient Registrations (Cohort-Java)";
	}

	@Override
	public String getDescription() {
		return "Number of patient registrations for a given date period";
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> parameterArrayList = new ArrayList<Parameter>();
		parameterArrayList.add(ReportingConstants.START_DATE_PARAMETER);
		parameterArrayList.add(ReportingConstants.END_DATE_PARAMETER);
		return parameterArrayList;
	}

	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDef = new ReportDefinition();
		reportDef.setUuid(getUuid());
		reportDef.setName(getName());
		reportDef.setDescription(getDescription());
		reportDef.setParameters(getParameters());


		PatientDataSetDefinition dataSetDefinition = new PatientDataSetDefinition();
		dataSetDefinition.setName(getName());
		dataSetDefinition.setParameters(getParameters());

		CohortDefinition cohortDefinition = cohortDefinitionProvider.getActivePatientRegistrations();
		dataSetDefinition.addRowFilter(Mapped.mapStraightThrough(cohortDefinition));

		reportDef.addDataSetDefinition("Patient Registration Count", Mapped.mapStraightThrough(dataSetDefinition));

		addColumn(dataSetDefinition, "OpenMRS ID", patientDataLibrary.getOpenmrsId());
		addColumn(dataSetDefinition, "Patient Name", builtInPatientData.getPreferredGivenName());
		addColumn(dataSetDefinition, "Gender", builtInPatientData.getGender());
		addColumn(dataSetDefinition, "Date Created", patientDataLibrary.getDateCreated());


		return reportDef;
	}

	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		return null;
	}

	@Override
	public String getVersion() {
		return "1.0";
	}


	protected void addColumn(PatientDataSetDefinition dsd, String columnName, PatientDataDefinition pdd) {
		dsd.addColumn(columnName, pdd, Mapped.straightThroughMappings(pdd));
	}
}
