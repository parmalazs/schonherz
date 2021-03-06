package com.schonherz.dbentities;

import java.util.List;
import com.schonherz.dbentities.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Telephelyek.
 */
public class Telephely {

    private Long telephelyID;
    private String telephelyNev;
    private String telephelyCim;
    private String telephelyTelefonszam;
    private Float telephelyXkoordinata;
    private Float telephelyYkoordinata;
    private String telephelyEmail;
    private Boolean telephelyIsActive;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TelephelyDao myDao;

    private List<Auto> autoList;
    private List<Munka> munkaList;

    public Telephely() {
    }

    public Telephely(Long telephelyID) {
        this.telephelyID = telephelyID;
    }

    public Telephely(Long telephelyID, String telephelyNev, String telephelyCim, String telephelyTelefonszam, Float telephelyXkoordinata, Float telephelyYkoordinata, String telephelyEmail, Boolean telephelyIsActive) {
        this.telephelyID = telephelyID;
        this.telephelyNev = telephelyNev;
        this.telephelyCim = telephelyCim;
        this.telephelyTelefonszam = telephelyTelefonszam;
        this.telephelyXkoordinata = telephelyXkoordinata;
        this.telephelyYkoordinata = telephelyYkoordinata;
        this.telephelyEmail = telephelyEmail;
        this.telephelyIsActive = telephelyIsActive;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTelephelyDao() : null;
    }

    public Long getTelephelyID() {
        return telephelyID;
    }

    public void setTelephelyID(Long telephelyID) {
        this.telephelyID = telephelyID;
    }

    public String getTelephelyNev() {
        return telephelyNev;
    }

    public void setTelephelyNev(String telephelyNev) {
        this.telephelyNev = telephelyNev;
    }

    public String getTelephelyCim() {
        return telephelyCim;
    }

    public void setTelephelyCim(String telephelyCim) {
        this.telephelyCim = telephelyCim;
    }

    public String getTelephelyTelefonszam() {
        return telephelyTelefonszam;
    }

    public void setTelephelyTelefonszam(String telephelyTelefonszam) {
        this.telephelyTelefonszam = telephelyTelefonszam;
    }

    public Float getTelephelyXkoordinata() {
        return telephelyXkoordinata;
    }

    public void setTelephelyXkoordinata(Float telephelyXkoordinata) {
        this.telephelyXkoordinata = telephelyXkoordinata;
    }

    public Float getTelephelyYkoordinata() {
        return telephelyYkoordinata;
    }

    public void setTelephelyYkoordinata(Float telephelyYkoordinata) {
        this.telephelyYkoordinata = telephelyYkoordinata;
    }

    public String getTelephelyEmail() {
        return telephelyEmail;
    }

    public void setTelephelyEmail(String telephelyEmail) {
        this.telephelyEmail = telephelyEmail;
    }

    public Boolean getTelephelyIsActive() {
        return telephelyIsActive;
    }

    public void setTelephelyIsActive(Boolean telephelyIsActive) {
        this.telephelyIsActive = telephelyIsActive;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public synchronized List<Auto> getAutoList() {
        if (autoList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AutoDao targetDao = daoSession.getAutoDao();
            autoList = targetDao._queryTelephely_AutoList(telephelyID);
        }
        return autoList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAutoList() {
        autoList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public synchronized List<Munka> getMunkaList() {
        if (munkaList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MunkaDao targetDao = daoSession.getMunkaDao();
            munkaList = targetDao._queryTelephely_MunkaList(telephelyID);
        }
        return munkaList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMunkaList() {
        munkaList = null;
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
