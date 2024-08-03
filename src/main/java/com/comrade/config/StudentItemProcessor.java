package com.comrade.config;

import com.comrade.entity.db.old.StudentOld;
import com.comrade.entity.db.latest.StudentLatest;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentItemProcessor implements ItemProcessor<StudentOld, StudentLatest> {

	@Override
	public StudentLatest process(StudentOld studentOld) throws Exception {
		StudentLatest studentLatest = new StudentLatest();
		studentLatest.setStudentId(studentOld.getStudentId());
		studentLatest.setFirstName(studentOld.getFirstName());
		studentLatest.setLastName(studentOld.getLastName());
		studentLatest.setEmail(studentOld.getEmail());
		return studentLatest;
	}

}
