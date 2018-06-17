package bizz.factory;

import bizz.AddressImpl;
import bizz.dto.AddressDTO;

public class AddressFactoryImpl implements AddressFactory {

  @Override
  public AddressDTO createAdress() {
    // TODO Auto-generated method stub
    return new AddressImpl();
  }



}
