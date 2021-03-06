package persistence.dao;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bizz.dto.AddressDTO;
import bizz.dto.CompanyDTO;
import bizz.dto.JEDTO;
import bizz.dto.ParticipationDTO;
import bizz.dto.UserDTO;
import bizz.factory.AddressFactory;
import bizz.factory.CompanyFactory;
import bizz.factory.ParticipationFactory;
import bizz.factory.UserFactory;
import exception.OptimisticLockException;
import persistence.DALBackendServices;
import persistence.DALServices;
import util.Util;

/**
 * This class is the implementation of CompanyDAO used in production. Since CompanyDAO extends DAO,
 * this implementation contains the basic CRUD as well.
 */
public class CompanyDAOImpl implements CompanyDAO {

  private DALServices dalServices;
  private CompanyFactory companyFactory;
  private UserFactory userFactory;
  private DAO<UserDTO> userDAO;
  private ParticipationFactory participationFactory;
  private AddressFactory addressFactory;

  private static Logger logger = Logger.getLogger(CompanyDAOImpl.class);
  private static final String SQL_SELECT_ALL_FROM_JE =
      "SELECT e.id_entreprise,e.createur, e.nom, e.date_premier_contact, e.num_version,"
          + " pe.etat, pe.annulee, pe.num_version_participation, af.rue, af.numero,"
          + " af.boite, c.code_postal, c.nomCommune, af.id_adresse_facturation, je.date_journee  "
          + "FROM PAE.entreprises e, PAE.participations_entreprises pe,"
          + "PAE.communes c , PAE.adresses_facturation af, PAE.journees_entreprises je  "
          + "WHERE e.id_entreprise = pe.entreprise "
          + "and e.adresse_facturation = af.id_adresse_facturation and af.commune=c.code_postal "
          + "and je.id_journee_entreprises=pe.journee_entreprises and pe.journee_entreprises = ?";
  private static final String SQL_SELECT_ALL_NOT_INVITED_IN_JE =
      "SELECT DISTINCT e.id_entreprise,e.createur, e.nom, e.date_premier_contact, e.num_version,"
          + " af.rue, af.numero, af.boite, c.code_postal, c.nomCommune, af.id_adresse_facturation  "
          + "FROM PAE.entreprises e, PAE.communes c , PAE.adresses_facturation af "
          + "WHERE e.adresse_facturation = af.id_adresse_facturation "
          + "and af.commune=c.code_postal and not exists ( select pe.journee_entreprises = ? "
          + "from PAE.participations_entreprises pe "
          + "where e.id_entreprise = pe.entreprise AND pe.journee_entreprises = ?)";
  private static final String SQL_INSERT_COMPANY =
      "INSERT INTO PAE.entreprises VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING id_entreprise";
  private static final String SQL_SELECT_BY_ID =
      "SELECT e.id_entreprise,e.createur, e.nom, e.date_premier_contact,"
          + " e.num_version, af.rue, af.numero, af.boite, c.code_postal, c.nomCommune, "
          + "af.id_adresse_facturation FROM PAE.entreprises e, PAE.communes c , "
          + "PAE.adresses_facturation af WHERE e.adresse_facturation = af.id_adresse_facturation "
          + "and af.commune = c.code_postal and e.id_entreprise = ?";
  private static final String SQL_INSERT_LOCALITY =
      "INSERT INTO PAE.communes VALUES (?, ?) RETURNING code_postal";
  private static final String SQL_SELECT_LOCALITY =
      "SELECT * FROM PAE.communes c WHERE c.code_postal = ?";
  private static final String SQL_INSERT_ADDRESS =
      "INSERT INTO PAE.adresses_facturation VALUES (DEFAULT, ?, ?, ?, ?) RETURNING "
          + "id_adresse_facturation";
  private static final String SQL_SELECT_ADDRESS =
      "SELECT * FROM PAE.adresses_facturation a,  PAE.communes c WHERE a.rue = ? AND a.numero = ? AND a.boite = ? "
          + "AND c.code_postal = ? AND c.code_postal = a.commune";

