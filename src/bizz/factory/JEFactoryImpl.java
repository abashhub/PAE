package bizz.factory;

import bizz.JEImpl;
import bizz.dto.JEDTO;

public class JEFactoryImpl implements JEFactory {

  @Override
  public JEDTO createJE() {
    return new JEImpl();
  }



}
