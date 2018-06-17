package bizz.factory;

import bizz.CompanyImpl;
import bizz.dto.CompanyDTO;

/**
 * This class is one implementation of the EntrepriseFactory, this is the implementation that should
 * be used in production.
 */
public class CompanyFactoryImpl implements CompanyFactory {

  @Override
  public CompanyDTO createCompany() {
    return new CompanyImpl();
  }

}
