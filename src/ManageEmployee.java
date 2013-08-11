
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.obs.photo.PhotoFile;
import com.obs.photo.PhotoTag;

public class ManageEmployee
{
   private static SessionFactory factory;

   public static void main(String[] args)
   {
      try
      {
         factory = new Configuration().configure().buildSessionFactory();
      }
      catch (Throwable ex)
      {
         System.err.println("Failed to create sessionFactory object." + ex);
         throw new ExceptionInInitializerError(ex);
      }
      ManageEmployee ME = new ManageEmployee();
      /* Let us have a set of certificates for the first employee */
      Set<PhotoTag> set1 = new HashSet<PhotoTag>();
      set1.add(new PhotoTag("MCA"));
      set1.add(new PhotoTag("MBA"));
      set1.add(new PhotoTag("PMP"));

      /* Add employee records in the database */
      Integer empID1 = ME.addEmployee("Manoj", "Kumar", 4000, set1);

      /* Another set of certificates for the second employee */
      Set<PhotoTag> set2 = new HashSet<PhotoTag>();
      set2.add(new PhotoTag("BCA"));
      set2.add(new PhotoTag("BA"));

      /* Add another employee record in the database */
      Integer empID2 = ME.addEmployee("Dilip", "Kumar", 3000, set2);

      /* List down all the employees */
      ME.listEmployees();

//      /* Update employee's salary records */
//      ME.updateEmployee(empID1, 5000);
//
//      /* Delete an employee from the database */
//      ME.deleteEmployee(empID2);
//
//      /* List down all the employees */
//      ME.listEmployees();

   }

   /* Method to add an employee record in the database */
   public Integer addEmployee(String fname, String lname, int salary, Set<PhotoTag> cert)
   {
      Session session = factory.openSession();
      Transaction tx = null;
      Integer employeeID = null;
      try
      {
         tx = session.beginTransaction();
         PhotoFile employee = new PhotoFile();
         employee.setTags(cert);
         employeeID = (Integer) session.save(employee);
         tx.commit();
      }
      catch (HibernateException e)
      {
         if (tx != null)
            tx.rollback();
         e.printStackTrace();
      }
      finally
      {
         session.close();
      }
      return employeeID;
   }

   /* Method to list all the employees detail */
   public void listEmployees()
   {
      Session session = factory.openSession();
      Transaction tx = null;
      try
      {
         tx = session.beginTransaction();
         List employees = session.createQuery("FROM PhotoFile").list();
         for (Iterator iterator1 = employees.iterator(); iterator1.hasNext();)
         {
            PhotoFile employee = (PhotoFile) iterator1.next();
            
            Set certificates = employee.getTags();
            for (Iterator iterator2 = certificates.iterator(); iterator2.hasNext();)
            {
               PhotoTag certName = (PhotoTag) iterator2.next();
               System.err.println("   Certificate: " + certName.getTag());
            }
         }
         tx.commit();
      }
      catch (HibernateException e)
      {
         if (tx != null)
            tx.rollback();
         e.printStackTrace();
      }
      finally
      {
         session.close();
      }
   }

   /* Method to update salary for an employee */
   public void updateEmployee(Integer EmployeeID, int salary)
   {
      Session session = factory.openSession();
      Transaction tx = null;
      try
      {
         tx = session.beginTransaction();
         PhotoFile employee = (PhotoFile) session.get(PhotoFile.class, EmployeeID);
         session.update(employee);
         tx.commit();
      }
      catch (HibernateException e)
      {
         if (tx != null)
            tx.rollback();
         e.printStackTrace();
      }
      finally
      {
         session.close();
      }
   }

   /* Method to delete an employee from the records */
   public void deleteEmployee(Integer EmployeeID)
   {
      Session session = factory.openSession();
      Transaction tx = null;
      try
      {
         tx = session.beginTransaction();
         PhotoFile employee = (PhotoFile) session.get(PhotoFile.class, EmployeeID);
         session.delete(employee);
         tx.commit();
      }
      catch (HibernateException e)
      {
         if (tx != null)
            tx.rollback();
         e.printStackTrace();
      }
      finally
      {
         session.close();
      }
   }
}