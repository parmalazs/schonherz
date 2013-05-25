package com.schonherz.dbentities;

import com.schonherz.dbentities.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Munkakepek.
 */
public class MunkaKep {

    private Long munkaKepID;
    private String munkaKepPath;
    private String munkaKepDate;
    private Boolean munkaKepIsUploaded;
    private Boolean munkaKepIsActive;
    private Long munkaID;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MunkaKepDao myDao;

    private Munka munka;
    private Long munka__resolvedKey;


    public MunkaKep() {
    }

    public MunkaKep(Long munkaKepID) {
        this.munkaKepID = munkaKepID;
    }

    public MunkaKep(Long munkaKepID, String munkaKepPath, String munkaKepDate, Boolean munkaKepIsUploaded, Boolean munkaKepIsActive, Long munkaID) {
        this.munkaKepID = munkaKepID;
        this.munkaKepPath = munkaKepPath;
        this.munkaKepDate = munkaKepDate;
        this.munkaKepIsUploaded = munkaKepIsUploaded;
        this.munkaKepIsActive = munkaKepIsActive;
        this.munkaID = munkaID;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMunkaKepDao() : null;
    }

    public Long getMunkaKepID() {
        return munkaKepID;
    }

    public void setMunkaKepID(Long munkaKepID) {
        this.munkaKepID = munkaKepID;
    }

    public String getMunkaKepPath() {
        return munkaKepPath;
    }

    public void setMunkaKepPath(String munkaKepPath) {
        this.munkaKepPath = munkaKepPath;
    }

    public String getMunkaKepDate() {
        return munkaKepDate;
    }

    public void setMunkaKepDate(String munkaKepDate) {
        this.munkaKepDate = munkaKepDate;
    }

    public Boolean getMunkaKepIsUploaded() {
        return munkaKepIsUploaded;
    }

    public void setMunkaKepIsUploaded(Boolean munkaKepIsUploaded) {
        this.munkaKepIsUploaded = munkaKepIsUploaded;
    }

    public Boolean getMunkaKepIsActive() {
        return munkaKepIsActive;
    }

    public void setMunkaKepIsActive(Boolean munkaKepIsActive) {
        this.munkaKepIsActive = munkaKepIsActive;
    }

    public Long getMunkaID() {
        return munkaID;
    }

    public void setMunkaID(Long munkaID) {
        this.munkaID = munkaID;
    }

    /** To-one relationship, resolved on first access. */
    public Munka getMunka() {
        if (munka__resolvedKey == null || !munka__resolvedKey.equals(munkaID)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MunkaDao targetDao = daoSession.getMunkaDao();
            munka = targetDao.load(munkaID);
            munka__resolvedKey = munkaID;
        }
        return munka;
    }

    public void setMunka(Munka munka) {
        this.munka = munka;
        munkaID = munka == null ? null : munka.getMunkaID();
        munka__resolvedKey = munkaID;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
