package com.schonherz.dbentities;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.SqlUtils;
import de.greenrobot.dao.Query;
import de.greenrobot.dao.QueryBuilder;

import com.schonherz.dbentities.PartnerKep;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Partnerkepek.
*/
public class PartnerKepDao extends AbstractDao<PartnerKep, Long> {

    public static final String TABLENAME = "Partnerkepek";

    /**
     * Properties of entity PartnerKep.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PartnerKepID = new Property(0, Long.class, "partnerKepID", true, "PARTNER_KEP_ID");
        public final static Property PartnerNev = new Property(1, String.class, "partnerNev", false, "PARTNER_NEV");
        public final static Property PartnerKepDate = new Property(2, String.class, "partnerKepDate", false, "PARTNER_KEP_DATE");
        public final static Property PartnerKepIsUploaded = new Property(3, Boolean.class, "partnerKepIsUploaded", false, "PARTNER_KEP_IS_UPLOADED");
        public final static Property PartnerID = new Property(4, Long.class, "partnerID", false, "PARTNER_ID");
    };

    private DaoSession daoSession;

    private Query<PartnerKep> partner_PartnerKepListQuery;

    public PartnerKepDao(DaoConfig config) {
        super(config);
    }
    
    public PartnerKepDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Partnerkepek' (" + //
                "'PARTNER_KEP_ID' INTEGER PRIMARY KEY ," + // 0: partnerKepID
                "'PARTNER_NEV' TEXT," + // 1: partnerNev
                "'PARTNER_KEP_DATE' TEXT," + // 2: partnerKepDate
                "'PARTNER_KEP_IS_UPLOADED' INTEGER," + // 3: partnerKepIsUploaded
                "'PARTNER_ID' INTEGER);"); // 4: partnerID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Partnerkepek'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PartnerKep entity) {
        stmt.clearBindings();
 
        Long partnerKepID = entity.getPartnerKepID();
        if (partnerKepID != null) {
            stmt.bindLong(1, partnerKepID);
        }
 
        String partnerNev = entity.getPartnerNev();
        if (partnerNev != null) {
            stmt.bindString(2, partnerNev);
        }
 
        String partnerKepDate = entity.getPartnerKepDate();
        if (partnerKepDate != null) {
            stmt.bindString(3, partnerKepDate);
        }
 
        Boolean partnerKepIsUploaded = entity.getPartnerKepIsUploaded();
        if (partnerKepIsUploaded != null) {
            stmt.bindLong(4, partnerKepIsUploaded ? 1l: 0l);
        }
 
        Long partnerID = entity.getPartnerID();
        if (partnerID != null) {
            stmt.bindLong(5, partnerID);
        }
    }

    @Override
    protected void attachEntity(PartnerKep entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PartnerKep readEntity(Cursor cursor, int offset) {
        PartnerKep entity = new PartnerKep( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // partnerKepID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // partnerNev
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // partnerKepDate
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // partnerKepIsUploaded
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // partnerID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PartnerKep entity, int offset) {
        entity.setPartnerKepID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPartnerNev(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPartnerKepDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPartnerKepIsUploaded(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setPartnerID(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PartnerKep entity, long rowId) {
        entity.setPartnerKepID(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PartnerKep entity) {
        if(entity != null) {
            return entity.getPartnerKepID();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "partnerKepList" to-many relationship of Partner. */
    public synchronized List<PartnerKep> _queryPartner_PartnerKepList(Long partnerID) {
        if (partner_PartnerKepListQuery == null) {
            QueryBuilder<PartnerKep> queryBuilder = queryBuilder();
            queryBuilder.where(Properties.PartnerID.eq(partnerID));
            partner_PartnerKepListQuery = queryBuilder.build();
        } else {
            partner_PartnerKepListQuery.setParameter(0, partnerID);
        }
        return partner_PartnerKepListQuery.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getPartnerDao().getAllColumns());
            builder.append(" FROM Partnerkepek T");
            builder.append(" LEFT JOIN Partnerek T0 ON T.'PARTNER_ID'=T0.'PARTNER_ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PartnerKep loadCurrentDeep(Cursor cursor, boolean lock) {
        PartnerKep entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Partner partner = loadCurrentOther(daoSession.getPartnerDao(), cursor, offset);
        entity.setPartner(partner);

        return entity;    
    }

    public PartnerKep loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<PartnerKep> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PartnerKep> list = new ArrayList<PartnerKep>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<PartnerKep> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PartnerKep> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
