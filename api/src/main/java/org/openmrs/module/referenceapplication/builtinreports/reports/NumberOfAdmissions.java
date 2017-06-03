package org.openmrs.module.referenceapplication.builtinreports.reports;

import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jude on 6/3/2017.
 */
public class NumberOfAdmissions extends BaseReportManager {

	@Override
	public String getUuid() {
		return "b8463d94-4844-11e7-a919-92ebcb67fe33";
	}

	@Override
	public String getName() {
		return "Number of Discharges (Java)";
	}

	@Override
	public String getDescription() {
		return "Number of Discharges for a given location";
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> parameterArrayList = new ArrayList<Parameter>();
		parameterArrayList.add(ReportingConstants.LOCATION_PARAMETER);
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

		reportDef.addDataSetDefinition("dischargeCount", Mapped.mapStraightThrough(sqlDataDef));


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
		stringBuilder.append("select 'Admissions', count(*) as 'total' from encounter e ");
		stringBuilder.append("where e.encounter_type=(select encounter_type_id from encounter_type where uuid='e22e39fd-7db2-45e7-80f1-60fa0d5a4378') ");
		stringBuilder.append("and e.location_id=:location ");
		stringBuilder.append("and e.voided = 0 ");
		stringBuilder.append("group by e.encounter_type ");

		return stringBuilder.toString();
	}
}