  private static final String SQL_SELECT_AVIABLE_FOR_JE =
      "select DISTINCT e.* from PAE.entreprises e, PAE.participations_entreprises p, PAE.journees_entreprises j "
          + "WHERE e.id_entreprise = p.entreprise AND j.id_journee_entreprises = p.journee_entreprises "
          + "AND p.etat = 'payee' and DATE(j.date_journee) between ? and ?";

  private static final String SQL_SELECT_NEW_FOR_JE =
      "SELECT * FROM PAE.entreprises e WHERE extract(year from e.date_premier_contact) = "
          + "extract(year from NOW()) AND e.id_entreprise NOT IN (SELECT pe.entreprise "
          + "FROM PAE.participations_entreprises pe)";

  private static final String SQL_INSERT_ADD_PARTICIPATION =
      "INSERT INTO PAE.participations_entreprises VALUES (?,?,?,?,false) RETURNING entreprise";

  private static final String SQL_UPDATE_COMPANY =
      "UPDATE PAE.entreprises SET nom = ?, adresse_facturation = ?, num_version = ? "
          + "WHERE num_version < ? AND id_entreprise = ? RETURNING id_entreprise";

  private static final String SQL_SELECT_ALL_NAMES = "SELECT DISTINCT e.nom FROM PAE.entreprises e";
  private static final String SQL_SELECT_ALL_POSTAL_CODE =
      "SELECT DISTINCT c.code_postal FROM PAE.communes c";
  private static final String SQL_SELECT_ALL_LOCALITIES =
      "SELECT DISTINCT c.nomCommune FROM PAE.communes c";
  private static final String SQL_SELECT_ALL_STREETS =
      "SELECT DISTINCT af.rue FROM PAE.adresses_facturation af";
  private static final String SQL_SEARCH_COMPANIES_WITH_CODE_POSTAL =
      "SELECT * FROM PAE.adresses_facturation a, PAE.communes c, PAE.entreprises e WHERE "
          + "a.rue LIKE ? AND a.commune = c.code_postal AND e.nom LIKE ? AND c.nomCommune LIKE "
          + "? AND a.id_adresse_facturation=id_entreprise  AND c.code_postal = ?";
  private static final String SQL_SEARCH_COMPANIES =
      "SELECT * FROM PAE.adresses_facturation a, PAE.communes c, PAE.entreprises e WHERE a.rue LIKE ?"
          + "AND a.commune = c.code_postal AND e.nom LIKE ? AND c.nomCommune LIKE ? AND "
          + "a.id_adresse_facturation=id_entreprise";
  private static final String SQL_UPDATE_PARTICIPATION =
      "UPDATE PAE.participations_entreprises SET etat = ?, num_version_participation = ? "
          + "WHERE num_version_participation < ? AND entreprise = ? AND journee_entreprises = ? AND "
          + "annulee = false RETURNING etat";
  private static final String SQL_SELECT_PARTICIPATION =
      "SELECT * FROM PAE.participations_entreprises WHERE entreprise = ? AND journee_entreprises = ? AND "
          + "annulee = false";

  private static final String SQL_UPDATE_CANCEL_PARTICIPATION =
      "UPDATE PAE.participations_entreprises SET annulee = true, num_version_participation = ? WHERE "
          + "num_version_participation < ? AND entreprise = ? AND journee_entreprises = ? AND "
          + "annulee = false RETURNING etat";
  private static final String SQL_SELECT_ALL_TO_CONTACT_FROM_JE =
      "SELECT e.id_entreprise,e.createur, e.nom, e.date_premier_contact, e.num_version,"
          + " pe.etat, pe.annulee, pe.num_version_participation, af.rue, af.numero, af.boite,"
          + " c.code_postal, c.nomCommune, af.id_adresse_facturation, je.date_journee  "
          + "FROM PAE.entreprises e, PAE.participations_entreprises pe,PAE.communes c , "
          + "PAE.adresses_facturation af, PAE.journees_entreprises je WHERE e.id_entreprise = "
          + "pe.entreprise and e.adresse_facturation = af.id_adresse_facturation and "
          + "af.commune = c.code_postal and je.id_journee_entreprises=pe.journee_entreprises and "
          + "pe.journee_entreprises = ? and pe.etat = 'invitee' and pe.annulee = false";

