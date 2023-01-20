package com.epaylater.app.service;


import com.epaylater.app.dto.CreditDto;
import com.epaylater.app.entities.Credit;
import com.epaylater.app.entities.Customer;
import com.epaylater.app.entities.Debit;
import com.epaylater.app.entities.Debit_Credit_Link;
import com.epaylater.app.repository.CreditDao;
import com.epaylater.app.repository.CustomerDao;
import com.epaylater.app.repository.DebitDao;
import com.epaylater.app.repository.LinkDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CreditService {
    private final CreditDao creditDao;
    private final CustomerDao customerDao;
    private final DebitDao debitDao;
    private final LinkDao linkDao;
    private Jdbi jdbi;

    public CreditService(Jdbi jdbi){
        this.creditDao = jdbi.onDemand(CreditDao.class);
        this.customerDao = jdbi.onDemand(CustomerDao.class);
        this.debitDao = jdbi.onDemand(DebitDao.class);
        this.linkDao = jdbi.onDemand(LinkDao.class);
    }

    //This can be improved.
    public Long addCredit(CreditDto credit) throws Exception {
        List<Long> debit_ids = credit.getDebit_ids();
        Double total_amount = 0.0;
        for(Long id: debit_ids){
            Debit curr_debit = debitDao.getDebitById(id);
            if(curr_debit == null) {
                throw new Exception();
            }else{
                total_amount += curr_debit.getRemaining_amount();
            }
        }
        if(credit.getCredit_amount() > total_amount)throw new Exception();
        Credit credit_obj = new Credit();
        credit_obj.setCredit_date(credit.getCredit_date());
        credit_obj.setCredit_amount(credit.getCredit_amount());
        credit_obj.setCustomer_id(credit.getCustomer_id());
        Long id = Long.valueOf(creditDao.createCredit(credit_obj));

        if(credit.getDebit_ids().size() > 1){
            if(total_amount > credit.getCredit_amount()){
                creditDao.deleteCredit(id);
                throw new Exception();
            }
            for(Long debit_id: debit_ids){
                debitDao.updateRemainingAmountOfDebit(debit_id, 0.0);
                Debit_Credit_Link link = new Debit_Credit_Link();
                link.setCredit_id(id);
                link.setDebit_id(debit_id);
                linkDao.createLink(link);
            }
            Customer customer = customerDao.getCustomer(credit.getCustomer_id());
            customerDao.updateCustomerLoanedAmount(credit.getCustomer_id(),customer.getLoaned_amount() - credit.getCredit_amount());
        }else{
            Debit debit = debitDao.getDebitById(credit.getDebit_ids().get(0));
            debitDao.updateRemainingAmountOfDebit(credit.getDebit_ids().get(0),debit.getRemaining_amount() - credit.getCredit_amount());
            Customer customer = customerDao.getCustomer(credit.getCustomer_id());
            customerDao.updateCustomerLoanedAmount(credit.getCustomer_id(),customer.getLoaned_amount() - credit.getCredit_amount());
            Debit_Credit_Link link = new Debit_Credit_Link();
            link.setCredit_id(id);
            link.setDebit_id(debit.getDebit_id());
            linkDao.createLink(link);
        }
        return id;
    }

    //function to clear all the debits := implement it.
    public int clearAllDues(Long id){ //removing debitId NOT NULL constraint
        List<Debit> allDueDebits = debitDao.getRemainingDebits(id);
        Double total_sum = 0.0;
        for(Debit debit : allDueDebits){
            total_sum += debit.getRemaining_amount();
            //also will need to check if their is enough balance in customer's bank. If transaction fails don't make changes.
            debitDao.updateRemainingAmountOfDebit(debit.getDebit_id(), 0.0);
        }
        Credit credit = new Credit();
        credit.setCredit_date(new Date());
        credit.setCredit_amount(total_sum);
        credit.setCustomer_id(id);
        int credit_id = creditDao.createCredit(credit);
        //making links
        for(Debit debit: allDueDebits){
            Debit_Credit_Link link = new Debit_Credit_Link();
            link.setDebit_id(debit.getDebit_id());
            link.setCredit_id((long) credit_id);
            linkDao.createLink(link);
        }
        return credit_id;
    }

    public List<Credit> getCustomerCredit(Long id){
        return creditDao.getCreditByCustomerId(id);
    }

}
