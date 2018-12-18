package com.cabipassenger.data.apiData;

/**
 * Created by developer on 1/11/16.
 * Contains request data for all api calling
 */
public class ApiRequestData {

    public static class UpcomingRequest {
        public void setId(String id) {
            this.id = id;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public void setDeviceType(String deviceType) {
            this.device_type = deviceType;
        }

        public String id;
        public String start;
        public String limit;
        public String device_type;
    }

    public static class PastBookingRequest {
        public String passenger_id;
        public String start;
        public String limit;
        public String month;
        public String year;
        public String device_type;

        public void setPassenger_id(String passenger_id) {
            this.passenger_id = passenger_id;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }
    }

    public static class getTripDetailRequest {
        public void setTrip_id(String trip_id) {
            this.trip_id = trip_id;
        }

        public void setPassenger_id(String passenger_id) {
            this.passenger_id = passenger_id;
        }

        public String trip_id,passenger_id;
    }


    public static class HelpSubmit {
        public void setTrip_id(String trip_id) {
            this.trip_id = trip_id;
        }

        public void setHelp_id(String help_id) {
            this.help_id = help_id;
        }

        public void setHelp_comment(String help_comment) {
            this.help_comment = help_comment;
        }

        String trip_id, help_id, help_comment;
    }

    public static class BaseUrl{
        public String company_main_domain,company_domain,device_type;
    }
}
