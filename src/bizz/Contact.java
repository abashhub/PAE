package bizz;

import bizz.dto.ContactDTO;

/**
 * This interface is used by the ContactUCC, it declares the complicated methods necessary to manage
 * a contact.
 */
public interface Contact extends ContactDTO {
  /**
   * Checks that the info of a contact are valid. Will throw a BizzException if not.
   */
  void checkContact();

  /**
   * Checks that the updated info of a contact are valid. It does less checks than checkContact().
   * Will throw a BizzException if invalid.
   */
  void checkContactForUpdate();
}
