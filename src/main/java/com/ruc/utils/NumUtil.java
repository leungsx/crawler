package com.ruc.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumUtil
{

	public static long str2long(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }


        try {
            return Long.parseLong(str.trim());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("parse str{} to long error! return defaultValue: {}", str, defaultValue);
            }
            return defaultValue;
        }
    }
	
	public static int str2int(String str, int defaultValue){
		if(str == null){
			return defaultValue;
		}
		
		try
		{
			return Integer.parseInt(str.trim());
			
		} catch (NumberFormatException e)
		{
			if(log.isDebugEnabled()){
				log.debug("parse str{} to int error! return defaultValue:{}",str,defaultValue);
			}
			return defaultValue;
		}
	}
	
	
	
}
