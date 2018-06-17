package bizz.dto;

import java.util.Date;
import java.util.List;

/**
 * This interface is used by the servlet and the persistence package to store data and to send them
 * to the CompanyUCC.
 */
public interface CompanyDTO {

  public Long getCompanyId();

  public void setCompanyId(Long companyId);

  public AddressDTO getAdresseFacturation();

  public void setAdresseFacturation(AddressDTO adr);

  public UserDTO getCreater();

  public void setCreater(UserDTO creater);

  public String getName();

  public void setName(String name);

  public Date getDateFirstContact();

  public void setDateFirstContact(Date dateFirstContact);

  public List<ParticipationDTO> getParticipations();

  public void setParticipations(List<ParticipationDTO> participations);
  
  public void setNumVersion(int numVersion);
  
  public int getNumVersion();

}
