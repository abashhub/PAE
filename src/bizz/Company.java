package bizz;

import bizz.dto.CompanyDTO;

/**
 * This interface is used by the CompanyUCC, it declares the complicated methods necessary to
 * managed an company.
 *
 */
public interface Company extends CompanyDTO {
  /**
   * Checks that the registration of a company is valid.
   * 
   * @return true if it's valid, false otherwise.
   */
  boolean checkRegistration();
}
