package persistence.dao;

import java.util.List;

import bizz.dto.CompanyDTO;
import bizz.dto.ContactDTO;
import bizz.dto.JEDTO;
import bizz.dto.ParticipationDTO;

/**
 * This interface extends DAO and adds specific methods related to Contacts like.
 */
public interface ContactDAO extends DAO<ContactDTO> {

  /**
   * Find a contact by its email. contactDTO is expected to contain the contact's email.
   * 
   * @param contactDTO the contact Data Transfer Object.
   * @return ContactDTO found.
   */
  ContactDTO findByEmail(ContactDTO contactDTO);

  /**
   * Return the list of the contact working for the given company.
   * 
   * @param companyDTO the contact Data Transfer Object.
   * @return list found.
   */
  List<ContactDTO> fillInfosContacts(CompanyDTO companyDTO, JEDTO jeDto);

  /**
   * Return all colums'row values in a list.
   * 
   * @return list found.
   */
  List<List<String>> getAllContactsInfoByCategory();

  /**
   * Search the corresponding contact.
   * 
   * @param c contains all informations required.
   * @return list found.
   */
  List<ContactDTO> searchContacts(ContactDTO c);

  /**
   * Insert a participation in the given JE for the given Contact.
   * 
   * @param contact contains the id of the contact.
   * @param jeDto contains the id of the je.
   * @return id of the participation.
   */
  int insertParticipation(ContactDTO contact, JEDTO jeDto);

  /**
   * Remove the participation of the given contact.
   * 
   * @param contact the contact Data Transfer Object.
   * @param jeDto the Je Data Transfer Object.
   * @return test as the boolean.
   */
  boolean deleteParticipation(ContactDTO contact, JEDTO jeDto);

  /**
   * Find all contacts related to a company. companyDTO is expected to contain the company's id.
   * 
   * @param companyDTO the company Data Transfer Object.
   * @return list of contacts.
   */
  List<ContactDTO> findByCompany(CompanyDTO companyDTO);

  /**
   * Deactivate a contact. contact is expected to contain the contact's id.
   * 
   * @param contact the contact Data Transfer Object.
   * @return boolean true if it worked.
   */
  boolean desactivateContact(ContactDTO contact);

  /**
   * Counts the number of contacts participating in the current JE.
   * 
   * @return The number of contacts participating in the current JE.
   */
  int countContactsParticipating();

  /**
   * Find the participation of a contact who's participating in the current JE.
   */
  ParticipationDTO getParticipationContact(ContactDTO contact, JEDTO jeDto);
}
