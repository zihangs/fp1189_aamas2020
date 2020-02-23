import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Set;

import org.jbpt.petri.*;
import org.jbpt.petri.io.PNMLSerializer;

public class PEPreader {
	
	public static void read()throws Exception{
		File file = new File("C:\\Users\\suzih\\OneDrive\\desktop\\p55b.net");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		
		NetSystem ns = new NetSystem();
		
		String Flag = "None";
		int p_count = 0;
		int t_count = 0;
		
		while ((st = br.readLine()) != null) {
			//change the Flag
			if (st.contentEquals("PL")) {
				Flag = "PL";
				continue;
			}
			
			if (st.contentEquals("TR")) {
				Flag = "TR";
				continue;
			}
			
			if (st.contentEquals("TP")) {
				Flag = "TP";
				continue;
			}
			
			if (st.contentEquals("PT")) {
				Flag = "PT";
				continue;
			}
			
			
			// process each line of input
			if (Flag == "PL") {
				p_count += 1;
				String[] res = st.split("\"");
				
				Place place = new Place();
				place.setName("p" + p_count);
				//place.setName(res[0]);
				
				place.setLabel(res[1]);
				place.setId(res[0]);
				
				ns.addPlace(place);
			}
			
			if (Flag == "TR") {
				//System.out.println(st);
				t_count += 1;
				String[] res = st.split("\"");
				
				Transition transition = new Transition();
				transition.setName("t" + t_count);
				//transition.setName(res[0]);
				
				transition.setLabel(res[1]);
				transition.setId(res[0]);
				
				ns.addTransition(transition);
			}
			
			if (Flag == "TP") {
				
				String[] res = st.split("<");
				
				Transition transition = null;
				Place place = null;
				
				Set<Transition> t_set = ns.getTransitions();
				Iterator<Transition> t_iter = t_set.iterator();
				Set<Place> p_set = ns.getPlaces();
				Iterator<Place> p_iter = p_set.iterator();
				
				while(t_iter.hasNext()){
					transition = t_iter.next();
					if(transition.getId().contentEquals(res[0]) ) {
						break;
					}
				}
				
				while(p_iter.hasNext()){
					place = p_iter.next();
					if(place.getId().contentEquals(res[1])) {
						break;
					}
				}
				
				ns.addFlow(transition, place);
			}
			
			if (Flag == "PT") {
				String[] res = st.split("<");
				
				Transition transition = null;
				Place place = null;
				
				Set<Transition> t_set = ns.getTransitions();
				Iterator<Transition> t_iter = t_set.iterator();
				Set<Place> p_set = ns.getPlaces();
				Iterator<Place> p_iter = p_set.iterator();
				
				while(t_iter.hasNext()){
					transition = t_iter.next();
					if(transition.getId().contentEquals(res[1]) ) {
						break;
					}
				}
				
				while(p_iter.hasNext()){
					place = p_iter.next();
					if(place.getId().contentEquals(res[0])) {
						break;
					}
				}
				ns.addFlow(place, transition);
			}
		}
		
		br.close();
		
		System.out.println(ns.toDOT());
		
		
		String pnmlString = PNMLSerializer.serializePetriNet(ns, 1);
		
		System.out.println(pnmlString);
		System.out.println(ns);
	}
}
