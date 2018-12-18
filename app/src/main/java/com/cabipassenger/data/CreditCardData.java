package com.cabipassenger.data;

/**
 * Getter/setter class to hold the card details.
 */
//
public class CreditCardData {
    public String id, type, month, year, card, cvv, default_card, original_cardno, original_cvv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public CreditCardData(String _id, String _type, String _month, String _year, String _card, String _cvv, String _default_card, String _original_cardno, String _original_cvv) {
        id = _id;
        type = _type;
        month = _month;
        year = _year;
        card = _card;
        cvv = _cvv;
        default_card = _default_card;
        original_cardno = _original_cardno;
        original_cvv = _original_cvv;
    }

    public String getOriginal_cardno() {
        return original_cardno;
    }

    public void setOriginal_cardno(String original_cardno) {
        this.original_cardno = original_cardno;
    }

    public String getOriginal_cvv() {
        return original_cvv;
    }

    public void setOriginal_cvv(String original_cvv) {
        this.original_cvv = original_cvv;
    }

    public String getDefault_card() {
        return default_card;
    }

    public void setDefault_card(String default_card) {
        this.default_card = default_card;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
