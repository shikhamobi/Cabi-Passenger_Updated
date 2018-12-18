package com.cabipassenger.data.apiData;

import java.util.ArrayList;

/**
 * Created by developer on 18/8/17.
 */

public class PromoDataList {
    public  class PromoData{
        private String message;
        private String status;

        public long getExpiry_date() {
            return expiry_date;
        }

        public void setExpiry_date(long expiry_date) {
            this.expiry_date = expiry_date;
        }

        private long expiry_date;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public PromoData getPromoData(){
       return new PromoData();
    }

   public ArrayList<PromoData> promoDatas=new ArrayList<>();
}
