package com.epaylater.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDto {
    List<Long> debit_ids;
    Long customer_id;
    Date credit_date;
    Double credit_amount;
}
