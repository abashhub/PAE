package bizz.factory;

import bizz.dto.ContactDTO;

/**
 * 
 * This interface is used by the Servlet and the persistance package, it's purpose is to give them
 * the ContactDTO they need.
 *
 */
public interface ContactFactory {

  ContactDTO createContact();

}
