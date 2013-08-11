package com.obs.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class HibernateUtil
{
   private static final SessionFactory sessionFactory = buildSessionFactory();

   private static SessionFactory buildSessionFactory()
   {
      try
      {
         // Create the SessionFactory from hibernate.cfg.xml
         Configuration configuration = new Configuration();
         Configuration configure = configuration.configure();
         SessionFactory buildSessionFactory = configure.buildSessionFactory();
         return buildSessionFactory;
      }
      catch (Throwable ex)
      {
         // Make sure you log the exception, as it might be swallowed
         System.err.println("Initial SessionFactory creation failed." + ex);
         throw new ExceptionInInitializerError(ex);
      }
   }

   public static SessionFactory getSessionFactory()
   {
      return sessionFactory;
   }

}
