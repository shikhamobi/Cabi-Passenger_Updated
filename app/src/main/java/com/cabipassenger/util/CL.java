package com.cabipassenger.util;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class is used to store the color files from api
 */
public class CL {
    Context context;
    public static HashMap<Integer,String> nfields_byID =new HashMap<>();
    public static HashMap<String,String> nfields_byName=new HashMap<>();


    public static ArrayList<String> fields=new ArrayList<>();
    public static ArrayList<String> fields_value=new ArrayList<>();

    public static HashMap<String,Integer> fields_id =new HashMap<>();


    static CL CL =null;
    private static String hexColor;

    static CL getInstance(){
        if(CL ==null)
            CL =new CL();
        else
            CL = CL;
        return CL;
    }
    public static CL getResources(){
        return  getInstance();
    }
    public static CL getActivity(){
        return  getInstance();
    }

    public static int getColor(int c,Context context){
        String text="";
        if(fields_value.contains(text)){

        }
//        if(c>1000000)
//             hexColor = String.format("#%06X", (0xFFFFFF & c));

        if(nfields_byID.get(c)!=null){
          //  System.out.println("notnulllccc"+c+"____"+nfields_byID.size());
        return Color.parseColor(nfields_byID.get(c));}
        else{
           // System.out.println("nulllccc"+c+"____"+nfields_byID.size());
            return Color.WHITE;
        }
    }
    public static int getColor(Context context, int c){
        String text="";
        CL.context=context;
        if(fields_value.contains(text)){

        }
//        if(c>1000000)
//             hexColor = String.format("#%06X", (0xFFFFFF & c));

        if(nfields_byID.get(c)!=null){
            //  System.out.println("notnulllccc"+c+"____"+nfields_byID.size());
            return Color.parseColor(nfields_byID.get(c));}
        else{
            // System.out.println("nulllccc"+c+"____"+nfields_byID.size());
         //   System.out.println("nulllccc"+c+"____"+nfields_byID.size()+c);
            ColorRestore.getAndStoreColorValues(SessionSave.getSession("wholekeyColor", context),context);
//            if(MainActivity.context!=null && MainActivity.context.getResources().getColor(Integer.parseInt(nfields_byID.get(c)))!=-1)
//                return  MainActivity.context.getResources().getColor(Integer.parseInt(nfields_byID.get(c)));
//            else
            if(nfields_byID.get(c)==null)
                return context.getResources().getColor(c);
            else
            return Color.parseColor(nfields_byID.get(c));
        }
    }
}
