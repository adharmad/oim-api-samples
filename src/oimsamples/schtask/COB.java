package oimsamples.schtask;

import java.util.HashMap;
import java.util.Random;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.scheduler.tasks.SchedulerBaseTask;

public class COB extends SchedulerBaseTask {

	public void execute() {
		try {
			Random r = new Random(System.currentTimeMillis());
			tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)getUtility("Thor.API.Operations.tcUserOperationsIntf");
			
			HashMap hm = new HashMap();
			hm.put("Users.User ID", "COB1");
			//tcResultSet usrRS = usrIntf.findUsersFiltered(hm, new String[] {"Users.Key", "Users.User ID", "Users.Row Version"});
			tcResultSet usrRS = usrIntf.findUsers(hm);
			
			
			HashMap updHm = new HashMap();
			updHm.put("Users.First Name", "hello_" + r.nextInt(1000));
			
			System.out.println("before updating");
			usrIntf.updateUser(usrRS, updHm);
			System.out.println("after updating");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
