package org.isf.commons.mvc;

public interface IMasterDetailController<T, K> extends ICRUDController<T>,
		IListProvider<K> {

}
