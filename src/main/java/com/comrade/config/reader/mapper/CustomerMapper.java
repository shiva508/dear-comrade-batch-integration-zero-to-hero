package com.comrade.config.reader.mapper;

import com.comrade.model.CustomerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class CustomerMapper implements RowMapper<CustomerModel> {

    Date jobRunDate = null;

    @Override
    public CustomerModel mapRow(RowSet rowSet) throws Exception {
        try {
            String[] currentRow = rowSet.getCurrentRow();
            return new CustomerModel(
                    Integer.valueOf(currentRow[0]),
                    currentRow[1],
                    currentRow[2],
                    currentRow[3] ,
                    currentRow[4],
                    currentRow[5]);
        } catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }

    }
}
