package persistence.dao;

import java.util.List;

import bizz.dto.JEDTO;

public interface JEDAO extends DAO<JEDTO> {
  /**
   * Get the info for the current JE if there is one.
   * 
   * @return the current je (a DTO) if it exists, null otherwise.
   */
  JEDTO getCurrentJE();

  /**
   * Verify the dates for the new JE, if there is already a JE his academic year.
   * 
   * @param jeDto a DTO.
   * @return true is no other JE this academic year, false otherwise.
   */
  boolean checkDates(JEDTO jeDto);

  /**
   * When the JE is created, adds all the selected Companies as participation "invited" to the JE.
   * 
   * @param List<String> the list of companies to add, Long id for the id.
   * @return true is no other JE this academic year, false otherwise.
   */
  boolean addParticipations(List<String> companies, Long id_JE);

  /**
   * Get JE if there is one.
   * 
   * @param String
   * @return JEDTO
   */
  JEDTO getJE(JEDTO jeDto);

  /**
   * Get all academic years
   * 
   * @return List<String>
   */
  List<String> selectAllAcademicYears();

  /**
   * Set the state of the current JE as cloture.
   * 
   * @param currentJE containing the current JE.
   */
  void cloture(JEDTO selectCurrent);



}
