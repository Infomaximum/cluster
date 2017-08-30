package com.infomaximum.cluster.component.database.datasource;

import com.infomaximum.cluster.component.database.remote.datasource.RControllerDatabaseDataSource;
import com.infomaximum.cluster.component.database.utils.DatabaseComponentUtil;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.rocksdb.core.datasource.DataSource;
import com.infomaximum.rocksdb.core.datasource.entitysource.EntitySource;
import com.infomaximum.rocksdb.shard.GlobalShardIdUtils;
import com.infomaximum.rocksdb.transaction.struct.modifier.Modifier;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Set;

/**
 * Created by user on 20.04.2017.
 */
public class DataSourceComponent implements DataSource {

    private Component role;

    public DataSourceComponent(Component role) {
        this.role = role;
    }

    @Override
    public long nextId(String columnFamily) throws RocksDBException {
        int shard = 0;
        String key = DatabaseComponentUtil.getKey(shard);

        RControllerDatabaseDataSource rControllerDatabaseDataSource = role.getRemotes().getFromSSKey(key, RControllerDatabaseDataSource.class);
        return rControllerDatabaseDataSource.nextId(columnFamily);
    }

    @Override
    public byte[] getField(String columnFamily, long id, String field) throws RocksDBException {
        int shard = 0;
        String key = DatabaseComponentUtil.getKey(shard);

        RControllerDatabaseDataSource rControllerDatabaseDataSource = role.getRemotes().getFromSSKey(key, RControllerDatabaseDataSource.class);
        return rControllerDatabaseDataSource.getField(columnFamily, id, field);
    }

    @Override
    public EntitySource getEntitySource(String columnFamily, boolean isTransaction, long id, Set<String> fields) throws RocksDBException {
        int shard = GlobalShardIdUtils.getShard(id);
        String key = DatabaseComponentUtil.getKey(shard);

        RControllerDatabaseDataSource rControllerDatabaseDataSource = role.getRemotes().getFromSSKey(key, RControllerDatabaseDataSource.class);
        return rControllerDatabaseDataSource.getEntitySource(columnFamily, isTransaction, id, fields);
    }

    @Override
    public EntitySource findNextEntitySource(String columnFamily, Long prevId, String index, int hash, Set<String> fields) throws RocksDBException {
        String key = DatabaseComponentUtil.getKey(0);

        RControllerDatabaseDataSource rControllerDatabaseDataSource = role.getRemotes().getFromSSKey(key, RControllerDatabaseDataSource.class);
        return rControllerDatabaseDataSource.findNextEntitySource(columnFamily, prevId, index, hash, fields);
    }

    @Override
    public EntitySource nextEntitySource(String columnFamily, Long prevId, Set<String> fields) throws RocksDBException {
        String key = DatabaseComponentUtil.getKey(0);

        RControllerDatabaseDataSource rControllerDatabaseDataSource = role.getRemotes().getFromSSKey(key, RControllerDatabaseDataSource.class);
        return rControllerDatabaseDataSource.nextEntitySource(columnFamily, prevId, fields);
    }

    @Override
    public void commit(List<Modifier> modifiers) throws RocksDBException {
        String key = DatabaseComponentUtil.getKey(0);

        RControllerDatabaseDataSource rControllerDatabaseDataSource = role.getRemotes().getFromSSKey(key, RControllerDatabaseDataSource.class);
        rControllerDatabaseDataSource.commit(modifiers);
    }

}
