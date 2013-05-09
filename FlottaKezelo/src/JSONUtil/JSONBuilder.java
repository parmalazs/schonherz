package JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.schonherz.dbentities.Sofor;

public class JSONBuilder {
	private static String URL = "http://www.flotta.host-ed.me/index.php"; 
	
	public JSONObject insertSofor(Sofor s){
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("soforID", s.getSoforID());
			object.put("soforNev", s.getSoforNev());
			
			object.put("soforCim", s.getSoforCim());
			object.put("soforTelefonszam", s.getSoforTelefonszam());
			object.put("soforLogin", s.getSoforLogin());
			object.put("soforPass", s.getSoforPass());
			object.put("soforBirthDate", s.getSoforBirthDate());
			object.put("soforRegTime", s.getSoforRegTime());
			if(s.getSoforIsAdmin()){
				object.put("soforIsAdmin", "1");
			}else{
				object.put("soforIsAdmin", "0");
			}
			object.put("soforEmail", s.getSoforEmail());
			object.put("soforProfileKepId", s.getSoforProfilKepID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action","insert");
			ret.put("tableName", "sofor");
			ret.put("objects",object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
//	public JSONObject insert(Object o) throws JSONException{
//		Field[] fields = o.getClass().getDeclaredFields();
//		JSONObject ret = new JSONObject();
//		SoforDao sd = new SoforDao();
//		sd.getPkColumns();
//		for (Field field : fields) {
//            //System.out.println("Field name = " + field.getName());
//            //System.out.println("Field type = " + field.getType().getName());
//            ret.put(field.getName(), field.getType());
//        }
//		ret.put("FieldNum:"," "+fields.length);
//		ret.put("Object neve:", o.getClass());
//		return ret;
//	}
	
}
