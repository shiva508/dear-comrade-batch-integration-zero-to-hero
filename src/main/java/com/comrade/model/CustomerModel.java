package com.comrade.model;

import java.util.Date;

public record CustomerModel(Integer sNo,
                            String name,
                            String gender,
                            String age,
                            String date,
                            String country) {
}
