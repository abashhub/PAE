package bizz;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bizz.dto.ParticipationDTO;

public class ParticipationImpl implements Participation, ParticipationDTO {
  public static final List<String> STATES =
      Collections.unmodifiableList(Arrays.asList("invitee", "confirmee", "facturee", "payee"));
  String state;
  boolean cancelled;
  private int numVersion;

  /**
   * Class constructor specifying every attribute.
   * 
   * @param state The state of the participation of a company.
   * @param cancelled Tells if a participation has been cancelled.
   */
  public ParticipationImpl(String state, boolean cancelled) {
    super();
    this.state = state;
    this.cancelled = cancelled;
    this.numVersion = 0;
  }

  /**
   * Empty class constructor.
   */
  public ParticipationImpl() {
    super();
    this.numVersion = 0;
  }

  // -------------------------- Business methods -------------------------- //

  @Override
  public boolean checkParticipation() {
    return ParticipationImpl.STATES.contains(this.state);
  }

  // -------------------------- Getters/setters --------------------------- //

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  public String toString() {
    return "ParticipationImpl [state=" + state + ", cancelled=" + cancelled + ", numVersion="
        + numVersion + "]";
  }

  public int getNumVersion() {
    return numVersion;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }
}
