package com.obs.photo;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class PhotoDAO extends DAO
{
   public List<PhotoFile> getAllPhotoFiles() throws Exception
   {
      log.log(Level.INFO, "cvcvcv: Entering getAllPhotoFiles()");
      
      try
      {
//         begin();
         Query query = getSession().createQuery("from PhotoFile order by FILENAME");
//         commit();
         return query.list();
      }
      catch (HibernateException ex)
      {
//         rollback();
         throw new Exception("Could not getAllPhotoFiles()", ex);
      }
      finally
      {
         log.log(Level.INFO, "cvcvcv: Leaving getAllPhotoFiles()");
      }
   }

   public Set<PhotoFile> getPhotoFiles(String tag) throws Exception
   {
      log.log(Level.INFO, "cvcvcv: Entering getPhotosFiles()");
      
      try
      {
//         begin();
         Query query = getSession().createQuery("from PhotoTag where TAG='" + tag + "' order by TAG");
         PhotoTag photoTag = (PhotoTag) query.uniqueResult();
//         commit();
         Set<PhotoFile> hashSet = photoTag.getPhotoFiles();
         return new TreeSet<PhotoFile>(hashSet);
      }
      catch (HibernateException ex)
      {
//         rollback();
         throw new Exception("Could not getPhotosFiles()", ex);
      }
      finally
      {
         log.log(Level.INFO, "cvcvcv: Leaving getPhotosFiles()");
      }
   }

   public List<PhotoTag> getAllTags() throws Exception
   {
      log.log(Level.INFO, "cvcvcv: Entering getAllTags()");
      
      try
      {
//         begin();
         Query query = getSession().createQuery("from PhotoTag order by TAG");
         List<PhotoTag> list = query.list();
//         commit();
         return list;
      }
      catch (HibernateException ex)
      {
//         rollback();
         throw new Exception("Could not getAllTags()", ex);
      }
      finally
      {
         log.log(Level.INFO, "cvcvcv: Leaving getAllTags()");
      }
   }

   PhotoFile getPhotoFile(String fileToLookup) throws Exception
   {
      log.log(Level.INFO, "cvcvcv: Entering getPhotoFile(" + fileToLookup + ")");
      
      try
      {
//         begin();
         Query query = getSession().createQuery("from PhotoFile where FILENAME = '" + fileToLookup + "'");
//         commit();
         return (PhotoFile) query.uniqueResult();
      }
      catch (HibernateException ex)
      {
//         rollback();
         throw new Exception("Could not getPhotoFile(" + fileToLookup + ")", ex);
      }
      finally
      {
         log.log(Level.INFO, "cvcvcv: Leaving getPhotoFile(" + fileToLookup + ")");
      }
   }

   void delete(PhotoFile photoFile) throws Exception
   {
      log.log(Level.INFO, "cvcvcv: Entering delete <" + photoFile.getFileName() + ">");
      
      try
      {
         begin();
         getSession().delete(photoFile);
         commit();
      }
      catch (HibernateException ex)
      {
         rollback();
         throw new Exception("Could not delete photoFile=<" + photoFile.getFileName() + ">", ex);
      }
      finally
      {
         log.log(Level.INFO, "cvcvcv: Leaving delete <" + photoFile.getFileName() + ">");
      }
   }

   public PhotoFile createPhotoFile(String fileName, Date lastModified, Date lastScanned, Set<PhotoTag> photoTags) throws Exception
   {
      log.log(Level.INFO, "cvcvcv: Entering createPhotoFile <" + fileName + ">");
      
      try
      {
         begin();
         PhotoFile photoFile = new PhotoFile();
         photoFile.setFileName(fileName);
         photoFile.setLastModified(lastModified);
         photoFile.setLastScanned(lastScanned);
         photoFile.setTags(photoTags);
         getSession().save(photoFile);
         getSession().flush();
         commit();
         return photoFile;
      }
      catch (HibernateException ex)
      {
         rollback();
         throw new Exception("Could not create photoFile=<" + fileName + ">", ex);
      }
      finally
      {
         log.log(Level.INFO, "cvcvcv: Leaving createPhotoFile <" + fileName + ">");
      }
   }

}
