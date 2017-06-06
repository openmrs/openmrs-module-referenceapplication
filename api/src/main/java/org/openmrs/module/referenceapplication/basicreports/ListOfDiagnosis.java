package org.openmrs.module.referenceapplication.basicreports;

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
public class ListOfDiagnosis extends BaseReportManager {

	public ListOfDiagnosis() {
	}

	@Override
	public String getUuid() {
		return "e451ae04-4881-11e7-a919-92ebcb67fe33";
	}

	@Override
	public String getName() {
		return "List of Diagnosis";
	}

	@Override
	public String getDescription() {
		return "List all diagnosis's for a given date range along with the count";
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

		reportDef.addDataSetDefinition("listOfDiagnosis", Mapped.mapStraightThrough(sqlDataDef));


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
		stringBuilder.append("select cn.name, count(*) as 'count' ");
		stringBuilder.append("from  obs, concept_name cn ");
		stringBuilder.append("where obs.concept_id = (select concept_id from concept where uuid='1284AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA') ");
		stringBuilder.append("and value_coded= cn.concept_id and ");
		stringBuilder.append("locale='en' and ");
		stringBuilder.append("locale_preferred = '1' ");
		stringBuilder.append("and obs.date_created >= :startDate ");
		stringBuilder.append("and obs.date_created <= :endDate ");
		stringBuilder.append("group by value_coded, cn.name ");
		stringBuilder.append("order by count(*) desc ");

		return stringBuilder.toString();
	}
}
