package com.hackensack.umc.response;

import com.hackensack.umc.patient_data.Address;
import com.hackensack.umc.patient_data.Extension;
import com.hackensack.umc.patient_data.MaritalStatus;
import com.hackensack.umc.patient_data.Name;
import com.hackensack.umc.patient_data.Telecom;
import com.hackensack.umc.patient_data.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class PatientResponse implements Serializable {
	private String resourceType;
	private String id;
	private Text text;
	private ArrayList<Name> name;
	private ArrayList<Telecom> telecom;
	private String birthDate;
	private ArrayList<Address> address;
	private MaritalStatus maritalStatus;
    private ArrayList<Extension> extension;

	public PatientResponse(String resourceType, String id, Text text,
			ArrayList<Name> name, ArrayList<Telecom> telecom, String birthDate,
			ArrayList<Address> address, MaritalStatus maritalStatus,
			String gender, String active,ArrayList<Extension>extensions) {
		super();
		this.resourceType = resourceType;
		this.id = id;
		this.text = text;
		this.name = name;
		this.telecom = telecom;
		this.birthDate = birthDate;
		this.address = address;
		this.maritalStatus = maritalStatus;
		this.gender = gender;
		this.active = active;
        this.extension=extensions;
	}

	public PatientResponse(String resourceType, String id,

	String gender, String active, Text text) {
		super();
		this.resourceType = resourceType;
		this.id = id;

		this.gender = gender;
		this.active = active;
		this.text = text;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public ArrayList<Name> getName() {
		return name;
	}

	public void setName(ArrayList<Name> name) {
		this.name = name;
	}

	public ArrayList<Telecom> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<Telecom> telecom) {
		this.telecom = telecom;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public ArrayList<Address> getAddress() {
		return address;
	}

	public void setAddress(ArrayList<Address> address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "PatientForEpic [resourceType=" + resourceType + ", id=" + id
				+ ", text=" + text + ", name=" + name + ", telecom=" + telecom
				+ ", birthDate=" + birthDate + ", address=" + address + "]";
	}

	private String gender;
	private String active;

	/*
	 * "{
	 *
	 * ""gender"": ""male"", ""address"": [ { ""use"": ""home"", ""line"": [
	 * ""1201SMainSt"", null ], ""state"": ""CA"", ""city"": ""Milpitas"",
	 * ""country"": ""en-US"", ""postalCode"": ""95035"", ""extension"": [ {
	 * ""url"": ""http: //hl7.org/fhir/StructureDefinition/us-core-county"",
	 * ""valueString"": ""SantaClaraCounty"" } ] } ], ""active"": ""true"",
	 * ""telecom"": [ { ""system"": ""phone"", ""value"": ""408-913-5805"",
	 * ""use"": ""mobile"" }, { ""system"": ""email"", ""value"":
	 * ""bibinkurian@live.com"", ""use"": ""home"" } ], ""birthDate"":
	 * ""1993-05-30"", ""maritalStatus"": { ""coding"": [ { ""system"": ""http:
	 * //hl7.org/fhir/v3/MaritalStatus"", ""code"": ""U"", ""display"":
	 * ""Unmarried"" } ], ""text"": ""Getrouwd"" }, ""text"": { ""status"":
	 * ""generated"", ""div"": ""<div>!--SnippedforBrevity--></div>"" },
	 * ""extension"": [ { ""valueCodeableConcept"": [ { ""coding"": {
	 * ""system"": ""http: //hl7.org/fhir/v3/Race"", ""code"": ""1096-7"" } } ],
	 * ""url"": ""http: //hl7.org/fhir/StructureDefinition/us-core-race"" }, {
	 * ""url"": ""http: //hl7.org/fhir/StructureDefinition/us-core-race"",
	 * ""valueCodeableConcept"": [ { ""coding"": { ""system"": ""http:
	 * //hl7.org/fhir/v3/Ethnicity"", ""code"": ""2162-6"" } } ] } ],
	 * ""resourceType"": ""Patient"", ""name"": { ""use"": ""official"",
	 * ""given"": ""Bibin"", ""family"": ""Kurian"" } }"
	 */

	/*
	 * { "resourceType": "Patient", "id": "Z5500", "text": { "status":
	 * "generated", "div":
	 * "<div><div class=\"hapiHeaderText\"> Bibin <b>KURIAN </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Address</td><td><span>1201 S Main St109 </span><br /><span>Freemont </span><span>California </span><span>United States of America </span></td></tr><tr><td>Date of birth</td><td><span>30 May 1987</span></td></tr></tbody></table></div>"
	 * }, "name": [ { "family": [ "Kurian" ], "given": [ "Bibin" ] } ],
	 * "telecom": [ { "system": "email", "value":
	 * "bibin_kurian@persistent.combibin_kurian@persistent.co.in", "use": "home"
	 * } ], "birthDate": "1987-05-30", "address": [ { "use": "home", "line": [
	 * "1201 S Main St109" ], "city": "Freemont", "state": "California",
	 * "postalCode": "95036", "country": "United States of America" }, { "use":
	 * "temp" } ]
	 */
}
