package bizz;

import bizz.dto.AddressDTO;

/**
 * This class is one implementation of the Address interface and the AddressDTO interface, this is
 * the implementation that should be used in production.
 */
public class AddressImpl implements Address, AddressDTO {
  private int idAdressFacturation;
  private String street;
  private String number;
  private String box;
  private String locality;
  private Long postCode;

  /**
   * Empty class constructor. Attributes are expected to be set through setters.
   */
  public AddressImpl() {}

  /**
   * Class constructor specifying every attribute.
   */
  public AddressImpl(int idAdressFacturation, String street, String number, String box,
      String locality, Long postCode) {
    super();
    this.idAdressFacturation = idAdressFacturation;
    this.street = street;
    this.number = number;
    this.box = box;
    this.locality = locality;
    this.postCode = postCode;
  }

  @Override
  public String toString() {
    return "Address [id_adresse_facturation=" + idAdressFacturation + ", street=" + street
        + ", number=" + number + ", box=" + box + ", locality=" + locality + "]";
  }

  public int getIdAdressFacturation() {
    return idAdressFacturation;
  }

  public String getStreet() {
    return street;
  }

  public String getNumber() {
    return number;
  }

  public String getBox() {
    return box;
  }

  public String getLocality() {
    return locality;
  }

  public Long getPostCode() {
    return postCode;
  }

  public void setIdAdressFacturation(int idAdressFacturation) {
    this.idAdressFacturation = idAdressFacturation;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setBox(String box) {
    this.box = box;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public void setPostCode(Long postCode) {
    this.postCode = postCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + idAdressFacturation;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AddressImpl other = (AddressImpl) obj;
    if (idAdressFacturation != other.idAdressFacturation) {
      return false;
    }
    return true;
  }
}
