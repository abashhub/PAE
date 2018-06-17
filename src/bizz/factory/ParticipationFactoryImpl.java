package bizz.factory;

import bizz.ParticipationImpl;
import bizz.dto.JEDTO;
import bizz.dto.ParticipationDTO;

public class ParticipationFactoryImpl implements ParticipationFactory {

  @Override
  public ParticipationDTO create(JEDTO day, String state, boolean cancelled) {
    // TODO Auto-generated method stub
    return new ParticipationImpl(state, cancelled);
  }

  @Override
  public ParticipationDTO create() {
    // TODO Auto-generated method stub
    return new ParticipationImpl();
  }

}
