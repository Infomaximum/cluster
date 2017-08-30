package com.infomaximum.cluster.component.database.remote.datasource;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.rocksdb.core.datasource.entitysource.EntitySource;
import com.infomaximum.rocksdb.transaction.struct.modifier.Modifier;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Set;

/**
 * Created by kris on 02.11.16.
 */
public interface RControllerDatabaseDataSource extends RController {

	public long nextId(String columnFamily) throws RocksDBException;

	public byte[] getField(String columnFamily, long id, String field) throws RocksDBException;

	public EntitySource getEntitySource(String columnFamily, boolean isTransaction, long id, Set<String> fields) throws RocksDBException;

	public EntitySource findNextEntitySource(String columnFamily, Long prevId, String index, int hash, Set<String> fields) throws RocksDBException;

	public EntitySource nextEntitySource(String columnFamily, Long prevId, Set<String> fields) throws RocksDBException;

	public void commit(List<Modifier> modifiers) throws RocksDBException;

}


