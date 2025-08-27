package org.isf.conditioning.mapper;

import java.util.List;
import java.util.stream.Collectors;


import org.isf.conditioning.dto.ConditioningDTO;
import org.isf.conditioning.model.Conditioning;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class ConditioningMapper extends GenericMapper<Conditioning, ConditioningDTO> {

	public ConditioningMapper() {
		super(Conditioning.class, ConditioningDTO.class);
	}

	@Override
	public List<ConditioningDTO> map2DTOList(List<Conditioning> list) {
		return list.stream().map(this::map2DTO).collect(Collectors.toList());
	}

	@Override
	public List<Conditioning> map2ModelList(List<ConditioningDTO> list) {
		return list.stream().map(this::map2Model).collect(Collectors.toList());
	}
}
