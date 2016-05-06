package com.hackensack.umc.patient_data;

import java.io.Serializable;
import java.util.ArrayList;

public class Address implements Serializable {

	/*
	 * ""state"": ""CA"", ""city"": ""Milpitas"", ""country"": ""en-US"",
	 * ""postalCode"": ""95035"", ""extension"": [ { ""url"": ""http:
	 * //hl7.org/fhir/StructureDefinition/us-core-county"", ""valueString"":
	 * ""SantaClaraCounty"" }
	 */
	private String use;
	private ArrayList<String> line;
	private String city;
	private String state;
	private String postalCode;
	private String country;

    public Address(String use, ArrayList<String> line, String state, String city, String postalCode, String country, ArrayList<Extension> extension) {
        this.use = use;
        this.line = line;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.extension = extension;
    }
	public Address(String use, ArrayList<String> line, String state, String city, String postalCode, String country) {
		this.use = use;
		this.line = line;
		this.state = state;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;

	}
    private ArrayList<Extension> extension;

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public ArrayList<String> getLine() {
		return line;
	}

	public void setLine(ArrayList<String> line) {
		this.line = line;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [use=" + use + ", line=" + line + ", city=" + city
				+ ", state=" + state + ", postalCode=" + postalCode
				+ ", country=" + country + "]";
	}

}
