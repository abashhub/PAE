package bizz.ucc;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import bizz.Company;
import bizz.Participation;
import bizz.dto.CompanyDTO;
import bizz.dto.JEDTO;
import exception.BizzException;
import exception.OptimisticLockException;
import persistence.DALServices;
import persistence.dao.CompanyDAO;
import persistence.dao.DAO;
import util.Util;

/**
 * This class is the implementation of EntrepriseUCC, this implementation is meant to be used in
 * production.
 *
 */
public class CompanyUCCImpl implements CompanyUCC {
  DALServices dalServices;
  DAO<CompanyDTO> companyDAO;

  private static Logger logger = Logger.getLogger(CompanyUCCImpl.class);

  /**
   * Class constructor.
   * 
   * @param dalServices The interface used to operate the database. Typically used to start
   *        transactions and commit modifications.
   * @param CompanyDAO The company DAO (Data Access Object) used to perform operations on the
   *        database regarding companies.
   */
  public CompanyUCCImpl(DALServices dalServices, DAO<CompanyDTO> companyDAO) {
    this.dalServices = dalServices;
    this.companyDAO = companyDAO;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO register(CompanyDTO companyDTO) {
    CompanyDTO companyReturn = null;
    // check if valid fields
    if (!((Company) companyDTO).checkRegistration()) {
      throw new BizzException("Erreur, entreprise incomplète.");
    }

    // Everything is ok, create the Company
    dalServices.startTransaction();
    Long companyId = companyDAO.create(companyDTO);
    if (companyId == null) {
      dalServices.rollback();
      throw new BizzException("Erreur, l'entreprise n'a pas pû être ajoutée.");

    } else {
      logger.debug("companyID=" + companyId);
      dalServices.commit();
      companyDTO.setCompanyId(companyId);
      companyReturn = companyDAO.find(companyDTO);
    }
    return companyReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompaniesInvitedForJE(JEDTO je) {
    if (Util.anyNull(je)) {
      throw new BizzException("Erreur, JE incomplète.");
    }
    dalServices.startTransaction();
    List<CompanyDTO> toReturn = ((CompanyDAO) companyDAO).findAllInvitedInJE(je);
    dalServices.commit();
    return toReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompaniesNotInvitedForJE(JEDTO je) {
    if (Util.anyNull(je)) {
      throw new BizzException("Erreur, JE incomplète.");
    }
    dalServices.startTransaction();
    List<CompanyDTO> toReturn = ((CompanyDAO) companyDAO).findAllNotInvitedInJE(je);
    dalServices.commit();
    return toReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO getCompany(CompanyDTO companyDTO) {
    if (Util.anyNull(companyDTO)) {
      throw new BizzException("Erreur, entreprise incomplète.");
    }
    dalServices.startTransaction();
    CompanyDTO company = ((CompanyDAO) companyDAO).find(companyDTO);
    dalServices.commit();
    if (company == null) {
      throw new BizzException("Erreur, l'entreprise n'a pas pu être trouvée");
    }
    return company;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompaniesAviableForJe(JEDTO jeDto) {
    if (Util.anyNull(jeDto)) {
      throw new BizzException("Erreur, JE incomplète.");
    }
    dalServices.startTransaction();
    List<CompanyDTO> listeReturn = ((CompanyDAO) companyDAO).getAllCompaniesAviableForJe(jeDto);
    dalServices.commit();
    return listeReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getNewCompaniesAviableForJe() {
    dalServices.startTransaction();
    List<CompanyDTO> listeReturn = ((CompanyDAO) companyDAO).getNewCompaniesAviableForJe();
    dalServices.commit();
    return listeReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public boolean inviteCompany(CompanyDTO companyDto, JEDTO jeCourrante) {
    if (Util.anyNull(companyDto, jeCourrante)) {
      throw new BizzException("Erreur, JE incomplète.");
    }
    Date dateActuelle = new Date();
    if (dateActuelle.after(jeCourrante.getDateJournee()) || jeCourrante.isCloturee()) {
      throw new BizzException("Journée dépassée.");
    }

    dalServices.startTransaction();
    if (!((CompanyDAO) companyDAO).inviteCompany(companyDto, jeCourrante)) {
      throw new BizzException("Erreur, l'entreprise n'as pas pu être invitée.");
    }
    dalServices.commit();
    return true;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO updateCompany(CompanyDTO companyDTO) {
    if (Util.anyNull(companyDTO)) {
      throw new BizzException("Erreur, entreprise incomplète.");
    }
    CompanyDTO companyReturn = null;
    try {
      dalServices.startTransaction();
      Long companyId = companyDAO.update(companyDTO);
      if (companyId == null) {
        // An error has occurred -> rollback
        // before that check what went wrong :
        companyId = companyDAO.find(companyDTO).getCompanyId();
        dalServices.rollback();

        if (companyId != null) {
          throw new BizzException(
              "Erreur, vous ne travaillez pas sur la dernière version de l'objet.");
        }
        // if still null then object doesn't exist in the DB
        throw new BizzException("Une erreur s'est introduite, l'objet n'existe pas.");

      } else {
        logger.debug("companyID=" + companyId);
        companyReturn = companyDAO.find(companyDTO);
        dalServices.commit();
      }
    } catch (OptimisticLockException ole) {
      throw new BizzException(ole.getMessage());
    }
    return companyReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<List<String>> getAllCompaniesInfoByCategory() {
    dalServices.startTransaction();
    List<List<String>> toReturn = ((CompanyDAO) companyDAO).getAllCompaniesInfoByCategory();
    dalServices.commit();
    return toReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> searchCompanies(CompanyDTO co) {
    dalServices.startTransaction();
    List<CompanyDTO> toReturn = ((CompanyDAO) companyDAO).searchCompanies(co);
    dalServices.commit();
    return toReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO changeState(CompanyDTO companyDto, JEDTO jeCourante) {
    if (Util.anyNull(companyDto, jeCourante)) {
      throw new BizzException("Erreur, JE incomplète.");
    }
    CompanyDTO company = null;
    try {
      if (((Participation) companyDto.getParticipations().get(0)).checkParticipation()) {
        logger.debug("etat correct");
        dalServices.startTransaction();
        company = ((CompanyDAO) companyDAO).changeState(companyDto, jeCourante);
        if (company == null) {
          // An error has occurred -> rollback
          dalServices.rollback();
          throw new BizzException("Erreur, une erreur s'est introduite, l'objet n'existe pas.");

        } else {
          logger.debug("company : " + company.toString());
          dalServices.commit();
        }
      } else {
        logger.debug("état inccorect");
      }
    } catch (OptimisticLockException ole) {
      throw new BizzException(ole.getMessage());
    }
    return company;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO cancelState(CompanyDTO companyDto, JEDTO jeCourante) {
    if (Util.anyNull(companyDto, jeCourante)) {
      throw new BizzException("Erreur, informations incomplètes.");
    }

    CompanyDTO company = null;
    dalServices.startTransaction();
    company = ((CompanyDAO) companyDAO).cancelState(companyDto, jeCourante);
    if (company == null) {
      // An error has occurred -> rollback
      dalServices.rollback();
      throw new BizzException("Erreur, la participation n'as pas pu être annulée.");
    } else {
      logger.debug("company : " + company.toString());
      dalServices.commit();
    }
    return company;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompaniesToContactForJE(JEDTO je) {
    if (Util.anyNull(je)) {
      throw new BizzException("Erreur, JE incomplète.");
    }
    dalServices.startTransaction();
    List<CompanyDTO> toReturn = ((CompanyDAO) companyDAO).getAllToContactForJE(je);
    dalServices.commit();
    return toReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompanies() {
    dalServices.startTransaction();
    List<CompanyDTO> toReturn = ((CompanyDAO) companyDAO).getAllCompanies();
    dalServices.commit();
    return toReturn;
  }

  @Override
  public CompanyDTO getCompanyForCSV(CompanyDTO companyDTO, JEDTO je) {
    if (Util.anyNull(companyDTO, je)) {
      throw new BizzException("Erreur, informations incomplètes.");
    }
    dalServices.startTransaction();
    CompanyDTO company = ((CompanyDAO) companyDAO).findForCSV(companyDTO, je);
    dalServices.commit();
    if (company == null) {
      throw new BizzException("Erreur, l'entreprise n'a pas pu être trouvée");
    }
    return company;
  }
}
