package com.fk07.backend.web.data.builder;

/**
 * @author Fabio
 *
 * @param <T>
 */
public interface IFilter<T> {
	boolean apply(T data);
}
