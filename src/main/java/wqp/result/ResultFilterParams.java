package wqp.result;

public enum ResultFilterParams {

	STATE("statecode", "FIPS code of a state"),
	COUNTY("countycode", "FIPS code of a county"),
	CHARACTERISTIC_TYPE("characteristicType", "Complete name of a characteristic type"),
	START_DATE_LO("startDateLo", "The start date of result in mm-dd-yyyy, mm-yyyy, or yyyy format.  Inclusive?"),
	START_DATE_HI("startDateHi", "The end date of result in mm-dd-yyyy, mm-yyyy, or yyyy format.  Inclusive?"),
	PROVIDERS("providers", "Name of the source of the data, e.g. NWIS, STORET, or STEWARDS"),
	SITE_TYPE("siteType", "The type of site:  Stream");

	final String keyName;
	final String description;

	final boolean splitToMultiValues;

	ResultFilterParams(String keyName, String description) {
		this.keyName = keyName;
		this.description = description;
		this.splitToMultiValues = true;
	}

	public String getKeyName() {
		return keyName;
	}

	public String getDescription() {
		return description;
	}

	public boolean isSplitToMultiValues() {
		return splitToMultiValues;
	}
}
