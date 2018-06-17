package bizz.ucc;

import java.util.List;

import bizz.dto.CompanyDTO;
import bizz.dto.JEDTO;

/**
 * This interface is meant to be used by the Servlet to call methods related to a company.
 */
public interface CompanyUCC {
  /**
   * Checks if the company is registered in the database. The company DTO should have a name an id
   * and an address.
   * 
   * @param companyDTO a DTO (Data Transfer Object).
   * @return a CompanyDTO to be used by the servlet.
   */
  CompanyDTO register(CompanyDTO companyDTO);

  /**
   * Ask the DataBase for all the Companies that are invited for the current JE.
   * 
   * @param je a DTO (Data Transfer Object) for the id of the current JE.
   * @return the list of companies selected by the query.
   */
  List<CompanyDTO> getAllCompaniesInvitedForJE(JEDTO je);

  /**
   * Ask the DataBase for all the Companies that are not invited for the current JE.
   * 
   * @param je a DTO (Data Transfer Object) for the id of the current JE.
   * @return the list of companies selected by the query.
   */
  List<CompanyDTO> getAllCompaniesNotInvitedForJE(JEDTO je);

  /**
   * Checks if a company with a particular id is in the DB
   * 
   * @param companyDTO a DTO (Data Transfer Object) used to transfer the id.
   * @return the companyDTO with this id if there is one.
   */
  CompanyDTO getCompany(CompanyDTO companyDTO);

  /**
   * Ask the DataBase for all the Companies that could be invited when a JE is created (must have
   * paticipated and paid in the last 4 years).
   * 
   * @param jeDto a DTO (Data Transfer Object) for the date of the JE.
   * @return the list of companies selected by the query.
   */
  List<CompanyDTO> getAllCompaniesAviableForJe(JEDTO jeDto);

  /**
   * Ask the DataBase for all the Companies that have been added since the last JE.
   * 
   * @return the list of companies selected by the query.
   */
  List<CompanyDTO> getNewCompaniesAviableForJe();

  /**
   * Insert the participation of a company for the current JE.
   * 
   * @param companyDto a DTO (Data Transfer Object) for the id of the Company.
   * @param jeCourrante a DTO (Data Transfer Object) for the id of the current JE.
   * @return the list of companies selected by the query.
   */
  boolean inviteCompany(CompanyDTO companyDto, JEDTO jeCourrante);

  /**
   * Update the company in @param if the num version is superior to the one in the data base.
   * 
   * @param companyDto a DTO (Data Transfer Object) for all the information of the Company, id to
   *        find it, numVersion to know if the update can be done, the rest for the update.
   * @return the company updated.
   */
  CompanyDTO updateCompany(CompanyDTO companyDto);

  /**
   * Select all companies and separates the fields for the research of a companies.
   * 
   * @return the list of lists containing all the names, addresses of all companies.
   */
  List<List<String>> getAllCompaniesInfoByCategory();

  /**
   * Changes the state of participation the company is in.
   * 
   * @param companyDto a DTO (Data Transfer Object) for the id of the Company, its numVersion and
   *        the state it will become and the id of the JE.
   * @param jeCourante a DTO (Data Transfer Object) for the id of the current JE, to change the
   *        right participation.
   * @return the company with its participation modified.
   */
  CompanyDTO changeState(CompanyDTO companyDto, JEDTO jeCourante);

  /**
   * Cancels the participation of the company.
   * 
   * @param companyDto a DTO (Data Transfer Object) for the id of the Company, its numVersion and
   *        the state it will become.
   * @param jeCourante a DTO (Data Transfer Object) for the id of the current JE.
   * @return the company with its participation cancelled.
   */
  CompanyDTO cancelState(CompanyDTO companyDto, JEDTO jeCourante);

  /**
   * Select all the companies with an address that start like the one in co.
   * 
   * @param co a DTO (Data Transfer Object) for the address of the Company.
   * @return A list of companies for the research.
   */
  List<CompanyDTO> searchCompanies(CompanyDTO co);

  /**
   * Select all the companies that needs to be contacted for the e.
   * 
   * @param je a DTO (Data Transfer Object) for the id of the JE.
   * @return A list of companies to contact.
   */
  List<CompanyDTO> getAllCompaniesToContactForJE(JEDTO je);

  /**
   * Cancels the participation of the company.
   * 
   * @return the list of every companies in the data base.
   */
  List<CompanyDTO> getAllCompanies();

  /**
   * Checks if a company with a particular id is in the DB for the current JE which is not "annulee"
   * 
   * @param companyDTO a DTO (Data Transfer Object) used to transfer the id.
   * @param je a DTO (Data Transfer Object) for the id of the JE.
   * @return the companyDTO with this id if there is one.
   */
  CompanyDTO getCompanyForCSV(CompanyDTO companyDTO, JEDTO je);
}
