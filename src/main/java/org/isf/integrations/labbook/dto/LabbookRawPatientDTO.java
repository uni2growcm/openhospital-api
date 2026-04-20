package org.isf.integrations.labbook.dto;

import java.time.LocalDate;

public class LabbookRawPatientDTO {

	private Integer idData;

	private Integer idUser;

	private Integer ano;

	private String code;

	private String codeLab;

	private String name;

	private String firstname;

	private LocalDate birth;

	private Integer sex;

	private String address;

	private  String zipcode;

	private String city;

	private String phone1;

	private String phone2;

	private String profession;

	private String maiden;

	private String district;

	private String pbox;

	private Integer birthApprox;

	private Integer age;

	private Integer ageUnit;

	private String email;

	private String agreement;

	private String midname;

	private Integer nationality;

	private String resident;

	private Integer bloodGroup;

	private Integer bloodRhesus;

	public LabbookRawPatientDTO() {
	}

	public LabbookRawPatientDTO(Integer idData, Integer idUser, Integer ano, String code, String codeLab, String name,
	                            String firstname, LocalDate birth, Integer sex, String address, String zipcode, String city,
	                            String phone1, String phone2, String profession, String maiden, String district, String pbox,
	                            Integer birthApprox, Integer age, Integer ageUnit, String email, String agreement, String midname,
	                            Integer nationality, String resident, Integer bloodGroup, Integer bloodRhesus) {
		this.idData = idData;
		this.idUser = idUser;
		this.ano = ano;
		this.code = code;
		this.codeLab = codeLab;
		this.name = name;
		this.firstname = firstname;
		this.birth = birth;
		this.sex = sex;
		this.address = address;
		this.zipcode = zipcode;
		this.city = city;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.profession = profession;
		this.maiden = maiden;
		this.district = district;
		this.pbox = pbox;
		this.birthApprox = birthApprox;
		this.age = age;
		this.ageUnit = ageUnit;
		this.email = email;
		this.agreement = agreement;
		this.midname = midname;
		this.nationality = nationality;
		this.resident = resident;
		this.bloodGroup = bloodGroup;
		this.bloodRhesus = bloodRhesus;
	}

	public Integer getIdData() {
		return idData;
	}

	public void setIdData(Integer idData) {
		this.idData = idData;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeLab() {
		return codeLab;
	}

	public void setCodeLab(String codeLab) {
		this.codeLab = codeLab;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getMaiden() {
		return maiden;
	}

	public void setMaiden(String maiden) {
		this.maiden = maiden;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPbox() {
		return pbox;
	}

	public void setPbox(String pbox) {
		this.pbox = pbox;
	}

	public Integer getBirthApprox() {
		return birthApprox;
	}

	public void setBirthApprox(Integer birthApprox) {
		this.birthApprox = birthApprox;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getAgeUnit() {
		return ageUnit;
	}

	public void setAgeUnit(Integer ageUnit) {
		this.ageUnit = ageUnit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getMidname() {
		return midname;
	}

	public void setMidname(String midname) {
		this.midname = midname;
	}

	public Integer getNationality() {
		return nationality;
	}

	public void setNationality(Integer nationality) {
		this.nationality = nationality;
	}

	public String getResident() {
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public Integer getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(Integer bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Integer getBloodRhesus() {
		return bloodRhesus;
	}

	public void setBloodRhesus(Integer bloodRhesus) {
		this.bloodRhesus = bloodRhesus;
	}
}
