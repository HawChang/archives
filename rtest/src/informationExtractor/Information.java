package informationExtractor;

import java.util.ArrayList;

public class Information {
	protected String originalInformation;
	public void setOriginalInformation(String originalInformation) {
		this.originalInformation = originalInformation;
	}
	public void setAttributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}
	//protected Map<String, String> informationMap;
	protected ArrayList<String> attributes;
	public String getOriginalInformation() {
		return originalInformation;
	}
	//public Map<String, String> getInformationMap() {
	//	return informationMap;
	//}
	public ArrayList<String> getAttributes() {
		return attributes;
	}
//	public Information(){
//		originalInformation=null;
//		//informationMap=new HashMap<String, String>();
//		attributes=new ArrayList<>();
//	}
//	//public Information(Map<String, String> m){
//	//	originalInformation=null;
//	//	informationMap=m;
//	//	attributes=new ArrayList<>(m.keySet());
//	//}
//	public Information(String o){
//		originalInformation=o;
//		//informationMap=m;
//		attributes=new ArrayList<>();
//	}
//	public Information(ArrayList<String> a){
//		originalInformation=null;
//		//informationMap=m;
//		attributes=a;
//	}
	public Information(ArrayList<String> a,String o){
		originalInformation=o;
		//informationMap=m;
		attributes=a;
	}
}
