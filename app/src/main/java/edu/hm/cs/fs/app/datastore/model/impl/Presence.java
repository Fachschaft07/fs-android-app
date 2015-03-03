package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.List;

/**
 * Created by Fabio on 18.02.2015.
 */
public class Presence {
	private final String nickName;
	private final String status;

	public Presence(final String nickName, final String status) {
		this.nickName = nickName;
		this.status = status;
	}

	public String getName() {
		return nickName;
	}

	public boolean isBusy() {
		return "Busy".equalsIgnoreCase(status);
	}

	public static boolean isPresent(List<Presence> presences) {
		boolean present = false;
		for (Presence presence : presences) {
			if (!presence.isBusy()) {
				present = true;
			}
		}
		return present;
	}
}
