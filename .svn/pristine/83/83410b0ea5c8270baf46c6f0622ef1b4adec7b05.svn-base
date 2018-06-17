import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import bizz.Contact;
import bizz.dto.CompanyDTO;
import bizz.dto.ContactDTO;
import bizz.dto.JEDTO;
import bizz.dto.ParticipationDTO;
import bizz.ucc.ContactUCC;
import bizz.ucc.ContactUCCImpl;
import exception.BizzException;
import persistence.DALServices;
import persistence.dao.ContactDAO;

public class ContactUCCImplTest {

  private DALServices mockDal;
  private ContactDAO daoCon;
  private ContactUCCImpl uccCon;


  @Before
  public void setUp() throws Exception {
    mockDal = EasyMock.createMock(DALServices.class);
    daoCon = EasyMock.createMock(ContactDAO.class);
    uccCon = new ContactUCCImpl(mockDal, daoCon);
  }

  @Test(expected = BizzException.class)
  public void testAddContact1() {
    Contact cd = EasyMock.createMock(Contact.class);
    cd.checkContact();
    EasyMock.expect(daoCon.findByEmail(cd)).andReturn(cd);
    EasyMock.replay(daoCon);
    EasyMock.expect(cd.getEmail()).andReturn("cd@yahoo.fr");
    EasyMock.replay(cd);
    uccCon.addContact(cd);
  }

  @Test(expected = BizzException.class)
  public void testAddContact2() {
    Contact cd = EasyMock.createMock(Contact.class);
    cd.checkContact();
    EasyMock.replay(cd);
    EasyMock.expect(daoCon.findByEmail(cd)).andReturn(null);
    mockDal.startTransaction();
    EasyMock.expect(daoCon.create(cd)).andReturn(null);
    EasyMock.replay(daoCon);
    mockDal.rollback();
    EasyMock.replay(mockDal);
    uccCon.addContact(cd);
  }

  @Test
  public void testAddContact3() {
    Contact cd = EasyMock.createMock(Contact.class);
    cd.checkContact();
    EasyMock.expect(daoCon.findByEmail(cd)).andReturn(null);
    mockDal.startTransaction();
    EasyMock.expect(daoCon.create(cd)).andReturn(5L);
    mockDal.commit();
    EasyMock.replay(mockDal);
    cd.setId(5L);
    EasyMock.replay(cd);
    EasyMock.expect(daoCon.find(cd)).andReturn(cd);
    EasyMock.replay(daoCon);
    assertEquals(uccCon.addContact(cd), cd);
  }

  @Test(expected = BizzException.class)
  public void testAddContact4() {
    uccCon.addContact(null);
  }

  @Test
  public void testFillInfosContacts1() {
    JEDTO je = EasyMock.createMock(JEDTO.class);
    CompanyDTO cd = EasyMock.createMock(CompanyDTO.class);
    List<ContactDTO> li = new ArrayList<>();
    li.add(EasyMock.createMock(ContactDTO.class));
    li.add(EasyMock.createMock(ContactDTO.class));
    mockDal.startTransaction();
    EasyMock.expect(daoCon.fillInfosContacts(cd, je)).andReturn(li);
    EasyMock.replay(daoCon);
    mockDal.commit();
    EasyMock.replay(mockDal);
    assertEquals(uccCon.fillInfosContacts(cd, je), li);
  }

  @Test(expected = BizzException.class)
  public void testFillInfosContacts2() {
    JEDTO je = EasyMock.createMock(JEDTO.class);
    uccCon.fillInfosContacts(null, je);
  }

  @Test(expected = BizzException.class)
  public void testFillInfosContacts3() {
    CompanyDTO cd = EasyMock.createMock(CompanyDTO.class);
    uccCon.fillInfosContacts(cd, null);
  }

