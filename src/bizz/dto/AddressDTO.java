package bizz.dto;

public interface AddressDTO {

  public int getIdAdressFacturation();

  public String getStreet();

  public String getNumber();

  public String getBox();

  public String getLocality();

  public Long getPostCode();

  public void setIdAdressFacturation(int idAdressFacturation);

  public void setStreet(String street);

  public void setNumber(String number);

  public void setBox(String box);

  public void setLocality(String locality);

  public void setPostCode(Long postCode);

}
