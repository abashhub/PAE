import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import bizz.User;
import bizz.dto.UserDTO;
import bizz.ucc.UserUCCImpl;
import exception.BizzException;
import persistence.DALServices;
import persistence.dao.UserDAO;

public class UserUCCImplTest {

  private DALServices mockDal;
  private UserDAO daoUse;
  private UserUCCImpl uccUse;

  @Before
  public void setUp() throws Exception {
    mockDal = EasyMock.createMock(DALServices.class);
    daoUse = EasyMock.createMock(UserDAO.class);
    uccUse = new UserUCCImpl(mockDal, daoUse);
  }

  @Test(expected = BizzException.class)
  public void testLogin1() {
    User ud = EasyMock.createMock(User.class);

    ud.checkLogin();
    mockDal.startTransaction();
    EasyMock.replay(ud);
    EasyMock.expect(daoUse.findByLogin(ud)).andReturn(null);
    mockDal.commit();
    EasyMock.replay(mockDal);
    EasyMock.replay(daoUse);
    uccUse.login(ud);
  }

  @Test
  public void testLogin2() {
    User ud = EasyMock.createMock(User.class);

    ud.checkLogin();
    mockDal.startTransaction();
    EasyMock.expect(daoUse.findByLogin(ud)).andReturn(ud);
    mockDal.commit();
    EasyMock.replay(mockDal);
    EasyMock.replay(daoUse);
    ud.checkPassword("test");
    EasyMock.expect(ud.getPassword()).andReturn("test");
    EasyMock.replay(ud);
    assertEquals(uccUse.login(ud), ud);

  }

  @Test(expected = BizzException.class)
  public void testLogin3() {
    uccUse.login(null);
  }

  @Test(expected = BizzException.class)
  public void testRegister1() {
    User us = EasyMock.createMock(User.class);
    us.checkRegistration();
    EasyMock.expect(daoUse.findByEmail(us)).andReturn(us);
    EasyMock.expect(daoUse.findByLogin(us)).andReturn(null);
    EasyMock.replay(us);
    EasyMock.replay(daoUse);
    uccUse.register(us);
  }

  @Test(expected = BizzException.class)
  public void testRegister2() {
    User us = EasyMock.createMock(User.class);
    us.checkRegistration();
    EasyMock.expect(daoUse.findByEmail(us)).andReturn(null);
    EasyMock.expect(daoUse.findByLogin(us)).andReturn(us);
    EasyMock.replay(us);
    EasyMock.replay(daoUse);
    uccUse.register(us);
  }

  @Test(expected = BizzException.class)
  public void testRegister3() {
    User us = EasyMock.createMock(User.class);
    us.checkRegistration();
    EasyMock.expect(daoUse.findByEmail(us)).andReturn(null);
    EasyMock.expect(daoUse.findByLogin(us)).andReturn(null);
    mockDal.startTransaction();
    EasyMock.expect(us.hashedPassword()).andReturn("azeaze");
    us.setPassword("azeaze");
    EasyMock.expect(daoUse.create(us)).andReturn(null);
    mockDal.rollback();
    EasyMock.replay(mockDal);
    EasyMock.replay(us);
    EasyMock.replay(daoUse);
    uccUse.register(us);
  }

  @Test
  public void testRegister4() {
    User us = EasyMock.createMock(User.class);
    us.checkRegistration();
    EasyMock.expect(daoUse.findByEmail(us)).andReturn(null);
    EasyMock.expect(daoUse.findByLogin(us)).andReturn(null);
    mockDal.startTransaction();
    EasyMock.expect(us.hashedPassword()).andReturn("azeaze");
    us.setPassword("azeaze");
    EasyMock.expect(daoUse.create(us)).andReturn(4L);
    mockDal.commit();
    EasyMock.replay(mockDal);
    us.setUserID(4L);
    EasyMock.replay(us);
    EasyMock.expect(daoUse.find(us)).andReturn(us);
    EasyMock.replay(daoUse);
    assertEquals(us, uccUse.register(us));
  }

  @Test
  public void testGetUserByLogin1() {
    UserDTO ud = EasyMock.createMock(UserDTO.class);
    EasyMock.expect(daoUse.findByLogin(ud)).andReturn(ud);
    EasyMock.replay(daoUse);
    assertEquals(ud, uccUse.getUserByLogin(ud));
  }

  @Test(expected = BizzException.class)
  public void testGetUserByLogin2() {
    UserDTO ud = EasyMock.createMock(UserDTO.class);
    EasyMock.expect(daoUse.findByLogin(ud)).andReturn(null);
    EasyMock.replay(daoUse);
    uccUse.getUserByLogin(ud);
  }

  @Test
  public void testGetUserByEmail1() {
    UserDTO ud = EasyMock.createMock(UserDTO.class);
    EasyMock.expect(daoUse.findByEmail(ud)).andReturn(ud);
    EasyMock.replay(daoUse);
    assertEquals(ud, uccUse.getUserByEmail(ud));
  }

  @Test(expected = BizzException.class)
  public void testGetUserByEmail2() {
    UserDTO ud = EasyMock.createMock(UserDTO.class);
    EasyMock.expect(daoUse.findByEmail(ud)).andReturn(null);
    EasyMock.replay(daoUse);
    uccUse.getUserByEmail(ud);
  }

}
