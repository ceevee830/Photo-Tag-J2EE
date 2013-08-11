package com.obs.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class PhotoFileManager
{
   static final String exts[] = { ".jpg", ".png", ".bmp", ".gif" };
   
   public static void main(String[] args)
   {
      if (args.length != 1)
      {
         System.out.println("usage: <parent folder>");
         System.exit(0);
      }

      PhotoFileManager mgr = new PhotoFileManager();
      mgr.scan(args[0]);
      mgr.read();
   }

   private void read()
   {
      PhotoDAO photoDAO = new PhotoDAO();
      
      try
      {
         List<PhotoFile> photos = photoDAO.getAllPhotoFiles();

         for (PhotoFile photo : photos)
         {

            System.out.println("File <" + photo.getFileName() + ">");

            // Collection<PhotoTag> tags = getTagsForThisPhoto(session, photo);
            for (PhotoTag tag : photo.getTags())
            {
               System.out.println("     Tag <" + tag.getTag() + ">");
            }
         }
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         List<PhotoTag> tags = photoDAO.getAllTags();
         for (PhotoTag tag : tags)
         {
            System.out.println("Tag <" + tag.getTag() + ">");

            Set<PhotoFile> files = tag.getPhotoFiles(); // getPhotosForThisTag(session, tag);
            for (PhotoFile file : files)
            {
               System.out.println("     File <" + file.getFileName() + ">");
            }

         }
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
      
      DAO.close();
   }

   private void scan(String parentFolder)
   {
      PhotoDAO photoDAO = new PhotoDAO();
      
      try
      {
         scanFolder(photoDAO, parentFolder);
      }
      catch (Throwable ex)
      {
         ex.printStackTrace();
         photoDAO.rollback();
      }

      DAO.close();
   }

   private void scanFolder(PhotoDAO photoDAO, String folderName)
   {
      File folder = new File(folderName);
      File[] items = folder.listFiles();

      for (File item : items)
      {
         if (item.isDirectory() && item.getAbsolutePath().indexOf(".picasaoriginals") < 0)
         {
            scanFolder(photoDAO, item.getAbsolutePath());
         }
         else
         {
            if (isResource(item))
            {
               getAndStorePhotoFile(photoDAO, item);
            }
         }
      }
   }

   private boolean isResource(File file)
   {
      boolean returnvalue = false;

      for (String ext : exts)
      {
         if (file.getAbsolutePath().toLowerCase().endsWith(ext))
         {
            returnvalue = true;
         }
      }

      return returnvalue;
   }

   public List<String> getExifTags(String filename)
   {
      return getExifTags(new File(filename));
   }

   public List<String> getExifTags(File file)
   {
      List<String> tags = new ArrayList<String>();
      
      try
      {
         final Metadata metadata = JpegMetadataReader.readMetadata(file);
         for (Directory directory : metadata.getDirectories())
         {
            for (Tag tag : directory.getTags())
            {
               if (tag.getTagName().equals("Keywords") || tag.getTagName().equals("Windows XP Keywords"))
               {
                  if (tag.getDescription().contains(";"))
                  {
                     String[] splitter = tag.getDescription().split(";");
                     for (String split : splitter)
                     {
                        tags.add(split);
                     }
                  }
                  else
                  {
                     tags.add(tag.getDescription());
                  }
                  
                  return tags;
               }
            }
         }
      }
      catch (Throwable ex)
      {
         System.err.println("cvcvcv: Could not scan <" + file.getAbsolutePath() + ">");
         ex.printStackTrace();
      }

      return tags;
   }
   
   private boolean doesThisFileNeedToBeAddedToDatabase(PhotoDAO photoDAO, File file)
   {
      boolean returnvalue = true;
      
      String fileToLookup = file.getAbsolutePath().substring(3).replace('\\', '/').replace("'", "''");

      try
      {
         PhotoFile photoFile = photoDAO.getPhotoFile(fileToLookup);

         
       //System.out.println("cvcvcv: size of list = " + list.size());
       //System.out.println("cvcvcv: file lastModified = " + file.lastModified());
             
          if (photoFile != null)
          {
             
//System.out.println("cvcvcv: file lastScanned = " + photoFile.getLastScanned().getTime());
             
             if (file.lastModified() < photoFile.getLastScanned().getTime())
             {
                returnvalue = false;
             }
             else
             {
                photoFile.getTags().clear();
                photoDAO.delete(photoFile);
             }
          }
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
      
      return returnvalue;
   }


   private void getAndStorePhotoFile(PhotoDAO photoDAO, File file)
   {
      if (doesThisFileNeedToBeAddedToDatabase(photoDAO, file))
      {
         Date lastModified = new Date(file.lastModified());
         String fileName = file.getAbsolutePath().substring(3).replace('\\', '/');

         PhotoFile photoFile = new PhotoFile();
         photoFile.setFileName(fileName);
         photoFile.setLastModified(lastModified);
         photoFile.setLastScanned(new Date());
         Set<PhotoTag> pt2 = new HashSet<PhotoTag>();

         try
         {
            List<PhotoTag> photoTags = photoDAO.getAllTags();
            List<String> exifTags = getExifTags(file);

            for (String exifTag : exifTags)
            {
               PhotoTag photoTag = null;
               Iterator<PhotoTag> iterator = photoTags.iterator();
               while (iterator.hasNext())
               {
                  PhotoTag temp = iterator.next();
                  if (temp.getTag().equals(exifTag))
                  {
                     photoTag = temp;
                     break;
                  }
               }

               if (photoTag == null)
               {
                  photoTag = new PhotoTag(exifTag);
               }

               pt2.add(photoTag);
            }
         }
         catch (Exception ex)
         {
            ex.printStackTrace();
         }

         try
         {
            photoDAO.createPhotoFile(fileName, lastModified, new Date(), pt2);
            copyFileToServer(file);
         }
         catch (Exception e)
         {
            System.err.println("item=<" + file.getAbsolutePath() + ">, " + e.getMessage());
            e.printStackTrace();
         }
      }
   }

   void copyFileToServer(File sourceFile) throws IOException
   {
      final String dir = System.getProperty("user.dir") + "/WebContent/images";
      String sourcePath = sourceFile.getAbsolutePath().substring(2).replace("\\", "/");
      File destFile = new File(dir + sourcePath);
      File parentPath = destFile.getParentFile();
      if (!parentPath.exists())
      {
         parentPath.mkdirs();
      }

      if (!destFile.exists())
      {
         destFile.createNewFile();
      }

      FileChannel source = null;
      FileChannel destination = null;

      try
      {
         source = new FileInputStream(sourceFile).getChannel();
         destination = new FileOutputStream(destFile).getChannel();
         destination.transferFrom(source, 0, source.size());
      }
      finally
      {
         if (source != null)
         {
            source.close();
         }
         if (destination != null)
         {
            destination.close();
         }
      }
   }

   <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c)
   {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list);
      return list;
   }
}
