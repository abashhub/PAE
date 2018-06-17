package bizz;

import java.util.Date;

import bizz.dto.JEDTO;

/**
 * This class is one implementation of the JE interface and the JEDTO interface, this is the
 * implementation that should be used in production.
 *
 */
public class JEImpl implements JEDTO, JE {
  private Long id;
  private int numVersion;
  private String anneeAcademique;
  private Date dateJournee;
  private Date dateInvitations;
  private boolean cloturee;

  /**
   * Empty class constructor. Attributes are expected to be set through setters.
   */
  public JEImpl() {
    super();
  }

  /**
   * Class constructor specifying all attributes.
   */
  public JEImpl(Long id, String anneeAcademique, Date dateJournee, Date dateInvitations) {
    super();
    this.id = id;
    this.anneeAcademique = anneeAcademique;
    this.dateJournee = dateJournee;
    this.dateInvitations = dateInvitations;
  }

  // -------------------------- Business methods -------------------------- //


  // -------------------------- Getters/setters --------------------------- //

  public void setId(Long id) {
    this.id = id;
  }

  public void setAnneeAcademique(String anneeAcademique) {
    this.anneeAcademique = anneeAcademique;
  }

  public void setDateJournee(Date dateJournee) {
    this.dateJournee = dateJournee;
  }

  public void setDateInvitations(Date dateInvitations) {
    this.dateInvitations = dateInvitations;
  }

  public Long getId() {
    return id;
  }

  public String getAnneeAcademique() {
    return anneeAcademique;
  }

  public Date getDateJournee() {
    return dateJournee;
  }

  public Date getDateInvitations() {
    return dateInvitations;
  }

  public boolean isCloturee() {
    return cloturee;
  }

  public void setCloturee(boolean cloturee) {
    this.cloturee = cloturee;
  }

  public int getNumVersion() {
    return numVersion;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }
}
