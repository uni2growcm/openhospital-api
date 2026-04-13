package org.isf.integrations.labbook.mapper;

import org.isf.integrations.labbook.dto.LabbookRawPatientDTO;
import org.isf.integrations.labbook.models.LabbookRawPatient;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class LabbookRawPatientMapper extends GenericMapper<LabbookRawPatient, LabbookRawPatientDTO> {
	public LabbookRawPatientMapper() {
		super(LabbookRawPatient.class, LabbookRawPatientDTO.class);
	}

	@Override
	public LabbookRawPatientDTO map2DTO(LabbookRawPatient fromObj) {

		LabbookRawPatientDTO dto = new LabbookRawPatientDTO();

		dto.setIdData(fromObj.idData());
		dto.setIdUser(fromObj.idUser());
		dto.setAno(fromObj.ano());
		dto.setCode(fromObj.code());
		dto.setCodeLab(fromObj.codeLab());
		dto.setName(fromObj.name());
		dto.setFirstname(fromObj.firstname());
		dto.setBirth(fromObj.birth());
		dto.setSex(fromObj.sex());
		dto.setAddress(fromObj.address());
		dto.setZipcode(fromObj.zipcode());
		dto.setCity(fromObj.city());
		dto.setPhone1(fromObj.phone1());
		dto.setPhone2(fromObj.phone2());
		dto.setProfession(fromObj.profession());
		dto.setMaiden(fromObj.maiden());
		dto.setDistrict(fromObj.district());
		dto.setPbox(fromObj.pbox());
		dto.setBirthApprox(fromObj.birthApprox());
		dto.setAge(fromObj.age());
		dto.setAgeUnit(fromObj.ageUnit());
		dto.setEmail(fromObj.email());
		dto.setAgreement(fromObj.agreement());
		dto.setMidname(fromObj.midname());
		dto.setNationality(fromObj.nationality());
		dto.setResident(fromObj.resident());
		dto.setBloodGroup(fromObj.bloodGroup());
		dto.setBloodRhesus(fromObj.bloodRhesus());

		return dto;
	}
}
