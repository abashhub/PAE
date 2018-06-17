package ihm;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import bizz.dto.CompanyDTO;
import bizz.dto.ContactDTO;
import bizz.dto.JEDTO;
import bizz.dto.UserDTO;
import bizz.factory.AddressFactory;
import bizz.factory.CompanyFactory;
import bizz.factory.ContactFactory;
import bizz.factory.JEFactory;
import bizz.factory.ParticipationFactory;
import bizz.factory.UserFactory;
import bizz.ucc.CompanyUCC;
import bizz.ucc.ContactUCC;
import bizz.ucc.JEUCC;
import bizz.ucc.UserUCC;
import config.Configuration;
import persistence.DALServices;
import persistence.dao.DAO;


/**
 * This class is the entry point of the application. It constructs the architecture, initializes the
 * properties and runs Jetty.
 */
public class Main {

  private static Logger logger = Logger.getLogger(Main.class);

  /**
   * Creates Jetty, instantiates the properties and constructs the architecture.
   * 
   * @param args the arguments passed to the main.
   */
  public static void main(String[] args) {
    PropertyConfigurator.configure("log4j.xml");

    Configuration configuration = null;
    try {
      configuration = (Configuration) Class.forName("config.Configuration").newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exc) {
      logger.fatal("Unexpected error", exc);
      System.exit(1);
    }

    // Properties initialization
    configuration.initProperties(args);

    // Instanciation sequence
    Servlet servlet = null;
    try {
      servlet = buildConfiguration(configuration);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException exc) {
      logger.fatal("Unexpected error", exc);
      System.exit(1);
    }

    // Jetty
    Server server = new Server(8080);
    WebAppContext context = new WebAppContext();

    context.setContextPath("/");
    context.addServlet(new ServletHolder(servlet), "/");
    context.addServlet(new ServletHolder(servlet), "/dispatcher");
    context.setResourceBase("www");
    context.setInitParameter("cacheControl", "no-store,nocache,must-revalidate");
    context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
    server.setHandler(context);

    try {
      server.start();
    } catch (Exception exc) {
      logger.fatal("Unexpected error", exc);
      System.exit(1);
    }
  }

  private static Servlet buildConfiguration(Configuration configuration)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

    DALServices dalServices =
        (DALServices) configuration.getClassFor(("DALServices")).newInstance();

    UserFactory userFactory =
        (UserFactory) configuration.getClassFor(("UserFactory")).newInstance();

    DAO<UserDTO> userDAO = (DAO<UserDTO>) configuration.getClassFor(("UserDAO"))
        .getDeclaredConstructor(DALServices.class, UserFactory.class)
        .newInstance(dalServices, userFactory);

    UserUCC userUcc = (UserUCC) configuration.getClassFor("UserUCC")
        .getConstructor(DALServices.class, DAO.class).newInstance(dalServices, userDAO);

    JEFactory jeFactory = (JEFactory) configuration.getClassFor(("JEFactory")).newInstance();

    DAO<JEDTO> jeDAO = (DAO<JEDTO>) configuration.getClassFor("JEDAO")
        .getConstructor(DALServices.class, JEFactory.class).newInstance(dalServices, jeFactory);

    JEUCC jeUCC = (JEUCC) configuration.getClassFor("JEUCC")
        .getConstructor(DALServices.class, DAO.class).newInstance(dalServices, jeDAO);

    AddressFactory adresseFactory =
        (AddressFactory) configuration.getClassFor("AddressFactory").newInstance();

    ParticipationFactory participationFactory =
        (ParticipationFactory) configuration.getClassFor("ParticipationFactory").newInstance();

    CompanyFactory companyFactory =
        (CompanyFactory) configuration.getClassFor("CompanyFactory").newInstance();

    DAO<CompanyDTO> companyDAO = (DAO<CompanyDTO>) configuration.getClassFor("CompanyDAO")
        .getConstructor(DALServices.class, CompanyFactory.class, UserFactory.class, DAO.class,
            ParticipationFactory.class, AddressFactory.class)
        .newInstance(dalServices, companyFactory, userFactory, userDAO, participationFactory,
            adresseFactory);

    CompanyUCC companyUCC = (CompanyUCC) configuration.getClassFor("CompanyUCC")
        .getConstructor(DALServices.class, DAO.class).newInstance(dalServices, companyDAO);

    ContactFactory contactFactory =
        (ContactFactory) configuration.getClassFor("ContactFactory").newInstance();

    DAO<ContactDTO> contactDAO = (DAO<ContactDTO>) configuration.getClassFor("ContactDAO")
        .getConstructor(DALServices.class, ContactFactory.class, CompanyFactory.class, DAO.class,
            ParticipationFactory.class)
        .newInstance(dalServices, contactFactory, companyFactory, companyDAO, participationFactory);

    ContactUCC contactUCC = (ContactUCC) configuration.getClassFor("ContactUCC")
        .getConstructor(DALServices.class, DAO.class).newInstance(dalServices, contactDAO);

    Servlet servlet = new Servlet(userUcc, userFactory, companyUCC, companyFactory, jeFactory,
        jeUCC, contactUCC, contactFactory, adresseFactory, participationFactory);

    return servlet;
  }
}
