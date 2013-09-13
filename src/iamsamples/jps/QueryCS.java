package iamsamples.jps;

import java.util.Iterator;
import java.util.Set;

import oracle.security.jps.JpsContext;
import oracle.security.jps.JpsContextFactory;
import oracle.security.jps.service.credstore.CredentialMap;
import oracle.security.jps.service.credstore.CredentialStore;

public class QueryCS {
	public static void main(String[] args) throws Exception {
		JpsContextFactory ctxFactory = JpsContextFactory.getContextFactory();
		JpsContext ctx = ctxFactory.getContext();

		CredentialStore store = ctx.getServiceInstance(CredentialStore.class);
		
		Set<String> maps = store.getMapNames();
		Iterator<String> it = maps.iterator();
		
		while (it.hasNext()) {
			String mapName = it.next();
			System.out.println(mapName);
			
			CredentialMap cm = store.getCredentialMap(mapName);
			Set<String> s = cm.keySet();
			
			Iterator<String> it1 = s.iterator();
			while (it1.hasNext()) {
				System.out.println(it1.next());
			}
		}
	}
}
