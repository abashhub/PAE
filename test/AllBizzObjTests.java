import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AddressImplTest.class, CompanyImplTest.class, ContactImplTest.class,
    JEImplTest.class, ParticipationImplTest.class, UserImplTest.class})
public class AllBizzObjTests {

}
