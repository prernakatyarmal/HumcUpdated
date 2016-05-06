package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

public class PatientForEpic implements Serializable {
	private String resourceType;

	private Text text;
	private Name name;
	private ArrayList<Telecom> telecom;
	private String birthDate;
	private ArrayList<Address> address;
	private MaritalStatus maritalStatus;
    private ArrayList<Extension> extension;

	public ArrayList<Photo> getphoto() {
		return photo;
	}

	public void setphoto(ArrayList<Photo> photo) {
		this.photo = photo;
	}

	private ArrayList<Photo>photo;

	public ArrayList<Identifier> getIdentifiers() {
		return identifier;
	}

	public void setIdentifiers(ArrayList<Identifier> identifier) {
		this.identifier = identifier;
	}

	private ArrayList<Identifier>identifier;

	public PatientForEpic(String resourceType, Text text,
						  Name name, ArrayList<Telecom> telecom, String birthDate,
						  ArrayList<Address> address, MaritalStatus maritalStatus,
						  String gender, String active, ArrayList<Extension> extensions) {
		super();
		this.resourceType = resourceType;

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

	public PatientForEpic(String resourceType, String id,

						  String gender, String active, Text text) {
		super();
		this.resourceType = resourceType;


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



	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
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
		return "PatientForEpic [resourceType=" + resourceType + ", text=" + text + ", name=" + name + ", telecom=" + telecom
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
