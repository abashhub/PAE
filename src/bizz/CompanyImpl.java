package bizz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bizz.dto.AddressDTO;
import bizz.dto.ParticipationDTO;
import bizz.dto.UserDTO;
import util.Util;

/**
 * This class is one implementation of the Company interface and the CompanyDTO interface, this is
 * the implementation that should be used in production.
 */
public class CompanyImpl implements Company {
  private Long companyId;
  private int numVersion;
  private AddressDTO adresseFacturation;
  private UserDTO creater;
  private String name;
  private Date dateFirstContact;
  private List<ParticipationDTO> participations;

  /**
   * Class constructor specifying every attribute excepted for the participations.
   */
  public CompanyImpl(Long companyId, AddressDTO adresseFacturation, UserDTO creater, String name,
      Date dateFirstContact) {
    super();
    this.companyId = companyId;
    this.adresseFacturation = adresseFacturation;
    this.creater = creater;
    this.name = name;
    this.dateFirstContact = dateFirstContact;
    this.participations = new ArrayList<>();
  }

  /**
   * Class constructor specifying every attribute .
   */
  public CompanyImpl(Long companyId, AddressDTO adresseFacturation, UserDTO creater, String name,
      Date dateFirstContact, List<ParticipationDTO> participations) {
    this(companyId, adresseFacturation, creater, name, dateFirstContact);
    for (ParticipationDTO p : participations) {
      // pas sur que ça soit utile de faire ça
      this.participations.add(p);
    }
  }

  /**
   * Empty class constructor. Attributes are expected to be set through setters.
   */
  public CompanyImpl() {
    this.participations = new ArrayList<>();
    // TODO Auto-generated constructor stub
  }

  // -------------------------- Business methods -------------------------- /

  @Override
  public boolean checkRegistration() {
    if (Util.anyNull(name) || Util.anyEmptyString(name)) {
      return false;
    }
    if (Util.anyNull(adresseFacturation)) {
      return false;
    }

    // si l'adresse de facturation n'a pas été entrée, on lui assigne un string vide
    if (adresseFacturation.getBox() == null) {
      adresseFacturation.setBox("");
    }
    return true;
  }

  // -------------------------- Getters/setters --------------------------- ///

  @Override
  public String toString() {
    return "CompanyImpl [idCompany=" + companyId + ", adresseFacturation=" + adresseFacturation
        + ", createur=" + creater + ", nom=" + name + ", datePremierContact=" + dateFirstContact
        + "]";
  }

  public int getNumVersion() {
    return numVersion;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public UserDTO getCreater() {
    return creater;
  }

  public void setCreater(UserDTO creater) {
    this.creater = creater;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }

  public Date getDateFirstContact() {
    return dateFirstContact;
  }

  public void setDateFirstContact(Date dateFirstContact) {
    this.dateFirstContact = dateFirstContact;
  }

  @Override
  public AddressDTO getAdresseFacturation() {
    // TODO Auto-generated method stub
    return this.adresseFacturation;
  }

  @Override
  public void setAdresseFacturation(AddressDTO adr) {
    this.adresseFacturation = adr;
  }

  public List<ParticipationDTO> getParticipations() {
    return participations;
  }

  public void setParticipations(List<ParticipationDTO> participations) {
    this.participations = participations;
  }
}
