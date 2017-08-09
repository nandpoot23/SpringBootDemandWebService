package com.example.dm.customer.performance;

import com.example.spring.boot.test.util.TokenHelper

class StubProfile extends TestProfile{

  def restResponseTime():Long = {
      return 4200;
  }
  
  def restResponseTimeMax(): Int = {
      return 8500;
  }

  def soapResponseTime():Long ={
      return 4200;
  }
  
  def soapResponseTimeMax(): Int = {
      return 8500;
  }

  def apiResponseTime(): Long = {
      return 450;
  }

  def apiResponseTimeMax(): Int = {
      return 550;
  }

  def authToken(): String = {
      return TokenHelper.getToken;
  }
  
}
