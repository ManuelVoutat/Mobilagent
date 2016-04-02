package jus.aor.mobilagent.lookforhotel;


import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jus.aor.mobilagent.kernel._Service;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Chaine implements _Service<List<Hotel>>{

	//Declaration des attributs nécessaires
	public List<Hotel> listHotel;
	
	/**
	 * @param args : 0 -> file du ficher xml décrivant la liste d'hotel
	 */
	public Chaine(Object... args){
		listHotel = new LinkedList<Hotel>();
		
		/* récupération des hôtels de la chaîne dans le fichier xml passé en 1er argument */
		DocumentBuilder docBuilder = null;
		Document doc=null;
		String file = (String) args[0];
		try{
		docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		System.out.println("Chaine : Opening file " + file);
		doc = docBuilder.parse(new File(file));
		}
		catch(Exception e){
			System.out.println("Chaine l36");
			System.out.println(e);
		}
		String name, localisation;
		NodeList list = doc.getElementsByTagName("Hotel");
		NamedNodeMap attrs;
		/* acquisition de toutes les entrées de la base d'hôtels */
		
		for(int i =0; i<list.getLength();i++) {
			attrs = list.item(i).getAttributes();
			name=attrs.getNamedItem("name").getNodeValue();
			localisation=attrs.getNamedItem("localisation").getNodeValue();
			listHotel.add(new Hotel(name,localisation));
			
//			System.out.println("CHAINE : name : "+name+" localosation "+localisation);
		}
	}

	@Override
	public String getServiceName() {
		return "Hotels";
	}

	private List<Hotel> get(String localisation) {
		//Creation d'un iterateur pour notre hashmap
		Iterator<Hotel> it = listHotel.iterator();
		
		//Creation de la liste que l'on va renvoyer
		LinkedList<Hotel> res = new LinkedList<Hotel>();
		
		while (it.hasNext())
		{
			Hotel h = it.next();
			if(h.localisation.compareTo(localisation)==0){
				res.add(h);
			}
				
		}
		
		return res;
	}

	 /**
	  * @params : 0 -> String : localisation
	  */
	public List<Hotel> call(Object... params)
  			throws IllegalArgumentException {
  		return get((String)params[0]);
  	}

}