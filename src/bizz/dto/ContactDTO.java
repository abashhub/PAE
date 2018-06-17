package bizz.dto;

/**
 * 
 * This interface is used by the servlet and the persistence package to stock data and to send them
 * to the ContactUCC
 *
 */
public interface ContactDTO {
  
  public Long getId();

  public CompanyDTO getCompany();

  public String getLastName();

  public String getFirstName();

  public String getEmail();

  public String getTelephone();

  public boolean isActive();

  public void setId(Long id);

  public void setCompany(CompanyDTO company);

  public void setLastName(String lastName);

  public void setFirstName(String firstName);

  public void setEmail(String email);

  public void setTelephone(String telephone);

  public void setStatus(boolean status);

  public void inactivate();
  
  public int getNumVersion();
  
  public void setNumVersion(int numVersion);
  
  public String toString();
  
  public ParticipationDTO getParticipation();
  
  public void setParticipation(ParticipationDTO participation);
}
