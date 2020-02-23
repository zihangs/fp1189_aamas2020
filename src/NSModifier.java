import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.jbpt.petri.NetSystem;
import org.jbpt.petri.Place;
import org.jbpt.petri.Transition;

public class NSModifier {
	public NSModifier() {
	}
	
	public NetSystem AddGoal(NetSystem ns, ArrayList<String> placeName) {
		
		Set<Place> nsPlaces = ns.getPlaces();
		Iterator<Place> iter = nsPlaces.iterator();
		
		Transition tg = new Transition("tg");
		Place pg = new Place("pg");
		
		ns.addTransition(tg);
		ns.addPlace(pg);
		
		while(iter.hasNext()){
			Place p = iter.next();
			
			if(placeName.contains(p.getName())) {
				ns.addFlow(p, tg);
			}
		}
		
		ns.addFlow(tg, pg);
		
		return ns;
		
	}

}