  private static final String SQL_SELECT_ALL_COMPANIES = "SELECT * FROM PAE.entreprises";

  private static final String SQL_DELETE_ALL_CONTACT_PARTICIPATIONS =
      "DELETE FROM PAE.participations_contacts WHERE entreprise = ? AND journee_entreprises = ?";
  private static final String SQL_SELECT_BY_ID_TO_CONTACT_FROM_JE =
      "SELECT e.id_entreprise,e.createur, e.nom, e.date_premier_contact, e.num_version,"
          + " pe.etat, pe.annulee, pe.num_version_participation, af.rue, af.numero, af.boite,"
          + " c.code_postal, c.nomCommune, af.id_adresse_facturation, je.date_journee  "
          + "FROM PAE.entreprises e, PAE.participations_entreprises pe,PAE.communes c , "
          + "PAE.adresses_facturation af, PAE.journees_entreprises je WHERE e.id_entreprise = "
          + "pe.entreprise and e.adresse_facturation = af.id_adresse_facturation and "
          + "af.commune = c.code_postal and je.id_journee_entreprises=pe.journee_entreprises and "
          + "pe.journee_entreprises = ? and e.id_entreprise = ?";

  /**
   * This class is the constructor, it is ment to be used in the main.class to build the
   * application.
   */
  public CompanyDAOImpl(DALServices dalServices, CompanyFactory companyFactory,
      UserFactory userFactory, DAO<UserDTO> userDAO2, ParticipationFactory participationFactory,
      AddressFactory adressFactory) {
    this.dalServices = dalServices;
    this.companyFactory = companyFactory;
    this.userFactory = userFactory;
    this.userDAO = userDAO2;
    this.participationFactory = participationFactory;
    this.addressFactory = adressFactory;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO find(CompanyDTO company) {
    CompanyDTO companyReturn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_BY_ID);
      ps.setLong(1, company.getCompanyId());
      rs = ps.executeQuery();
      while (rs.next()) {
        companyReturn = buildCompanyWithoutParticipation(rs);
      }
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return companyReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Long create(CompanyDTO company) {
    Long insertedId = null;
    // check if locality already in the sys
    Long localityId = company.getAdresseFacturation().getPostCode();
    if (!findLocality(localityId)) {
      // sinon le créer !
      createLocality(company.getAdresseFacturation().getLocality(),
          company.getAdresseFacturation().getPostCode());
    }
    // check if address is already in the sys
    int addressId = -1;
    AddressDTO address = findAddress(company.getAdresseFacturation());
    if (address == null) {
      addressId = createAddress(company.getAdresseFacturation());
    } else {
      addressId = address.getIdAdressFacturation();
    }
    // get user from db
    UserDTO user = ((UserDAO) this.userDAO).findByLogin(company.getCreater());

    // all needed references are now in the sys-> add company
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_COMPANY);
      ps.setInt(1, addressId);
      ps.setInt(2, user.getUserID().intValue());
      ps.setString(3, company.getName());
      ps.setDate(4, Date.valueOf(LocalDate.now()));
      // num_version when created == 0
      ps.setInt(5, 0);
      rs = ps.executeQuery();
      while (rs.next()) {
        insertedId = rs.getLong(1);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return insertedId;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Long update(CompanyDTO companyDto) {
    // check if address has changed if so, then create a new one
    AddressDTO currentAddress = null;
    Long insertedId = null;
    Long insertadAddressId = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_ADDRESS);
      ps.setString(1, companyDto.getAdresseFacturation().getStreet());
      ps.setString(2, companyDto.getAdresseFacturation().getNumber());
      ps.setString(3, companyDto.getAdresseFacturation().getBox());
      ps.setLong(4, companyDto.getAdresseFacturation().getPostCode());
      logger.debug(companyDto.getAdresseFacturation().getStreet()
          + companyDto.getAdresseFacturation().getNumber()
          + companyDto.getAdresseFacturation().getBox()
          + companyDto.getAdresseFacturation().getPostCode());
      rs = ps.executeQuery();
      if (rs.next()) {
        logger.debug("adresse identique");
        logger.debug("id address " + companyDto.getAdresseFacturation().getIdAdressFacturation());
        currentAddress = buildAddress(rs);
        logger.debug("id address apres build " + currentAddress.getIdAdressFacturation());
        rs.close();
        ps.close();
        // then address didn't change and can update name
        ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_UPDATE_COMPANY);
        ps.setString(1, companyDto.getName());
        ps.setInt(2, currentAddress.getIdAdressFacturation());
        ps.setInt(3, companyDto.getNumVersion() + 1);
        ps.setInt(4, companyDto.getNumVersion() + 1);
        ps.setLong(5, companyDto.getCompanyId());
        rs = ps.executeQuery();
        logger.debug(companyDto.getNumVersion() + 1);
        logger.debug(companyDto.getName() + " addresse : " + currentAddress.getIdAdressFacturation()
            + " " + companyDto.getCompanyId());
        if (rs.next()) {
          logger.debug("a fait l'update ");
          insertedId = rs.getLong(1);
          rs.close();
        }
      } else {
        rs.close();
        ps.close();
        logger.debug("adresse différente");
        // address changed, need to create a new one
        // check if postcode exist
        ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_LOCALITY);
        ps.setLong(1, companyDto.getAdresseFacturation().getPostCode());
        rs = ps.executeQuery();
        if (!rs.next()) {
          logger.debug("postcode pas trouvé");
          // no postcode found, must add new one
          ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_LOCALITY);
          ps.setLong(1, companyDto.getAdresseFacturation().getPostCode());
          ps.setString(2, companyDto.getAdresseFacturation().getLocality());
          rs = ps.executeQuery();
        }
        rs.close();
        // add address
        logger.debug("add address");
        ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_ADDRESS);
        ps.setString(1, companyDto.getAdresseFacturation().getStreet());
        ps.setString(2, companyDto.getAdresseFacturation().getNumber());
        ps.setString(3, companyDto.getAdresseFacturation().getBox());
        ps.setLong(4, companyDto.getAdresseFacturation().getPostCode());
        rs = ps.executeQuery();
        if (rs.next()) {
          insertadAddressId = rs.getLong(1);
          // now update the company
          ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_UPDATE_COMPANY);
          ps.setString(1, companyDto.getName());
          ps.setLong(2, insertadAddressId);
          ps.setInt(3, companyDto.getNumVersion() + 1);
          ps.setInt(4, companyDto.getNumVersion() + 1);
          ps.setLong(5, companyDto.getCompanyId());
          rs = ps.executeQuery();
          logger.debug("update company");
          if (rs.next()) {
            insertedId = rs.getLong(1);
          } else {
            if (find(companyDto).getCompanyId() != null) {
              throw new OptimisticLockException(
                  "Erreur, vous ne travaillez pas sur la dernière version de l'objet.");
            }
          }
        }
      }
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return insertedId;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void delete(CompanyDTO obj) {
    // TODO Auto-generated method stub
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> findAllInvitedInJE(JEDTO je) {
    List<CompanyDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_ALL_FROM_JE);
      ps.setLong(1, je.getId());
      rs = ps.executeQuery();
      CompanyDTO co;
      while (rs.next()) {
        co = this.buildCompany(rs);
        list.add(co);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return list;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> findAllNotInvitedInJE(JEDTO je) {
    List<CompanyDTO> list = new ArrayList<>();
    if (je == null)
      return list;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices)
          .prepareStatement(SQL_SELECT_ALL_NOT_INVITED_IN_JE);
      ps.setLong(1, je.getId());
      ps.setLong(2, je.getId());
      rs = ps.executeQuery();
      CompanyDTO co;
      while (rs.next()) {
        co = this.buildCompanyWithoutParticipation(rs);
        list.add(co);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return list;
  }

  /**
   * This method returns an object CompanyDTO using the informations in the ResultSet given as
   * parameter.
   * 
   * @return CompanyDTO build.
   */
  private CompanyDTO buildCompany(ResultSet rs) throws SQLException {
    // creation of the participation
    ParticipationDTO pa = this.participationFactory.create();
    pa.setCancelled(rs.getBoolean("annulee"));
    pa.setState(rs.getString("etat"));
    logger.debug("num version " + rs.getInt("num_version_participation"));
    pa.setNumVersion(rs.getInt("num_version_participation"));

    // creation of the company object
    CompanyDTO co = buildCompanyWithoutParticipation(rs);
    co.getParticipations().add(pa);

    return co;
  }

  /**
   * This method returns an object CompanyDTO using the informations in the ResultSet given as
   * parameter.
   * 
   * @return CompanyDTO build.
   */
  private CompanyDTO buildCompanyWithoutParticipation(ResultSet rs) throws SQLException {
    // creation of the adress object
    AddressDTO adr = buildAddressWithLocality(rs);

    // creation of the creator's user object
    UserDTO createur = this.userFactory.createUser();
    createur.setUserID(rs.getLong("createur"));
    createur = this.userDAO.find(createur);

    // creation of the company object
    CompanyDTO co = buildCompanyWithNameAndID(rs);
    co.setAdresseFacturation(adr);
    co.setCreater(createur);
    co.setDateFirstContact(rs.getDate("date_premier_contact"));
    co.setCompanyId(rs.getLong("id_entreprise"));
    co.setName(rs.getString("nom"));
    co.setNumVersion(rs.getInt("num_version"));

    return co;
  }

  /**
   * Build the company.
   * 
   * @return company build.
   */
  private CompanyDTO buildCompanyWithNameAndID(ResultSet rs) throws SQLException {
    CompanyDTO company = companyFactory.createCompany();
    company.setCompanyId(rs.getLong("id_entreprise"));
    company.setName(rs.getString("nom"));
    company.setNumVersion(rs.getInt("num_version"));
    return company;
  }

  /**
   * Build the locality.
   * 
   * @return id of the created locality.
   */
  private Long createLocality(String locality, Long postCode) {
    Long insertedId = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_LOCALITY);
      ps.setLong(1, postCode);
      ps.setString(2, locality);
      rs = ps.executeQuery();
      while (rs.next()) {
        insertedId = rs.getLong(1);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return insertedId;
  }

  /**
   * Return the name of the locality corresponding.
   * 
   * @return true if found.
   */
  private boolean findLocality(Long postCode) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_LOCALITY);
      ps.setLong(1, postCode);
      rs = ps.executeQuery();
      if (rs.next()) {
        rs.close();
        return true;
      }
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return false;
  }

  /**
   * Build the address.
   * 
   * @return id of address created.
   */
  private int createAddress(AddressDTO addressDTO) {
    int insertedId = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_ADDRESS);
      ps.setString(1, addressDTO.getStreet());
      ps.setString(2, addressDTO.getNumber());
      ps.setString(3, addressDTO.getBox());
      ps.setLong(4, addressDTO.getPostCode());
      rs = ps.executeQuery();
      while (rs.next()) {
        insertedId = rs.getInt(1);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return insertedId;
  }

  /**
   * Find the corresponding address.
   * 
   * @return adr found.
   */
  private AddressDTO findAddress(AddressDTO addressDTO) {
    AddressDTO address = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_ADDRESS);
      ps.setString(1, addressDTO.getStreet());
      ps.setString(2, addressDTO.getNumber());
      ps.setString(3, addressDTO.getBox());
      ps.setLong(4, addressDTO.getPostCode());
      rs = ps.executeQuery();
      while (rs.next()) {
        address = this.buildAddress(rs);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return address;
  }

  /**
   * Build the address without building a locality.
   * 
   * @return adr build.
   */
  private AddressDTO buildAddressWithLocality(ResultSet rs) throws SQLException {
    AddressDTO adr = this.addressFactory.createAdress();
    adr.setLocality(rs.getString("nomCommune"));
    adr.setBox(rs.getString("boite"));
    adr.setNumber(rs.getString("numero"));
    adr.setStreet(rs.getString("rue"));
    adr.setIdAdressFacturation(rs.getInt("id_adresse_facturation"));
    adr.setPostCode(rs.getLong("code_postal"));
    return adr;
  }

  /**
   * Build the address.
   * 
   * @return adr build.
   */
  private AddressDTO buildAddress(ResultSet rs) throws SQLException {
    AddressDTO adr = this.addressFactory.createAdress();
    adr.setLocality(rs.getString("commune"));
    adr.setBox(rs.getString("boite"));
    adr.setNumber(rs.getString("numero"));
    adr.setStreet(rs.getString("rue"));
    adr.setIdAdressFacturation(rs.getInt("id_adresse_facturation"));
    adr.setPostCode(rs.getLong("code_postal"));
    return adr;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompaniesAviableForJe(JEDTO jeDto) {
    List<CompanyDTO> listeReturn = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    java.util.Date dateAnterieur = new Date(jeDto.getDateJournee().getTime());
    dateAnterieur.setYear(dateAnterieur.getYear() - 4);
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_AVIABLE_FOR_JE);
      ps.setDate(1, new java.sql.Date(dateAnterieur.getTime()));
      ps.setDate(2, new java.sql.Date(jeDto.getDateJournee().getTime()));
      rs = ps.executeQuery();
      while (rs.next()) {
        CompanyDTO company = buildCompanyWithNameAndID(rs);
        listeReturn.add(company);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return listeReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getNewCompaniesAviableForJe() {
    List<CompanyDTO> listeReturn = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_NEW_FOR_JE);
      rs = ps.executeQuery();
      while (rs.next()) {
        CompanyDTO company = buildCompanyWithNameAndID(rs);
        listeReturn.add(company);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return listeReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public boolean inviteCompany(CompanyDTO companyDto, JEDTO jeCourrante) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_ADD_PARTICIPATION);
      ps.setLong(1, jeCourrante.getId());
      ps.setLong(2, companyDto.getCompanyId());
      ps.setString(3, "invitee");
      ps.setInt(4, 0);
      rs = ps.executeQuery();
      if (rs.next()) {
        logger.debug("DAO " + rs.getInt("entreprise"));
        rs.close();
        return true;
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return false;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<List<String>> getAllCompaniesInfoByCategory() {
    List<List<String>> liste = new ArrayList<>();
    addToList(liste, SQL_SELECT_ALL_NAMES, "nom");
    addToList(liste, SQL_SELECT_ALL_POSTAL_CODE, "code_postal");
    addToList(liste, SQL_SELECT_ALL_LOCALITIES, "nomCommune");
    addToList(liste, SQL_SELECT_ALL_STREETS, "rue");
    return liste;
  }

  /**
   * This function add the content of the given resultSet in the given list.
   */
  private void addToList(List<List<String>> liste, String statement, String column) {
    ArrayList<String> listeTemp = new ArrayList<String>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(statement);
      rs = ps.executeQuery();
      while (rs.next()) {
        listeTemp.add(rs.getString(column));
      }
      liste.add(listeTemp);
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> searchCompanies(CompanyDTO co) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<CompanyDTO> list = new ArrayList<>();
    try {
      if (co.getAdresseFacturation().getPostCode() != null) {
        ps = ((DALBackendServices) this.dalServices)
            .prepareStatement(SQL_SEARCH_COMPANIES_WITH_CODE_POSTAL);
        ps.setLong(4, co.getAdresseFacturation().getPostCode());
      } else {
        ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SEARCH_COMPANIES);
      }
      ps.setString(1, '%' + co.getAdresseFacturation().getStreet() + '%');
      ps.setString(2, '%' + co.getName() + '%');
      ps.setString(3, '%' + co.getAdresseFacturation().getLocality() + '%');
      rs = ps.executeQuery();

      while (rs.next()) {
        co = this.buildCompanyWithoutParticipation(rs);
        list.add(co);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return list;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO changeState(CompanyDTO companyDto, JEDTO jeCourante) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_UPDATE_PARTICIPATION);
      ps.setString(1, companyDto.getParticipations().get(0).getState());
      ps.setInt(2, companyDto.getParticipations().get(0).getNumVersion() + 1);
      ps.setInt(3, companyDto.getParticipations().get(0).getNumVersion() + 1);
      ps.setLong(4, companyDto.getCompanyId());
      ps.setLong(5, jeCourante.getId());
      rs = ps.executeQuery();
      if (rs.next()) {
        logger.debug("etat " + rs.getString("etat"));
        companyDto.setNumVersion(companyDto.getNumVersion() + 1);
        return companyDto;
      } else {
        if (checkParticipation(companyDto, jeCourante)) {
          throw new OptimisticLockException(
              "Erreur, vous ne travaillez pas sur la dernière version de l'objet.");

        }
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return null;
  }

  private boolean checkParticipation(CompanyDTO companyDto, JEDTO jeCourante) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_PARTICIPATION);
      ps.setLong(1, companyDto.getCompanyId());
      ps.setLong(2, jeCourante.getId());
      rs = ps.executeQuery();
      if (rs.next()) {
        rs.close();
        return true;
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return false;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public CompanyDTO cancelState(CompanyDTO companyDto, JEDTO jeCourante) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices)
          .prepareStatement(SQL_UPDATE_CANCEL_PARTICIPATION);
      ps.setInt(1, companyDto.getParticipations().get(0).getNumVersion() + 1);
      ps.setInt(2, companyDto.getParticipations().get(0).getNumVersion() + 1);
      ps.setLong(3, companyDto.getCompanyId());
      ps.setLong(4, jeCourante.getId());
      rs = ps.executeQuery();
      if (rs.next()) {
        logger.debug("etat apres annulation " + rs.getString("etat"));
        companyDto.setNumVersion(companyDto.getNumVersion() + 1);
        cancelAllContactParticipation(companyDto.getCompanyId(), jeCourante.getId());
        return companyDto;
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return null;
  }

  private void cancelAllContactParticipation(Long companyId, Long jeId) {
    PreparedStatement ps = null;
    try {
      ps = ((DALBackendServices) this.dalServices)
          .prepareStatement(SQL_DELETE_ALL_CONTACT_PARTICIPATIONS);
      ps.setLong(1, companyId);
      ps.setLong(2, jeId);
      ps.execute();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(ps);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllToContactForJE(JEDTO je) {
    List<CompanyDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices)
          .prepareStatement(SQL_SELECT_ALL_TO_CONTACT_FROM_JE);
      ps.setLong(1, je.getId());
      rs = ps.executeQuery();
      CompanyDTO co;
      while (rs.next()) {
        co = this.buildCompany(rs);
        list.add(co);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return list;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<CompanyDTO> getAllCompanies() {
    List<CompanyDTO> list = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_ALL_COMPANIES);
      rs = ps.executeQuery();
      CompanyDTO co;
      while (rs.next()) {
        co = this.buildCompanyWithNameAndID(rs);
        list.add(co);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return list;
  }

  @Override
  public CompanyDTO findForCSV(CompanyDTO company, JEDTO je) {
    CompanyDTO companyReturn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices)
          .prepareStatement(SQL_SELECT_BY_ID_TO_CONTACT_FROM_JE);
      ps.setLong(1, je.getId());
      ps.setLong(2, company.getCompanyId());
      rs = ps.executeQuery();
      while (rs.next()) {
        companyReturn = buildCompany(rs);
      }
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return companyReturn;
  }


}
