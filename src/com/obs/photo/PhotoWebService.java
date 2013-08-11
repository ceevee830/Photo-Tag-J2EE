package com.obs.photo;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class PhotoWebService
{
   private PhotoWebImpl mBusinessObject = new PhotoWebImpl();

   @WebMethod
   public List<String> getFilesWithThisTag(String tag)
   {
      return mBusinessObject.getFilesWithThisTag(tag);
   }
}
