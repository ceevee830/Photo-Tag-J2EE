package com.obs.photo;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class PhotoFile implements Comparable<PhotoFile>
{
   private long id;
   private String fileName;
   private Date lastModified;
   private Date lastScanned;
   private Set<PhotoTag> tags = new HashSet<PhotoTag>();
   
   public PhotoFile()
   {
   }
   
   @Id
   @GeneratedValue   
   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      this.id = id;
   }

   @ManyToMany(cascade = CascadeType.ALL)
   @JoinTable(name="photofile_phototag")
   public Set<PhotoTag> getTags()
   {
      return tags;
   }

   public void setTags(Set<PhotoTag> tags)
   {
      this.tags = tags;
   }

   public String getFileName()
   {
      return fileName;
   }
   
   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }

   public Date getLastModified()
   {
      return lastModified;
   }

   public void setLastModified(Date lastModified)
   {
      this.lastModified = lastModified;
   }

   public Date getLastScanned()
   {
      return lastScanned;
   }

   public void setLastScanned(Date lastScanned)
   {
      this.lastScanned = lastScanned;
   }
   
   public String toString()
   {
      return getFileName();
   }

   @Override
   public int compareTo(PhotoFile o)
   {
      return this.getFileName().compareTo(o.getFileName());
   }
}