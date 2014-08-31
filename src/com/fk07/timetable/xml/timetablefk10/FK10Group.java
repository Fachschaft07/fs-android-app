package com.fk07.timetable.xml.timetablefk10;

public class FK10Group {
	private final String groupName;
	private final String groupId;

	public FK10Group(final String groupName, final String groupId) {
		super();
		this.groupName = groupName;
		this.groupId = groupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public String getGroupId() {
		return this.groupId;
	}
}
