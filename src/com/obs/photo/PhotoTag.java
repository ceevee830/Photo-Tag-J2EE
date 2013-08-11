package com.obs.photo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class PhotoTag implements Comparable<PhotoTag>
{
   private long id;

   @Column(unique=true)
   private String tag;
   
   private Set<PhotoFile> photoFiles = new HashSet<PhotoFile>();   
   
   public PhotoTag()
   {
   }

   public PhotoTag(String tag)
   {
      this.tag = tag;
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

   public String getTag()
   {
      return tag;
   }

   public void setTag(String tag)
   {
      this.tag = tag;
   }

   @ManyToMany(mappedBy="tags", cascade = CascadeType.ALL)
   public Set<PhotoFile> getPhotoFiles()
   {
      return photoFiles;
   }

   public void setPhotoFiles(Set<PhotoFile> photoFiles)
   {
      this.photoFiles = photoFiles;
   }
   
   public String toString()
   {
      return getTag();
   }

   @Override
   public int compareTo(PhotoTag o)
   {
      return this.getTag().compareTo(o.getTag());
   }
}