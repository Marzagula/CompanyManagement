package com.gminds.employee_service.util.mappers;

import com.gminds.employee_service.model.Job;
import com.gminds.employee_service.model.dtos.JobDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    JobDTO toJobDTO(Job job);

    @Mapping(target = "id", ignore = true)
    Job toJob(JobDTO jobDTO);
}
