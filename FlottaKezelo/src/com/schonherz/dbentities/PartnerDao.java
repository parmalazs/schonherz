package com.schonherz.dbentities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import com.schonherz.dbentities.Partner;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Partnerek.
*/
public class PartnerDao extends AbstractDao<Partner, Long> {

    public static final String TABLENAME = "Partnerek";

    /**
     * Properties of entity Partner.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PartnerID = new Property(0, Long.class, "partnerID", true, "PARTNER_ID");
        public final static Property PartnerNev = new Property(1, String.class, "partnerNev", false, "PARTNER_NEV");
        public final static Property PartnerCim = new Property(2, String.class, "partnerCim", false, "PARTNER_CIM");
        public final static Property PartnerXkoordinata = new Property(3, Float.class, "partnerXkoordinata", false, "PARTNER_XKOORDINATA");
        public final static Property PartnerYkoodinata = new Property(4, Float.class, "partnerYkoodinata", false, "PARTNER_YKOODINATA");
        public final static Property PartnerTelefonszam = new Property(5, String.class, "partnerTelefonszam", false, "PARTNER_TELEFONSZAM");
        public final static Property PartnerWeboldal = new Property(6, String.class, "partnerWeboldal", false, "PARTNER_WEBOLDAL");
        public final static Property PartnerEmailcim = new Property(7, String.class, "partnerEmailcim", false, "PARTNER_EMAILCIM");
        public final static Property PartnerIsActive = new Property(8, Boolean.class, "partnerIsActive", false, "PARTNER_IS_ACTIVE");
    };

    private DaoSession daoSession;


    public PartnerDao(DaoConfig config) {
        super(config);
    }
    
    public PartnerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Partnerek' (" + //
                "'PARTNER_ID' INTEGER PRIMARY KEY ," + // 0: partnerID
                "'PARTNER_NEV' TEXT," + // 1: partnerNev
                "'PARTNER_CIM' TEXT," + // 2: partnerCim
                "'PARTNER_XKOORDINATA' REAL," + // 3: partnerXkoordinata
                "'PARTNER_YKOODINATA' REAL," + // 4: partnerYkoodinata
                "'PARTNER_TELEFONSZAM' TEXT," + // 5: partnerTelefonszam
                "'PARTNER_WEBOLDAL' TEXT," + // 6: partnerWeboldal
                "'PARTNER_EMAILCIM' TEXT," + // 7: partnerEmailcim
                "'PARTNER_IS_ACTIVE' INTEGER);"); // 8: partnerIsActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Partnerek'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Partner entity) {
        stmt.clearBindings();
 
        Long partnerID = entity.getPartnerID();
        if (partnerID != null) {
            stmt.bindLong(1, partnerID);
        }
 
        String partnerNev = entity.getPartnerNev();
        if (partnerNev != null) {
            stmt.bindString(2, partnerNev);
        }
 
        String partnerCim = entity.getPartnerCim();
        if (partnerCim != null) {
            stmt.bindString(3, partnerCim);
        }
 
        Float partnerXkoordinata = entity.getPartnerXkoordinata();
        if (partnerXkoordinata != null) {
            stmt.bindDouble(4, partnerXkoordinata);
        }
 
        Float partnerYkoodinata = entity.getPartnerYkoodinata();
        if (partnerYkoodinata != null) {
            stmt.bindDouble(5, partnerYkoodinata);
        }
 
        String partnerTelefonszam = entity.getPartnerTelefonszam();
        if (partnerTelefonszam != null) {
            stmt.bindString(6, partnerTelefonszam);
        }
 
        String partnerWeboldal = entity.getPartnerWeboldal();
        if (partnerWeboldal != null) {
            stmt.bindString(7, partnerWeboldal);
        }
 
        String partnerEmailcim = entity.getPartnerEmailcim();
        if (partnerEmailcim != null) {
            stmt.bindString(8, partnerEmailcim);
        }
 
        Boolean partnerIsActive = entity.getPartnerIsActive();
        if (partnerIsActive != null) {
            stmt.bindLong(9, partnerIsActive ? 1l: 0l);
        }
    }

    @Override
    protected void attachEntity(Partner entity) {
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
    public Partner readEntity(Cursor cursor, int offset) {
        Partner entity = new Partner( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // partnerID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // partnerNev
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // partnerCim
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // partnerXkoordinata
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // partnerYkoodinata
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // partnerTelefonszam
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // partnerWeboldal
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // partnerEmailcim
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0 // partnerIsActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Partner entity, int offset) {
        entity.setPartnerID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPartnerNev(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPartnerCim(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPartnerXkoordinata(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setPartnerYkoodinata(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setPartnerTelefonszam(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPartnerWeboldal(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPartnerEmailcim(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPartnerIsActive(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Partner entity, long rowId) {
        entity.setPartnerID(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Partner entity) {
        if(entity != null) {
            return entity.getPartnerID();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
