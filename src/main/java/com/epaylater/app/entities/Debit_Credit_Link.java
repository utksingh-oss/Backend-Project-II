package com.epaylater.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Debit_Credit_Link {
    Long link_id;
    Long debit_id;
    Long credit_id;
}
