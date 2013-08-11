package com.obs.photo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class DAO
{
   protected static final Logger log = Logger.getAnonymousLogger();
   private static final ThreadLocal<Session> session = new  ThreadLocal<Session>();
   private static final SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();

   protected DAO()
   {
      log.setLevel(Level.OFF);
   }
   
   public static Session getSession()
   {
      Session session = (Session)DAO.session.get();
      if (session == null)
      {
         session = sessionFactory.openSession();
         DAO.session.set(session);
      }
      
      return session;
   }
   
   protected void begin()
   {
      log.log(Level.INFO, "cvcvcv: About to beginTransaction");
      getSession().beginTransaction();
      log.log(Level.INFO, "cvcvcv: Just did the beginTransaction");
   }
   
   protected void commit()
   {
      log.log(Level.INFO, "cvcvcv: About to commit");
      getSession().getTransaction().commit();
      log.log(Level.INFO, "cvcvcv: Just did the commit commit");
   }
   
   protected void rollback()
   {
      try
      {
         getSession().getTransaction().rollback();
      }
      catch (HibernateException ex)
      {
         log.log(Level.WARNING, "Cannot rollback", ex);
      }
      
      try
      {
         getSession().close();
      }
      catch (HibernateException ex)
      {
         log.log(Level.WARNING, "Cannot close", ex);
      }
      
      DAO.session.set(null);
   }
   
   public static void close()
   {
      getSession().close();
      DAO.session.set(null);
   }
}
