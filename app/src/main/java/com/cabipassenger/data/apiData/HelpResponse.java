package com.cabipassenger.data.apiData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 7/11/16.
 * Contains data for help response api
 */
public class HelpResponse {


        public String message;
        public List<Detail> details = new ArrayList<Detail>();
        public Integer status;



    public class Detail {

        public String help_id;
        public String help_content;

    }


}
