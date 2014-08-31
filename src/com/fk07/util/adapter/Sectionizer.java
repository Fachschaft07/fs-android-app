/*
 * Copyright (C) 2012 Mobs and Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fk07.util.adapter;

/**
 * Interface provides mechanism for supplying titles for instances based on the property they are compared against. The
 * parameterized type of the <b>Sectionizer</b> should be same as that of the {@link SimpleSectionAdapter}.
 * 
 * @author Ragunath Jawahar R <rj@mobsandgeeks.com>
 * @version 1.0
 */
public interface Sectionizer<I, O extends Comparable<O>> {

	/**
	 * @param instance
	 * @return
	 */
	O getSortForItem(I instance);

	/**
	 * Returns the title for the given instance from the data source.
	 * 
	 * @param instance
	 *            The instance obtained from the data source of the decorated list adapter.
	 * @return section title for the given instance.
	 */
	O getSectionTitleForItem(I instance);
}