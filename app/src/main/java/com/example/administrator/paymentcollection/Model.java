package com.example.administrator.paymentcollection;

public class Model {

    private String lDate;
    private String lDescription;
    private String lDebit;
    private String lCredit;

    public Model(String lDate, String lDescription, String lDebit, String lCredit) {
        this.lDate = lDate;
        this.lDescription = lDescription;
        this.lDebit = lDebit;
        this.lCredit = lCredit;
    }

    public Model() {

    }

    public String getsNo() {
        return lDate;
    }

    public String getProduct() {
        return lDescription;
    }

    public String getCategory() {
        return lDebit;
    }

    public String getPrice() {
        return lCredit;
    }

    public String encoding_phone(String usermobile) {

        StringBuffer final_result = new StringBuffer();
        StringBuffer temp = new StringBuffer();
        StringBuffer temp2 = new StringBuffer();

        char uchar1, uchar2;
        int uascii1, uascii2, usum_val;

        final_result.setLength(0);
        temp.setLength(0);
        temp2.setLength(0);

        for (int i = 0; i <= 9; i++) {

            uchar1 = usermobile.charAt(i); // This gives the character 'a'
            uascii1 = (int) uchar1;
            if (i == 9) {
                uchar2 = 77;
                uascii2 = (int) uchar2; // ascii is now 97.
            } else {
                uchar2 = usermobile.charAt(i + 1);
                uascii2 = (int) uchar2; // ascii is now 97.
            }

            usum_val = (uascii1 + uascii2) - 28;

            char c = (char) usum_val;

            final_result.append(String.valueOf(c));

        }
        return final_result.toString();
    }
}
