package org.openmrs.module.referenceapplication.builtinreports.reports;

import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jude on 6/3/2017.
 */

@Component
public class NumberOfVisitNotes extends BaseReportManager {

	public NumberOfVisitNotes() {
	}

	@Override
	public String getUuid() {
		return "9667ac52-4881-11e7-a919-92ebcb67fe33";
	}

	@Override
	public String getName() {
		return "Number of Visit Notes";
	}

	@Override
	public String getDescription() {
		return "Number of active visit notes for a given time period";
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

		SqlDataSetDefinition sqlDataDef = new SqlDataSetDefinition();
		sqlDataDef.setName(getName());
		sqlDataDef.addParameters(getParameters());
		sqlDataDef.setSqlQuery(getSQLQuery());

		reportDef.addDataSetDefinition("Visit Note Count", Mapped.mapStraightThrough(sqlDataDef));


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

	private String getSQLQuery(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select count(*) as Number_of_visit_notes ");
		stringBuilder.append("from encounter e ");
		stringBuilder.append("where e.encounter_type = (select et.encounter_type_id ");
		stringBuilder.append("from encounter_type et ");
		stringBuilder.append("where et.uuid = 'd7151f82-c1f3-4152-a605-2f9ea7414a79') ");
		stringBuilder.append("and e.encounter_datetime >= :startDate ");
		stringBuilder.append("and e.encounter_datetime <= :endDate ");
		stringBuilder.append("and e.voided = 0 ");

		return stringBuilder.toString();
	}
}
