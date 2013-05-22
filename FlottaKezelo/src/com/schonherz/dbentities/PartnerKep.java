package com.schonherz.dbentities;

import com.schonherz.dbentities.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Partnerkepek.
 */
public class PartnerKep {

    private Long partnerKepID;
    private String partnerNev;
    private String partnerKepDate;
    private Boolean partnerKepIsUploaded;
    private Long partnerID;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PartnerKepDao myDao;

    private Partner partner;
    private Long partner__resolvedKey;


    public PartnerKep() {
    }

    public PartnerKep(Long partnerKepID) {
        this.partnerKepID = partnerKepID;
    }

    public PartnerKep(Long partnerKepID, String partnerNev, String partnerKepDate, Boolean partnerKepIsUploaded, Long partnerID) {
        this.partnerKepID = partnerKepID;
        this.partnerNev = partnerNev;
        this.partnerKepDate = partnerKepDate;
        this.partnerKepIsUploaded = partnerKepIsUploaded;
        this.partnerID = partnerID;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPartnerKepDao() : null;
    }

    public Long getPartnerKepID() {
        return partnerKepID;
    }

    public void setPartnerKepID(Long partnerKepID) {
        this.partnerKepID = partnerKepID;
    }

    public String getPartnerNev() {
        return partnerNev;
    }

    public void setPartnerNev(String partnerNev) {
        this.partnerNev = partnerNev;
    }

    public String getPartnerKepDate() {
        return partnerKepDate;
    }

    public void setPartnerKepDate(String partnerKepDate) {
        this.partnerKepDate = partnerKepDate;
    }

    public Boolean getPartnerKepIsUploaded() {
        return partnerKepIsUploaded;
    }

    public void setPartnerKepIsUploaded(Boolean partnerKepIsUploaded) {
        this.partnerKepIsUploaded = partnerKepIsUploaded;
    }

    public Long getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Long partnerID) {
        this.partnerID = partnerID;
    }

    /** To-one relationship, resolved on first access. */
    public Partner getPartner() {
        if (partner__resolvedKey == null || !partner__resolvedKey.equals(partnerID)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PartnerDao targetDao = daoSession.getPartnerDao();
            partner = targetDao.load(partnerID);
            partner__resolvedKey = partnerID;
        }
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
        partnerID = partner == null ? null : partner.getPartnerID();
        partner__resolvedKey = partnerID;
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