  @Test(expected = BizzException.class)
  public void testFillInfosContacts4() {
    JEDTO je = EasyMock.createMock(JEDTO.class);
    CompanyDTO cd = EasyMock.createMock(CompanyDTO.class);
    mockDal.startTransaction();
    EasyMock.expect(daoCon.fillInfosContacts(cd, je)).andReturn(null);
    EasyMock.replay(daoCon);
    mockDal.rollback();
    EasyMock.replay(mockDal);
    uccCon.fillInfosContacts(cd, je);
  }

  @Test(expected = BizzException.class)
  public void testUpdateContatc1() {
    ContactUCC ucc = EasyMock.createMock(ContactUCC.class);
    Contact cd = EasyMock.createMock(Contact.class);
    JEDTO je = EasyMock.createMock(JEDTO.class);
    ParticipationDTO pa = EasyMock.createMock(ParticipationDTO.class);


    cd.checkContactForUpdate();
    ucc.updateParticipation(cd, je);
    EasyMock.replay(ucc);

    EasyMock.expect(daoCon.getParticipationContact(cd, je)).andReturn(pa);
    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.FALSE);

    EasyMock.expect(daoCon.insertParticipation(cd, je)).andReturn(1);
    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.FALSE);

    EasyMock.replay(pa);

    EasyMock.replay(cd);


    EasyMock.expect(daoCon.update(cd)).andReturn(null);
    EasyMock.replay(daoCon);
    uccCon.updateContact(cd, je);
  }



  @Test
  public void testUpdateContatc2() {
    ContactUCC ucc = EasyMock.createMock(ContactUCC.class);
    Contact cd = EasyMock.createMock(Contact.class);
    JEDTO je = EasyMock.createMock(JEDTO.class);
    ParticipationDTO pa = EasyMock.createMock(ParticipationDTO.class);


    cd.checkContactForUpdate();
    ucc.updateParticipation(cd, je);
    EasyMock.replay(ucc);

    EasyMock.expect(daoCon.getParticipationContact(cd, je)).andReturn(pa);
    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.FALSE);

    EasyMock.expect(daoCon.insertParticipation(cd, je)).andReturn(1);
    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.FALSE);

    EasyMock.replay(pa);

    EasyMock.replay(cd);


    EasyMock.expect(daoCon.update(cd)).andReturn(5L);
    EasyMock.replay(daoCon);
    uccCon.updateContact(cd, je);
  }

  @Test(expected = BizzException.class)
  public void testUpdateContatc3() {
    JEDTO je = EasyMock.createMock(JEDTO.class);
    uccCon.updateContact(null, je);
  }

  @Test(expected = BizzException.class)
  public void testUpdateContatc4() {
    ContactDTO cd = EasyMock.createMock(ContactDTO.class);
    uccCon.updateContact(cd, null);
  }

  @Test(expected = BizzException.class)
  public void testUpdateParticipation1() {
    JEDTO je = EasyMock.createMock(JEDTO.class);
    uccCon.updateParticipation(null, je);
  }

  @Test(expected = BizzException.class)
  public void testUpdateParticiaption2() {
    ContactDTO cd = EasyMock.createMock(ContactDTO.class);
    uccCon.updateParticipation(cd, null);
  }

  @Test
  public void testUpdateParticipation3() {
    Contact cd = EasyMock.createMock(Contact.class);
    JEDTO je = EasyMock.createMock(JEDTO.class);
    ParticipationDTO pa = EasyMock.createMock(ParticipationDTO.class);

    EasyMock.expect(daoCon.getParticipationContact(cd, je)).andReturn(null);

    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.FALSE);

    EasyMock.expect(daoCon.insertParticipation(cd, je)).andReturn(1);
    EasyMock.replay(daoCon);
    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.FALSE);

    EasyMock.replay(pa);
    EasyMock.replay(cd);

    uccCon.updateParticipation(cd, je);
  }

  @Test(expected = BizzException.class)
  public void testUpdateParticipation4() {
    Contact cd = EasyMock.createMock(Contact.class);
    JEDTO je = EasyMock.createMock(JEDTO.class);
    ParticipationDTO pa = EasyMock.createMock(ParticipationDTO.class);

    EasyMock.expect(daoCon.getParticipationContact(cd, je)).andReturn(pa);

    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.TRUE);

    EasyMock.expect(cd.getParticipation()).andReturn(pa);
    EasyMock.expect(pa.isCancelled()).andReturn(Boolean.TRUE);

    EasyMock.expect(daoCon.deleteParticipation(cd, je)).andReturn(Boolean.FALSE);
    EasyMock.replay(daoCon);
    EasyMock.replay(pa);
    EasyMock.replay(cd);


    uccCon.updateParticipation(cd, je);
  }

  @Test
  public void testGetAllContactsInfoByCategory1() {
    List<List<String>> list = new ArrayList<>();
    List<String> li = new ArrayList<>();
    li.add("ss");
    li.add("gne");
    List<String> li2 = new ArrayList<>();
    li2.add("yup");
    li2.add("nope");
    list.add(li);
    list.add(li2);
    EasyMock.expect(daoCon.getAllContactsInfoByCategory()).andReturn(list);
    EasyMock.replay(daoCon);
    assertEquals(uccCon.getAllContactsInfoByCategory(), list);
  }

  @Test(expected = BizzException.class)
  public void testGetAllContactsInfoByCategory2() {
    EasyMock.expect(daoCon.getAllContactsInfoByCategory()).andReturn(null);
    EasyMock.replay(daoCon);
    uccCon.getAllContactsInfoByCategory();
  }

  @Test
  public void testSearchContacts1() {
    List<ContactDTO> list = new ArrayList<>();
    list.add(EasyMock.createMock(ContactDTO.class));
    list.add(EasyMock.createMock(ContactDTO.class));
    ContactDTO cd = EasyMock.createMock(ContactDTO.class);
    EasyMock.expect(daoCon.searchContacts(cd)).andReturn(list);
    EasyMock.replay(daoCon);
    assertEquals(list, uccCon.searchContacts(cd));
  }

  @Test(expected = BizzException.class)
  public void testSearchContacts2() {
    uccCon.searchContacts(null);
  }

  @Test
  public void testGetContactsByCompany1() {
    List<ContactDTO> list = new ArrayList<>();
    list.add(EasyMock.createMock(ContactDTO.class));
    list.add(EasyMock.createMock(ContactDTO.class));
    CompanyDTO cd = EasyMock.createMock(CompanyDTO.class);
    EasyMock.expect(daoCon.findByCompany(cd)).andReturn(list);
    EasyMock.replay(daoCon);
    assertEquals(list, uccCon.getContactByCompany(cd));
  }

  @Test(expected = BizzException.class)
  public void testGetContactsByCompany2() {
    uccCon.getContactByCompany(null);
  }

  @Test
  public void testDesactivateContact1() {
    ContactDTO cd = EasyMock.createMock(ContactDTO.class);
    mockDal.startTransaction();
    EasyMock.expect(daoCon.desactivateContact(cd)).andReturn(Boolean.FALSE);
    EasyMock.replay(daoCon);
    mockDal.rollback();
    EasyMock.replay(mockDal);
    uccCon.desactivateContact(cd);
  }

  @Test
  public void testDesactivateContact2() {
    ContactDTO cd = EasyMock.createMock(ContactDTO.class);
    mockDal.startTransaction();
    EasyMock.expect(daoCon.desactivateContact(cd)).andReturn(Boolean.TRUE);
    EasyMock.replay(daoCon);
    mockDal.commit();
    EasyMock.replay(mockDal);
    uccCon.desactivateContact(cd);
  }

  @Test(expected = BizzException.class)
  public void testDesactivateContact3() {
    uccCon.desactivateContact(null);
  }

  @Test
  public void testCountContactsParticipating() {
    EasyMock.expect(daoCon.countContactsParticipating()).andReturn(6);
    EasyMock.replay(daoCon);
    assertEquals(6, uccCon.countContactsParticipating());
  }
}
