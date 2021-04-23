/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.caze;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.facility.FacilityType;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.visit.VisitDto;
import de.symeda.sormas.api.visit.VisitStatus;

public final class CaseLogic {

	private CaseLogic() {
		// Hide Utility Class Constructor
	}

	private static final String EPID_PATTERN_COMPLETE = "([A-Z]{3}-){3}[0-9]{2}-[0-9]+";
	private static final String EPID_PATTERN_PREFIX = "([A-Z]{3}-){3}[0-9]{2}-";

	public static void validateInvestigationDoneAllowed(CaseDataDto caze) throws ValidationException {

		if (caze.getCaseClassification() == CaseClassification.NOT_CLASSIFIED) {
			throw new ValidationException("Not allowed to set investigation status to done for an unclassified case.");
		}
	}

	public static Date getStartDate(Date onsetDate, Date reportDate) {

		if (onsetDate != null) {
			return onsetDate;
		} else {
			return reportDate;
		}
	}

	public static Date getEndDate(Date onsetDate, Date reportDate, Date followUpUntil) {
		return followUpUntil != null ? followUpUntil : onsetDate != null ? onsetDate : reportDate;
	}

	public static boolean isEpidNumberPrefix(String s) {

		if (StringUtils.isEmpty(s)) {
			return false;
		}

		return Pattern.matches(EPID_PATTERN_PREFIX, s);
	}

	public static boolean isCompleteEpidNumber(String s) {

		if (StringUtils.isEmpty(s)) {
			return false;
		}

		return Pattern.matches(EPID_PATTERN_COMPLETE, s);
	}

	/**
	 * Handles the hospitalization change of a case.
	 * 
	 * @param caze
	 *            The new CaseDataDto for which the facility change should be handled.
	 * @param oldCase
	 *            The Dto of the existing case being changed.
	 * @param isTransfer
	 *            Indicates if the old case is transferred (both from or to a hospital).
	 */
	public static void handleHospitalization(CaseDataDto caze, CaseDataDto oldCase, boolean isTransfer) {
		// todo (@JonasCir) I feel this whole class or at least this method should be absorbed by the case EJB
		// case is already in a hospital and is transferred from it (discharge or other hospital)...
		if (isTransfer && FacilityType.HOSPITAL.equals(oldCase.getFacilityType())) {
			// therefore add the old hospitalization to the list of previous ones
			PreviousHospitalizationDto prevHosp = PreviousHospitalizationDto.build(oldCase);
			caze.getHospitalization().getPreviousHospitalizations().add(prevHosp);
			caze.getHospitalization().setHospitalizedPreviously(YesNoUnknown.YES);
		}

		// clear everything if a case is transferred or discharged from a hospital
		if (isTransfer || !FacilityType.HOSPITAL.equals(caze.getFacilityType())) {
			// set everything but previous hospitalization to null
			try {
				PropertyDescriptor[] pds = Introspector.getBeanInfo(HospitalizationDto.class, EntityDto.class).getPropertyDescriptors();

				for (PropertyDescriptor pd : pds) {
					// Skip properties without a read or write method
					if (pd.getWriteMethod() == null
						|| HospitalizationDto.HOSPITALIZED_PREVIOUSLY.equals(pd.getName())
						|| HospitalizationDto.PREVIOUS_HOSPITALIZATIONS.equals(pd.getName())) {
						continue;
					}

					pd.getWriteMethod().invoke(caze.getHospitalization(), (Object) null);
				}
			} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException("Exception when trying to fill dto: " + e.getMessage(), e.getCause());
			}
		}

		// case gets transferred to a hospital
		if (isTransfer && FacilityType.HOSPITAL.equals(caze.getFacilityType())) {
			caze.getHospitalization().setAdmissionDate(new Date());
		}
	}

	/**
	 * Calculates the follow-up until date of the case based on its start date (onset contact or report date), the follow-up duration of
	 * the disease, the current follow-up until date and the date of the last cooperative visit.
	 *
	 * @param ignoreOverwrite
	 *            Returns the expected follow-up until date based on case start date, follow-up duration of the disease and date of the
	 *            last cooperative visit. Ignores current follow-up until date and whether or not follow-up until has been overwritten.
	 */
	public static Date calculateFollowUpUntilDate(CaseDataDto caze, List<VisitDto> visits, int followUpDuration, boolean ignoreOverwrite) {

		Date beginDate = CaseLogic.getStartDate(caze.getSymptoms().getOnsetDate(), caze.getReportDate());
		Date standardUntilDate = DateHelper.addDays(beginDate, followUpDuration);
		Date untilDate = !ignoreOverwrite && caze.isOverwriteFollowUpUntil() ? caze.getFollowUpUntil() : standardUntilDate;

		Date lastVisitDate = null;
		boolean additionalVisitNeeded = true;
		for (VisitDto visit : visits) {
			if (lastVisitDate == null || DateHelper.getStartOfDay(visit.getVisitDateTime()).after(DateHelper.getStartOfDay(lastVisitDate))) {
				lastVisitDate = visit.getVisitDateTime();
			}
			if (additionalVisitNeeded
				&& !DateHelper.getStartOfDay(visit.getVisitDateTime()).before(DateHelper.getStartOfDay(untilDate))
				&& visit.getVisitStatus() == VisitStatus.COOPERATIVE) {
				additionalVisitNeeded = false;
			}
		}

		// Follow-up until needs to be extended to the date after the last visit if there is no cooperative visit after the follow-up until date
		if (additionalVisitNeeded && lastVisitDate != null) {
			untilDate = DateHelper.addDays(untilDate, DateHelper.getDaysBetween(untilDate, lastVisitDate));
		}

		// If the follow-up until date is before the standard follow-up until date for some reason (e.g. because the report date and/or last contact
		// date were changed), set it to the standard follow-up until date
		if (DateHelper.getStartOfDay(untilDate).before(standardUntilDate)) {
			untilDate = standardUntilDate;
		}

		return untilDate;
	}
}
