import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import bizz.ContactImpl;
import bizz.dto.CompanyDTO;
import bizz.dto.ParticipationDTO;
import exception.BizzException;

public class ContactImplTest {

  private ContactImpl cont;
  private CompanyDTO comp;
  private ParticipationDTO part;

  @Before
  public void setUp() throws Exception {
    comp = EasyMock.createMock(CompanyDTO.class);
    part = EasyMock.createMock(ParticipationDTO.class);
    cont = new ContactImpl(1L, comp, "dodo", "coco", "coco@yahoo.fr", "04", true);
    cont.setNumVersion(2);
    cont.setParticipation(part);
  }

  @Test
  public void testGetParticipation() {
    assertEquals(part, cont.getParticipation());
  }

  @Test
  public void testSetParticipation() {
    ParticipationDTO part2 = EasyMock.createMock(ParticipationDTO.class);
    cont.setParticipation(part2);
    assertEquals(part2, cont.getParticipation());
  }

  @Test
  public void testSetNumVersion() {
    cont.setNumVersion(1);
    assertEquals(1, cont.getNumVersion());
  }

  @Test
  public void testGetId() {
    assertEquals(new Long(1), cont.getId());
  }

  @Test
  public void testGetNumVersion() {
    assertEquals(2, cont.getNumVersion());
  }

  @Test
  public void testGetCompany() {
    assertEquals(comp, cont.getCompany());
  }

  @Test
  public void testGetLastName() {
    assertEquals("dodo", cont.getLastName());
  }

  @Test
  public void testGetFirstName() {
    assertEquals("coco", cont.getFirstName());
  }

  @Test
  public void testGetEmail() {
    assertEquals("coco@yahoo.fr", cont.getEmail());
  }

  @Test
  public void testGetTelephone() {
    assertEquals("04", cont.getTelephone());
  }

  @Test
  public void testIsActiv() {
    assertTrue(cont.isActive());
  }

  @Test
  public void testSetId() {
    cont.setId(7L);
    assertEquals(new Long(7), cont.getId());
  }

  @Test
  public void testSetCompany() {
    CompanyDTO company = EasyMock.createMock(CompanyDTO.class);
    cont.setCompany(company);
    assertEquals(company, cont.getCompany());
  }

  @Test
  public void testSetLastName() {
    cont.setLastName("Jerry");
    assertEquals("Jerry", cont.getLastName());
  }

  @Test
  public void testSetFirstName() {
    cont.setFirstName("Jerry");
    assertEquals("Jerry", cont.getFirstName());
  }

  @Test
  public void testSetEmail() {
    cont.setEmail("Jerry@yahoo.fr");
    assertEquals("Jerry@yahoo.fr", cont.getEmail());
  }

  @Test
  public void testSetTelephone() {
    cont.setTelephone("06");;
    assertEquals("06", cont.getTelephone());
  }

  @Test
  public void testSetStatus() {
    cont.setStatus(false);
    assertFalse(cont.isActive());
  }

  @Test
  public void testInactivate() {
    cont.inactivate();
    assertFalse(cont.isActive());
  }

  @Test(expected = BizzException.class)
  public void testCheckContact1() {
    ContactImpl cot = new ContactImpl(1L, null, "dodo", "coco", "coco@yahoo.fr", "04", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact2() {
    ContactImpl cot = new ContactImpl(1L, comp, null, "coco", "coco@yahoo.fr", "04", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact3() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", null, "coco@yahoo.fr", "04", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact4() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", null, null, true);
    cot.checkContact();
  }

  @Test
  public void testCheckContact5() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "coco@yahoo.fr", null, true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact6() {
    ContactImpl cot = new ContactImpl(1L, comp, "", "coco", "coco@yahoo.fr", "04", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact7() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "", "coco@yahoo.fr", "04", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact8() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "", "", true);
    cot.checkContact();
  }

  @Test
  public void testCheckContact9() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "coco@yahoo.fr", "", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact10() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "cocop", "04", true);
    cot.checkContact();
  }

  @Test
  public void testCheckContact11() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "", "04", true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact12() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "", null, true);
    cot.checkContact();
  }

  @Test(expected = BizzException.class)
  public void testCheckContact13() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", null, "", true);
    cot.checkContact();
  }

  @Test
  public void testToString() {
    assertEquals(
        "ContactImpl [id=1, company=EasyMock for interface bizz.dto.CompanyDTO, lastName=dodo, firstName=coco, email=coco@yahoo.fr, telephone=04, isActive=true, numVersion=2, participation=EasyMock for interface bizz.dto.ParticipationDTO]",
        cont.toString());
  }

  @Test
  public void testCheckContactForUpdate1() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "", "04", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate2() {
    ContactImpl cot = new ContactImpl(1L, comp, null, "coco", "", "04", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate3() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", null, "", "04", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate4() {
    ContactImpl cot = new ContactImpl(1L, comp, "", "coco", "", "04", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate5() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "", "", "04", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate6() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "", "", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate7() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", null, "", true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate8() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "", null, true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate9() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", null, null, true);
    cot.checkContactForUpdate();
  }

  @Test(expected = BizzException.class)
  public void testCheckContactForUpdate10() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "cocol", "", true);
    cot.checkContactForUpdate();
  }

  @Test
  public void testCheckContactForUpdate11() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", null, "04", true);
    cot.checkContactForUpdate();
  }

  @Test
  public void testCheckContactForUpdate12() {
    ContactImpl cot = new ContactImpl(1L, comp, "dodo", "coco", "coco@yahoo.fr", null, true);
    cot.checkContactForUpdate();
  }
}
