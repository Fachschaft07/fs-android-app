package com.fk07.backend.data;

import com.fk07.R;

/**
 * The Room stores the data for a room with type, name and the time the room stays free.
 * 
 * @author Fabio
 * @version 2
 */
public class Room {
	private final Type type;
	private final String name;
	private final String freeUntilTime;

	/**
	 * Creates a new room.
	 * 
	 * @param type
	 *            of the room.
	 * @param name
	 *            of the room.
	 * @param freeUntilTime
	 *            the room stays free.
	 */
	public Room(final Type type, final String name, final String freeUntilTime) {
		this.type = type;
		this.name = name;
		this.freeUntilTime = freeUntilTime;
	}

	/**
	 * Get the type of the room.
	 * 
	 * @return the type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Get the name of the room.
	 * 
	 * @return the room.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the time the room stays free unti the time is reached.
	 * 
	 * @return the free time.
	 */
	public String getFreeTime() {
		return freeUntilTime;
	}

	/**
	 * @author Fabio
	 * 
	 */
	public enum Type {
		LABORATORY(R.string.laboratory), HALL(R.string.hall);

		private final int stringId;

		private Type(final int stringId) {
			this.stringId = stringId;
		}

		/**
		 * @return
		 */
		public int getNameId() {
			return stringId;
		}
	}
}
