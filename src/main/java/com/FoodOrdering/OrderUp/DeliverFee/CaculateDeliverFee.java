package com.FoodOrdering.OrderUp.DeliverFee;

import com.FoodOrdering.OrderUp.Controller.AdminController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaculateDeliverFee {
   public   Logger log = LoggerFactory.getLogger(CaculateDeliverFee.class);
    private final Cache<Integer, List<GetDistrictRes>> districtCache = Caffeine.newBuilder().build();
    private static List<GetProvinceRes> cachedProvince;
    private static final String API_TOKEN = "7f737d52-e33a-11ed-8bba-de4829400020";
    private static final String SHOP_ID = "4068758";
    public  double caculate(String from, String to){
        double fee = 0;


        String[] aFrom = from.split(",");
        String[] aTo = to.split(",");

        String fromDistrict = aFrom[aFrom.length-3].trim();
        String toDistrict  = aTo[aTo.length-3].trim();
        String fromProvince = aFrom[aFrom.length-2].trim();
        String toProvince = aTo[aTo.length-2].trim();

        log.info("==============> fromDistrict: "+fromDistrict );
        log.info("==============> toDistrict: "+toDistrict );
        log.info("==============> fromProvince: "+fromProvince );
        log.info("==============> toProvince: "+toProvince );


        int fd = getCodeByName(fromDistrict,fromProvince);
        int td  = getCodeByName(toDistrict,toProvince);
        int fromDistrictCode=fd == 0? 2270:fd;
        int toDistrictCode =td == 0 ? 3284:td;

        log.info("================>fromDistrictCode:  "+ fromDistrictCode);
        log.info("================>toDistrictCode:  "+ toDistrictCode);


        // create payload
        String payload ;
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        int serviceID = getService(fromDistrictCode,toDistrictCode);
        try{
            jsonWriter.beginObject();
            jsonWriter.name("from_district_id").value(fromDistrictCode);
            jsonWriter.name("service_id").value(serviceID);
            jsonWriter.name("to_district_id").value(toDistrictCode);
            jsonWriter.name("weight").value(2);
            jsonWriter.endObject();
        }catch (Exception e){

        }
        payload =  stringWriter.toString();

        log.info("=================> payload: "+ payload);
        String url ="https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
        //set header
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("token", API_TOKEN);
        header.put("shopId",SHOP_ID);
        header.put("Content-Type", "application/json");

        String response = postrequestData(url, payload, header);

        Document document = new Document();

        Gson gson= new Gson();
        CaculateFeeRes caculateFeeRes = gson.fromJson(response, CaculateFeeRes.class);

//        log.info("=====================> caculateFeeRes: "+ caculateFeeRes);





        return  caculateFeeRes.getData().getTotal();
    }


    public int getService(int from, int to){
        // create payload
        String payload ;
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        try{
            jsonWriter.beginObject();
            jsonWriter.name("shop_id").value(885);
            jsonWriter.name("from_district").value(from);
            jsonWriter.name("to_district").value(to);
            jsonWriter.endObject();
        }catch (Exception e){

        }
        payload =  stringWriter.toString();

        log.info("=================> payload: "+ payload);
        String url ="https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services";
        //set header
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("token", API_TOKEN);
        header.put("Content-Type", "application/json");

        String response = postrequestData(url, payload, header);

        Document document = new Document();

        Gson gson= new Gson();
        GetServiceRes getServiceRes = gson.fromJson(response,GetServiceRes.class);
        log.info("=====================> document: "+ document);
        log.info("==================> get: "+ getServiceRes);
        return getServiceRes.getData().get(0).getService_id();
    }

    public int getCodeByName( String fromDistrict,String fromProvince){

        List<GetProvinceRes> provinceRes = getProvince();
        List<GetDistrictRes> districtRes = getCachedDistrict(0);

        GetDistrictRes getDistrictRes = getDistrictResByExtension(districtRes,fromDistrict);
        if(getDistrictRes == null){
            GetProvinceRes getProvinceRes = getProvinceResResByExtension(provinceRes,fromProvince);
            if(getProvinceRes != null){
                int province = getProvinceRes.getProvinceID();
                log.info("===============> province: "+ province);
                List<GetDistrictRes> districtRes1 = getCachedDistrict(province);

                for (GetDistrictRes getDistrictRes1: districtRes1){
                    if(getDistrictRes1.getProvinceID() == province){
                        return getDistrictRes1.getDistrictID();
                    }
                }
            }

        }
        else {
            return getDistrictRes.getDistrictID();
        }

        return 0;
    }


    public GetDistrictRes getDistrictResByExtension(List<GetDistrictRes> districtResList, String extension) {
        for (GetDistrictRes districtRes : districtResList) {
            List<String> nameEx = districtRes.getNameExtension();
            if(nameEx == null)
                continue;;
            if (nameEx.contains(extension)) {
                return districtRes;
            }
        }
        return null;
    }

    public GetProvinceRes getProvinceResResByExtension(List<GetProvinceRes> provinceResList, String extension) {
        for (GetProvinceRes provinceRes : provinceResList) {
            if (provinceRes.getNameExtension().contains(extension)) {
                return provinceRes;
            }
        }
        return null;
    }




    private List<GetProvinceRes> getProvince() {
        if (cachedProvince == null) {
            String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
            //set header
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("token", API_TOKEN);

            String response = postrequestData(url, "", header);

            Gson gson = new Gson();
            ProvinceResponse provinceResponse = gson.fromJson(response, ProvinceResponse.class);
            List<GetProvinceRes> provinces = new ArrayList<>();
            for (ProvinceData data : provinceResponse.getData()) {
                GetProvinceRes province = new GetProvinceRes();
                province.setProvinceID(data.getProvinceID());
                province.setNameExtension(data.getNameExtension());
                provinces.add(province);
            }
//      log.info("==============> province: "+ provinces);

            cachedProvince = provinces;
        }

        return cachedProvince;
    }


    private List<GetDistrictRes> getCachedDistrict(int provinceCode) {
        return districtCache.get(provinceCode, this::getDistrict);
    }

    private List<GetDistrictRes> getDistrict(int provinceCode)  {

        String url ="https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
        //set header
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("token", API_TOKEN);
        String payload ;
        if(provinceCode !=0){
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);

            try{
                jsonWriter.beginObject();
                jsonWriter.name("province_id").value(provinceCode);

                jsonWriter.endObject();
            }catch (Exception e){

            }
            payload =  stringWriter.toString();
        }
        else {
            payload = "";
        }
        String response = postrequestData(url, payload, header);


        Gson gson = new Gson();
        Data data = gson.fromJson(response, Data.class);
        List<GetDistrictRes> list = new ArrayList<>();
        for (Data.District district : data.data) {
            GetDistrictRes res = new GetDistrictRes();
            res.setDistrictID(district.DistrictID);
            res.setNameExtension(district.NameExtension);
            res.setProvinceID(district.ProvinceID);
            list.add(res);
        }
//        log.info("==============> result: "+ list);


        return list;
    }

    static class Data {
        int code;
        String message;
        District[] data;

        static class District {
            int DistrictID;
            int ProvinceID;
            String DistrictName;
            String Code;
            int Type;
            int SupportType;
            List<String> NameExtension;
            int IsEnable;
            int UpdatedBy;
            String CreatedAt;
            String UpdatedAt;
            boolean CanUpdateCOD;
            int Status;
            int PickType;
            int DeliverType;
            WhiteListClient WhiteListClient;
            WhiteListDistrict WhiteListDistrict;
            String ReasonCode;
            String ReasonMessage;
            Object OnDates;
            String UpdatedDate;

            static class WhiteListClient {
                Object From;
                Object To;
                Object Return;
            }

            static class WhiteListDistrict {
                Object From;
                Object To;
            }
        }
    }


    private String postrequestData(String url, String payload, HashMap<String, String> headers) {

        HttpURLConnection con = null;
        BufferedReader in = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setConnectTimeout(60000); // 60 seconds
            con.setReadTimeout(60000); // 60 seconds
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    con.setRequestProperty(key, headers.get(key));
                }
            }
            OutputStream os = con.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();
            int responseCode = con.getResponseCode();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }


            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args)  {

//        CaculateDeliverFee.caculate("M7F9+CP Hữu Lũng, Lạng Sơn, Việt Nam","Handico Tower, Phạm Hùng, Từ Liêm, Hà Nội, Việt Nam");

        CaculateDeliverFee caculateDeliverFee= new CaculateDeliverFee();
        caculateDeliverFee.caculate("Nam Sơn, Tp. Bắc Ninh, Bắc Ninh, Việt Nam","Cẩm Vũ, Cẩm Giàng, Hải Dương, Việt Nam");
    }
}
