package com.example.dm.customer.performance;

class TestParams {

  def serviceUrl(): String = {
  
    return System.getProperty("dm.serviceURL")
  }

  def profile(): TestProfile = {
  
      val clazz = Class.forName(System.getProperty("dm.testProfile"));
      val profileDefined = clazz.newInstance();
      return profileDefined.asInstanceOf[TestProfile];
  }
  
}