package com.schonherz.dbentities;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.IdentityScopeType;

import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.MunkaTipus;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.MunkaEszkoz;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaKep;

import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.dbentities.MunkaEszkozDao;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaKepDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig soforDaoConfig;
    private final DaoConfig profilKepDaoConfig;
    private final DaoConfig autoDaoConfig;
    private final DaoConfig autoKepDaoConfig;
    private final DaoConfig munkaTipusDaoConfig;
    private final DaoConfig partnerDaoConfig;
    private final DaoConfig partnerKepDaoConfig;
    private final DaoConfig telephelyDaoConfig;
    private final DaoConfig munkaEszkozDaoConfig;
    private final DaoConfig munkaDaoConfig;
    private final DaoConfig munkaKepDaoConfig;

    private final SoforDao soforDao;
    private final ProfilKepDao profilKepDao;
    private final AutoDao autoDao;
    private final AutoKepDao autoKepDao;
    private final MunkaTipusDao munkaTipusDao;
    private final PartnerDao partnerDao;
    private final PartnerKepDao partnerKepDao;
    private final TelephelyDao telephelyDao;
    private final MunkaEszkozDao munkaEszkozDao;
    private final MunkaDao munkaDao;
    private final MunkaKepDao munkaKepDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        soforDaoConfig = daoConfigMap.get(SoforDao.class).clone();
        soforDaoConfig.initIdentityScope(type);

        profilKepDaoConfig = daoConfigMap.get(ProfilKepDao.class).clone();
        profilKepDaoConfig.initIdentityScope(type);

        autoDaoConfig = daoConfigMap.get(AutoDao.class).clone();
        autoDaoConfig.initIdentityScope(type);

        autoKepDaoConfig = daoConfigMap.get(AutoKepDao.class).clone();
        autoKepDaoConfig.initIdentityScope(type);

        munkaTipusDaoConfig = daoConfigMap.get(MunkaTipusDao.class).clone();
        munkaTipusDaoConfig.initIdentityScope(type);

        partnerDaoConfig = daoConfigMap.get(PartnerDao.class).clone();
        partnerDaoConfig.initIdentityScope(type);

        partnerKepDaoConfig = daoConfigMap.get(PartnerKepDao.class).clone();
        partnerKepDaoConfig.initIdentityScope(type);

        telephelyDaoConfig = daoConfigMap.get(TelephelyDao.class).clone();
        telephelyDaoConfig.initIdentityScope(type);

        munkaEszkozDaoConfig = daoConfigMap.get(MunkaEszkozDao.class).clone();
        munkaEszkozDaoConfig.initIdentityScope(type);

        munkaDaoConfig = daoConfigMap.get(MunkaDao.class).clone();
        munkaDaoConfig.initIdentityScope(type);

        munkaKepDaoConfig = daoConfigMap.get(MunkaKepDao.class).clone();
        munkaKepDaoConfig.initIdentityScope(type);

        soforDao = new SoforDao(soforDaoConfig, this);
        profilKepDao = new ProfilKepDao(profilKepDaoConfig, this);
        autoDao = new AutoDao(autoDaoConfig, this);
        autoKepDao = new AutoKepDao(autoKepDaoConfig, this);
        munkaTipusDao = new MunkaTipusDao(munkaTipusDaoConfig, this);
        partnerDao = new PartnerDao(partnerDaoConfig, this);
        partnerKepDao = new PartnerKepDao(partnerKepDaoConfig, this);
        telephelyDao = new TelephelyDao(telephelyDaoConfig, this);
        munkaEszkozDao = new MunkaEszkozDao(munkaEszkozDaoConfig, this);
        munkaDao = new MunkaDao(munkaDaoConfig, this);
        munkaKepDao = new MunkaKepDao(munkaKepDaoConfig, this);

        registerDao(Sofor.class, soforDao);
        registerDao(ProfilKep.class, profilKepDao);
        registerDao(Auto.class, autoDao);
        registerDao(AutoKep.class, autoKepDao);
        registerDao(MunkaTipus.class, munkaTipusDao);
        registerDao(Partner.class, partnerDao);
        registerDao(PartnerKep.class, partnerKepDao);
        registerDao(Telephely.class, telephelyDao);
        registerDao(MunkaEszkoz.class, munkaEszkozDao);
        registerDao(Munka.class, munkaDao);
        registerDao(MunkaKep.class, munkaKepDao);
    }
    
    public void clear() {
        soforDaoConfig.getIdentityScope().clear();
        profilKepDaoConfig.getIdentityScope().clear();
        autoDaoConfig.getIdentityScope().clear();
        autoKepDaoConfig.getIdentityScope().clear();
        munkaTipusDaoConfig.getIdentityScope().clear();
        partnerDaoConfig.getIdentityScope().clear();
        partnerKepDaoConfig.getIdentityScope().clear();
        telephelyDaoConfig.getIdentityScope().clear();
        munkaEszkozDaoConfig.getIdentityScope().clear();
        munkaDaoConfig.getIdentityScope().clear();
        munkaKepDaoConfig.getIdentityScope().clear();
    }

    public SoforDao getSoforDao() {
        return soforDao;
    }

    public ProfilKepDao getProfilKepDao() {
        return profilKepDao;
    }

    public AutoDao getAutoDao() {
        return autoDao;
    }

    public AutoKepDao getAutoKepDao() {
        return autoKepDao;
    }

    public MunkaTipusDao getMunkaTipusDao() {
        return munkaTipusDao;
    }

    public PartnerDao getPartnerDao() {
        return partnerDao;
    }

    public PartnerKepDao getPartnerKepDao() {
        return partnerKepDao;
    }

    public TelephelyDao getTelephelyDao() {
        return telephelyDao;
    }

    public MunkaEszkozDao getMunkaEszkozDao() {
        return munkaEszkozDao;
    }

    public MunkaDao getMunkaDao() {
        return munkaDao;
    }

    public MunkaKepDao getMunkaKepDao() {
        return munkaKepDao;
    }

}
