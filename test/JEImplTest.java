import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import bizz.JEImpl;

public class JEImplTest {

  private JEImpl je;

  @Before
  public void setUp() throws Exception {
    je = new JEImpl(1L, "2017-2018", new Date(2018, 02, 15), new Date(2017, 04, 17));
    je.setNumVersion(5);
  }

  @Test
  public void testSetId() {
    je.setId(2L);
    assertEquals(new Long(2), je.getId());
  }

  @Test
  public void testSetAnneeAcademique() {
    je.setAnneeAcademique("2016-2017");
    assertEquals("2016-2017", je.getAnneeAcademique());
  }

  @Test
  public void testSetDateJournee() {
    Date dat = new Date(2044, 03, 11);
    je.setDateJournee(dat);
    assertEquals(dat, je.getDateJournee());
  }

  @Test
  public void testSetDateInvitation() {
    Date dat = new Date(2044, 03, 11);
    je.setDateInvitations(dat);
    assertEquals(dat, je.getDateInvitations());
  }

  @Test
  public void testGetId() {
    assertEquals(new Long(1), je.getId());
  }

  @Test
  public void testGetAnneeAcademique() {
    assertEquals("2017-2018", je.getAnneeAcademique());
  }

  @Test
  public void testGetDateJournee() {
    assertEquals(new Date(2018, 02, 15), je.getDateJournee());
  }

  @Test
  public void testGetDateInvitation() {
    assertEquals(new Date(2017, 04, 17), je.getDateInvitations());
  }

  @Test
  public void testIsCloturee() {
    assertFalse(je.isCloturee());
  }

  @Test
  public void testSetCloturee() {
    je.setCloturee(true);
    assertTrue(je.isCloturee());
  }

  @Test
  public void testGetNumVersion() {
    assertEquals(5, je.getNumVersion());
  }

  @Test
  public void testSetNumVersion() {
    je.setNumVersion(6);
    assertEquals(6, je.getNumVersion());
  }
}
