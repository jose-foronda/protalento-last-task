package com.protalento.jdbc;

import java.util.List;

public interface DAO<E, K> {
	
	public abstract E findById(K k);
	
	public abstract Boolean insert(E e);
	
	public abstract Boolean update(E e);
	
	public abstract Boolean save(E e);
	
	public abstract Boolean delete(E e);
	
	public abstract List<E> listAll();
}
