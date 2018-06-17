package bizz.ucc;

import java.util.List;

import bizz.dto.JEDTO;

/**
 * This interface is meant to be used by the Servlet to call methods related to a JE.
 *
 */
public interface JEUCC {

  /**
   * Select the current JE if there is one.
   * 
   * @return a JEDTO if there is one.
   */
  JEDTO selectCurrent();

  /**
   * Create the JE, the academic year as to be valid, there can't be another JE for this year.
   * 
   * @param jeDto a DTO (Data Transfer Object) for all the information for the new JE.
   * @param companies the list of all the id's of the companies that participate to the new je
   * @return the newly registered je (a DTO) if it succeeded.
   */
  JEDTO createJE(JEDTO jeDto, List<String> companies);

  /**
   * Verify the dates for the new JE.
   * 
   * @param jeDto a DTO (Data Transfer Object) for the dates of the je.
   * @return true if there is no other JE in the same academic year.
   */
  boolean checkDates(JEDTO jeDto);


  /**
   * Select all academics years from all JE.
   * 
   * @return all academic year.
   */
  List<String> selectAllAcademicYears();


  /**
   * Select the current JE if there is one.
   * 
   * @param jeDto a DTO (Data Transfer Object) for the id of the je.
   * @return a JEDTO if there is one, null otherwise.
   */
  JEDTO select(JEDTO jeDto);

  /**
   * Set the attribute "cloture" of the current je to true.
   * 
   */
  void cloture();

  /**
   * Checks if the date of the new je will be the latest je.
   * 
   * @param jeDto a DTO (Data Transfer Object) for the date of the je.
   * @return true if so.
   */
  boolean checkDateIsLatest(JEDTO jeDto);


}
