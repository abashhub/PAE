package bizz.dto;

import java.util.Date;

/**
 * This interface is used by the servlet and the persistence package to stock data and to send them
 * to the JEUCC.
 *
 */
public interface JEDTO {
  /**
   * 
   * @return the ID of this JEDTO
   */
  public Long getId();

  /**
   * 
   * @return the academic year of this JEDTO
   */
  public String getAnneeAcademique();

  /**
   * 
   * @return the date of this JEDTO
   */
  public Date getDateJournee();

  /**
   * 
   * @return the date of the first invitations of this JEDTO
   */
  public Date getDateInvitations();

  /**
   * Set the parameter id as the new id
   * 
   * @param id
   */
  public void setId(Long id);

  /**
   * Set the parameter anneeAcademique as the new anneeAcademique
   * 
   * @param anneeAcademique
   */
  public void setAnneeAcademique(String anneeAcademique);

  /**
   * Set the parameter dateJournee as the new dateJournee
   * 
   * @param dateJournee
   */
  public void setDateJournee(Date dateJournee);

  /**
   * Set the parameter dateInvitation as the new dateInvitation
   * 
   * @param dateInviation
   */
  public void setDateInvitations(Date dateInviation);

  public boolean isCloturee();

  public void setCloturee(boolean cloturee);

}
