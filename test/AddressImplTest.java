import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import bizz.AddressImpl;
import bizz.Contact;

public class AddressImplTest {
  private AddressImpl a1;

  @Before
  public void setUp() throws Exception {
    a1 = new AddressImpl(1, "des test", "5", "7", "testVille", 1790L);
  }

  @Test
  public void testToString() {
    assertEquals(
        "Address [id_adresse_facturation=1, street=des test, number=5, box=7, locality=testVille]",
        a1.toString());
  }

  @Test
  public void testGetIdAdressFacturation() {
    assertEquals(1, a1.getIdAdressFacturation());
  }

  @Test
  public void testSetIdAdressFacturation() {
    a1.setIdAdressFacturation(5);
    assertEquals(5, a1.getIdAdressFacturation());
  }

  @Test
  public void testGetStreet() {
    assertEquals("des test", a1.getStreet());
  }

  @Test
  public void testSetStreet() {
    a1.setStreet("rest");
    assertEquals("rest", a1.getStreet());
  }

  @Test
  public void testGetNumber() {
    assertEquals("5", a1.getNumber());
  }

  @Test
  public void testSetNumber() {
    a1.setNumber("8");
    assertEquals("8", a1.getNumber());
  }

  @Test
  public void testGetBox() {
    assertEquals("7", a1.getBox());
  }

  @Test
  public void testSetBox() {
    a1.setBox("2");
    assertEquals("2", a1.getBox());
  }

  @Test
  public void testGetLocality() {
    assertEquals("testVille", a1.getLocality());
  }

  @Test
  public void testSetLocality() {
    a1.setLocality("restVille");
    assertEquals("restVille", a1.getLocality());
  }

  @Test
  public void testGetPostCode() {
    assertEquals(new Long(1790), a1.getPostCode());
  }

  @Test
  public void testSetPostCode() {
    a1.setPostCode(55L);
    assertEquals(new Long(55), a1.getPostCode());
  }

  @Test
  public void testHashCode() {
    AddressImpl clone01 = a1;
    assertEquals(a1.hashCode(), clone01.hashCode());
  }

  @Test
  public void testEquals1() {
    AddressImpl clone01 = a1;
    assertTrue(a1.equals(clone01));
  }

  @Test
  public void testEquals2() {
    assertFalse(a1.equals(null));
  }

  @Test
  public void testEquals3() {
    Contact ct = EasyMock.createMock(Contact.class);
    assertFalse(a1.equals(ct));
  }

  @Test
  public void testEquals4() {
    AddressImpl a2 = new AddressImpl(2, "des test", "5", "7", "testVille", 1790L);
    assertFalse(a1.equals(a2));
  }

  @Test
  public void testEquals5() {
    assertTrue(a1.equals(a1));
  }

}
