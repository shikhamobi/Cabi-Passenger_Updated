package com.cabipassenger.interfaces;

import org.json.JSONObject;

/**
 * This interface used to get back the API call result and process the response as per the page need.
 */
//
public interface APIResult_JSON
{
	void getResult(boolean isSuccess, JSONObject result);
}
